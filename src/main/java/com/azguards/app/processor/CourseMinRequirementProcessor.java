package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseMinRequirement;
import com.azguards.app.bean.CourseMinRequirementSubject;
import com.azguards.app.bean.EducationSystem;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseMinRequirementDao;
import com.azguards.app.dao.EducationSystemDao;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.institute.CourseMinRequirementDto;
import com.azguards.common.lib.dto.institute.CourseMinRequirementSubjectDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeNotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseMinRequirementProcessor {
	@Autowired
	private CourseMinRequirementDao courseMinRequirementDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	@Lazy
	private CourseProcessor courseProcessor;

	@Autowired
	private EducationSystemDao eudcationSystemDao;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CommonProcessor commonProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public List<CourseMinRequirementDto> saveUpdateCourseMinRequirements(String userId, String courseId,
			@Valid List<CourseMinRequirementDto> courseMinRequirementDtos) {
		log.info("inside CourseMinRequirementProcessor.saveCourseMinRequirement for courseId : {}", courseId);
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("min_requirement.add.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("min_requirement.add.no.access"));
		}
		saveUpdateCourseMinRequirements(userId, course, courseMinRequirementDtos, false);

		List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
		coursesToBeSavedOrUpdated.add(course);

		List<Course> savedCourses = courseDao.saveAll(coursesToBeSavedOrUpdated);

		log.info("Send notification for course content updates");
		commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);

		commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		return savedCourses.get(0).getCourseMinRequirements().stream().map(e -> modelToDto(e)).toList();
	}
	
	public void saveUpdateCourseMinRequirements(String userId, Course course,
		@Valid List<CourseMinRequirementDto> courseMinRequirementDtos, boolean deleteMissing)
		throws ForbiddenException, NotFoundException, ValidationException {
		List<CourseMinRequirement> courseMinRequirements = course.getCourseMinRequirements();
		Map<String, CourseMinRequirement> existingCourseMinRequirementMap = courseMinRequirements.stream()
				.collect(Collectors.toMap(CourseMinRequirement::getId, e -> e));
		if(!CollectionUtils.isEmpty(courseMinRequirementDtos)) {
			if (deleteMissing) {
				Set<String> updateRequestIds = courseMinRequirementDtos.stream().filter(e -> StringUtils.hasText(e.getId()))
						.map(CourseMinRequirementDto::getId).collect(Collectors.toSet());
				courseMinRequirements.removeIf(e -> !updateRequestIds.contains(e.getId()));
			}
			courseMinRequirementDtos.stream().forEach(dto -> {
				CourseMinRequirement courseMinRequirement = new CourseMinRequirement();
				if (StringUtils.hasText(dto.getId())) {
					log.info("entityId is present so going to see if it is present in db if yes then we have to update it");
					courseMinRequirement = existingCourseMinRequirementMap.get(dto.getId());
					if (ObjectUtils.isEmpty(courseMinRequirement)) {
						log.error("invalid course min requirement id : {}", dto.getId());
						throw new RuntimeNotFoundException("invalid course min requirement id : " + dto.getId());
					}
				}
				courseMinRequirement.setCountryName(dto.getCountryName());
				courseMinRequirement.setStateName(dto.getStateName());
				courseMinRequirement.setGradePoint(dto.getGradePoint());
				courseMinRequirement.setEducationSystem(validateAndGetEducationSystem(dto.getEducationSystemId()));
				courseMinRequirement.setCourse(course);
				saveUpdateSubjects(userId, courseMinRequirement, dto.getMinRequirementSubjects());
				courseMinRequirement.setAuditFields(userId);
				courseMinRequirement.setStudyLanguages(dto.getStudyLanguages());
				log.info("going to save record in db");
				courseMinRequirements.add(courseMinRequirement);
			
			});	
		}
		else if (deleteMissing) {
			courseMinRequirements.clear();	
		}
	}
		

	@Transactional
	public PaginationResponseDto getAllCourseMinimumRequirements(String userId, String courseId, Integer pageNumber,
			Integer pageSize) {
		log.info("inside CourseMinRequirementProcessor.getAllCourseMinimumRequirements");
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<CourseMinRequirement> courseMinRequirementPage = courseMinRequirementDao.findByCourseId(courseId,
				pageable);
		List<CourseMinRequirementDto> courseMinRequirementDtos = courseMinRequirementPage.getContent().stream()
				.map(e -> modelToDto(e)).collect(Collectors.toList());
		return PaginationUtil.calculatePaginationAndPrepareResponse(PaginationUtil.getStartIndex(pageNumber, pageSize),
				pageSize, ((Long) courseMinRequirementPage.getTotalElements()).intValue(), courseMinRequirementDtos);

	}

	@Transactional
	public void deleteCourseMinRequirements(String userId, String courseId, List<String> courseMinRequirementIds,
			List<String> linkedCourseIds) throws NotFoundException, ForbiddenException, ValidationException {
		log.info("inside CourseMinRequirementProcessor.deleteCourseMinRequirement");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("min_requirement.delete.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("min_requirement.delete.no.access"));
		}

		List<CourseMinRequirement> courseMinRequirements = course.getCourseMinRequirements();
		courseMinRequirements.removeIf(e -> Utils.contains(courseMinRequirementIds, e.getId()));
		List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
		coursesToBeSavedOrUpdated.add(course);
		if (!CollectionUtils.isEmpty(linkedCourseIds)) {
			List<CourseMinRequirementDto> dtosToReplicate = courseMinRequirements.stream().map(e -> modelToDto(e))
					.collect(Collectors.toList());
			coursesToBeSavedOrUpdated.addAll(replicateCourseMinRequirements(userId, linkedCourseIds, dtosToReplicate));
		}
		courseDao.saveAll(coursesToBeSavedOrUpdated);
		commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);

		commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
	}

	private EducationSystem validateAndGetEducationSystem(String educationSystemId) throws NotFoundException {
		EducationSystem educationSystem = eudcationSystemDao.get(educationSystemId);
		if (!ObjectUtils.isEmpty(educationSystem)) {
			return educationSystem;
		} else {
			log.error(messageTranslator.toLocale("min_requirement.system.id.invalid",educationSystemId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("min_requirement.system.id.invalid",educationSystemId));
		}
	}

	private List<Course> replicateCourseMinRequirements(String userId, List<String> courseIds,
			List<CourseMinRequirementDto> courseMinRequirementDtos) throws ValidationException, NotFoundException {
		log.info("inside courseProcessor.replicateCourseMinRequirements");

		if (!CollectionUtils.isEmpty(courseIds)) {
			List<Course> courses = courseProcessor.validateAndGetCourseByIds(courseIds);
			courses.stream().forEach(course -> {
				List<CourseMinRequirement> courseMinRequirements = course.getCourseMinRequirements();
				if (CollectionUtils.isEmpty(courseMinRequirementDtos)) {
					courseMinRequirements.clear();
				} else {
					courseMinRequirements.removeIf(e -> !contains(courseMinRequirementDtos, e));
					courseMinRequirementDtos.stream().forEach(dto -> {
						Optional<CourseMinRequirement> existingMinRequirementOp = courseMinRequirements.stream()
								.filter(e -> e.getCountryName().equalsIgnoreCase(dto.getCountryName())
										&& e.getStateName().equalsIgnoreCase(dto.getStateName())
										&& e.getEducationSystem().getId().equals(dto.getEducationSystem().getId()))
								.findAny();
						CourseMinRequirement courseMinRequirement = null;
						if (existingMinRequirementOp.isPresent()) {
							courseMinRequirement = existingMinRequirementOp.get();
						} else {
							courseMinRequirement = new CourseMinRequirement();
							courseMinRequirement.setCourse(course);
							courseMinRequirements.add(courseMinRequirement);
						}
						saveUpdateSubjects(userId, courseMinRequirement, dto.getMinRequirementSubjects());
						courseMinRequirement.setCountryName(dto.getCountryName());
						courseMinRequirement.setStateName(dto.getStateName());
						courseMinRequirement.setGradePoint(dto.getGradePoint());
						courseMinRequirement.setStudyLanguages(dto.getStudyLanguages());
						courseMinRequirement.setAuditFields(userId);
					});
				}
			});
			return courses;
		}
		return new ArrayList<>();
	}

	public static boolean contains(List<CourseMinRequirementDto> lst, CourseMinRequirement target) {
		return lst.stream()
				.anyMatch(e -> e.getCountryName().equalsIgnoreCase(target.getCountryName())
						&& e.getStateName().equalsIgnoreCase(target.getStateName())
						&& e.getEducationSystem().getId().equals(target.getEducationSystem().getId()));
	}

	public CourseMinRequirementDto modelToDto(CourseMinRequirement source) {
		CourseMinRequirementDto courseMinRequirement = modelMapper.map(source, CourseMinRequirementDto.class);
		courseMinRequirement.setStudyLanguages(source.getStudyLanguages());
		return courseMinRequirement;
	}
	
	private void saveUpdateSubjects(String userId, CourseMinRequirement minRequirement, List<CourseMinRequirementSubjectDto> subjectDtos) {
		List<CourseMinRequirementSubject> subjects = minRequirement.getCourseMinRequirementSubjects();
		if (!CollectionUtils.isEmpty(subjectDtos)) {
			List<String> updateRequestIds = subjectDtos.stream().filter(e -> StringUtils.hasText(e.getId()))
					.map(CourseMinRequirementSubjectDto::getId).collect(Collectors.toList());
			subjects.removeIf(e -> !Utils.contains(updateRequestIds, e.getId()));
					
			
			log.info("preparing map of exsiting course min requirement subject");
			Map<String, CourseMinRequirementSubject> existingSubjectMap = subjects.stream().filter(e -> StringUtils.hasText(e.getId()))
					.collect(Collectors.toMap(CourseMinRequirementSubject::getId, e -> e));
			
			subjectDtos.stream().forEach(dto->{
				CourseMinRequirementSubject model = new CourseMinRequirementSubject();
				if (StringUtils.hasText(dto.getId())) {
					log.info(
							"id is present so going to see if it is present in db if yes then we have to update it");
					model = existingSubjectMap.get(dto.getId());
					if (ObjectUtils.isEmpty(model)) {
						log.error("invalid course min requirement subject id : {}", dto.getId());
						throw new RuntimeNotFoundException("invalid course min requirement subject id : " + dto.getId());
					}
				}else {
					subjects.add(model);
				}
				model.setName(dto.getName());
				model.setGrade(dto.getGrade());
				model.setAuditFields(userId);
				model.setCourseMinRequirement(minRequirement);
			});
			
		} else {
			subjects.clear();
		}
	}
}
