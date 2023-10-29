package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Careers;
import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseCareerOutcome;
import com.azguards.app.dao.CareerDao;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.CourseCareerOutcomeRequestWrapper;
import com.azguards.common.lib.dto.institute.CourseCareerOutcomeDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeNotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseCareerOutcomeProcessor {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private CommonProcessor commonProcessor;

	@Autowired
	private CareerDao careerDao;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void saveUpdateCourseCareerOutcomes(String userId, String courseId,
			CourseCareerOutcomeRequestWrapper request) throws NotFoundException, ValidationException {
		log.info("inside CourseCareerOutcomeProcessor.saveUpdateCourseCareerOutcomes");
		List<CourseCareerOutcomeDto> courseCareerOutcomeDtos = request.getCourseCareerOutcomeDtos();
		Course course = courseDao.get(courseId);
		if (!ObjectUtils.isEmpty(course)) {
			List<CourseCareerOutcome> careerOutcomeBeforeUpdate = course.getCourseCareerOutcomes().stream().map(outcome -> {
				CourseCareerOutcome clone = new CourseCareerOutcome();
				BeanUtils.copyProperties(outcome, clone);
				return clone;
			}).collect(Collectors.toList());
			
			Set<String> careerIds = courseCareerOutcomeDtos.stream().map(CourseCareerOutcomeDto::getCareerId)
					.collect(Collectors.toSet());
			Map<String, Careers> careersMap = getCareerByIds(careerIds.stream().collect(Collectors.toList()));

			List<CourseCareerOutcome> courseCareerOutcomes = course.getCourseCareerOutcomes();

			log.info("preparing map of exsiting course career outcomes");
			Map<String, CourseCareerOutcome> existingCourseCareerOutcomesMap = courseCareerOutcomes.stream()
					.collect(Collectors.toMap(CourseCareerOutcome::getId, e -> e));

			log.info("loop the requested list to collect the entitities to be saved/updated");
			courseCareerOutcomeDtos.stream().forEach(e -> {
				CourseCareerOutcome courseCareerOutcome = new CourseCareerOutcome();
			if (!StringUtils.isEmpty(e.getId())) {
					log.info(
							"entityId is present so going to see if it is present in db if yes then we have to update it");
					courseCareerOutcome = existingCourseCareerOutcomesMap.get(e.getId());
					if (courseCareerOutcome == null) {
						log.error(messageTranslator.toLocale("course_outcome.id.invalid" , e.getId(),Locale.US));
						throw new RuntimeNotFoundException(messageTranslator.toLocale("course_outcome.id.invalid" , e.getId()));
					}
				}else {
					courseCareerOutcomes.add(courseCareerOutcome);
}
				BeanUtils.copyProperties(e, courseCareerOutcome);
				courseCareerOutcome.setCareer(careersMap.get(e.getCareerId()));
				courseCareerOutcome.setCourse(course);
				courseCareerOutcome.setAuditFields(userId);
			});

			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			if (!CollectionUtils.isEmpty(request.getLinkedCourseIds())) {
				List<CourseCareerOutcomeDto> dtosToReplicate = courseCareerOutcomes.stream()
						.map(e -> modelMapper.map(e, CourseCareerOutcomeDto.class)).collect(Collectors.toList());
				coursesToBeSavedOrUpdated
						.addAll(replicateCourseCareerOutcomes(userId, request.getLinkedCourseIds(), dtosToReplicate));
			}
			
			log.info("going to save record in db");
			courseDao.saveAll(coursesToBeSavedOrUpdated);

			if(!careerOutcomeBeforeUpdate.equals(courseCareerOutcomes)) {
				log.info("Notify course information changed");
				commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);
			}
			
			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error(messageTranslator.toLocale("course_id.invalid", courseId, Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_id.invalid", courseId));
		}
	}

	@Transactional
	public void deleteByCourseCareerOutcomeIds(String userId, String courseId, List<String> careerOutcomeIds,
			List<String> linkedCourseIds) throws NotFoundException, ValidationException {
		log.info("inside CourseCareerOutcomeProcessor.deleteByCourseCareerOutcomeIds");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<CourseCareerOutcome> courseCareerOutcomes = course.getCourseCareerOutcomes();
		if (courseCareerOutcomes.stream().map(CourseCareerOutcome::getId).collect(Collectors.toSet())
				.containsAll(careerOutcomeIds)) {
			if (courseCareerOutcomes.stream().anyMatch(e -> !e.getCreatedBy().equals(userId))) {
				log.error(messageTranslator.toLocale("career_outcomes.no.access",Locale.US));
				throw new ForbiddenException(messageTranslator.toLocale("career_outcomes.no.access"));
			}
			courseCareerOutcomes.removeIf(e -> Utils.contains(careerOutcomeIds, e.getId()));
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			if (!CollectionUtils.isEmpty(linkedCourseIds)) {
				List<CourseCareerOutcomeDto> dtosToReplicate = courseCareerOutcomes.stream()
						.map(e -> modelMapper.map(e, CourseCareerOutcomeDto.class)).collect(Collectors.toList());
				coursesToBeSavedOrUpdated
						.addAll(replicateCourseCareerOutcomes(userId, linkedCourseIds, dtosToReplicate));
			}
			courseDao.saveAll(coursesToBeSavedOrUpdated);
			
			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);
			
			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error(messageTranslator.toLocale("course_career.outcome.id.invalid",Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_career.outcome.id.invalid"));
		}
	}

	private Map<String, Careers> getCareerByIds(List<String> careerIds) throws ValidationException {
		log.info("inside CourseCareerOutcomeProcessor.getCareerByIds");
		Map<String, Careers> careersMap = careerDao.findByIdIn(new ArrayList<>(careerIds)).stream()
				.collect(Collectors.toMap(Careers::getId, e -> e));
		if (careersMap.size() != careerIds.size()) {
			log.error(messageTranslator.toLocale("course_career.career.id.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course_career.career.id.invalid"));
		} else {
			return careersMap;
		}
	}

	private List<Course> replicateCourseCareerOutcomes(String userId, List<String> courseIds,
			List<CourseCareerOutcomeDto> courseCareerOutcomeDtos) throws ValidationException, NotFoundException {
		log.info("inside courseProcessor.replicateCourseCareerOutcomes");
		Set<String> careerIds = courseCareerOutcomeDtos.stream().map(CourseCareerOutcomeDto::getCareerId)
				.collect(Collectors.toSet());
		Map<String, Careers> careersMap = getCareerByIds(careerIds.stream().collect(Collectors.toList()));
		if (!CollectionUtils.isEmpty(courseIds)) {
			List<Course> courses = courseProcessor.validateAndGetCourseByIds(courseIds);
			courses.stream().forEach(course -> {

				List<CourseCareerOutcome> courseCareerOutcomes = course.getCourseCareerOutcomes();
				if (CollectionUtils.isEmpty(courseCareerOutcomeDtos)) {
					courseCareerOutcomes.clear();
				} else {
					courseCareerOutcomes
							.removeIf(e -> !Utils.containsIgnoreCase(careerIds.stream().collect(Collectors.toList()),
									e.getCareer().getId()));
					courseCareerOutcomeDtos.stream().forEach(dto -> {
						Optional<CourseCareerOutcome> existingScholarshipOp = courseCareerOutcomes.stream()
								.filter(e -> e.getCareer().getId().equals(dto.getCareerId())).findAny();
						CourseCareerOutcome courseCareerOutcome = null;
						if (existingScholarshipOp.isPresent()) {
							courseCareerOutcome = existingScholarshipOp.get();
						} else {
							courseCareerOutcome = new CourseCareerOutcome();
							courseCareerOutcome.setCourse(course);
							courseCareerOutcomes.add(courseCareerOutcome);
						}
						courseCareerOutcome.setAuditFields(userId);
						courseCareerOutcome.setCareer(careersMap.get(dto.getCareerId()));
					});
				}
			});
			return courses;
		}
		return new ArrayList<>();
	}
}