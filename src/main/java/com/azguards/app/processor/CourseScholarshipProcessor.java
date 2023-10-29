package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseScholarship;
import com.azguards.app.bean.CourseScholarshipItem;
import com.azguards.app.bean.Scholarship;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseScholarshipDao;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.ScholarshipDao;
import com.azguards.common.lib.dto.institute.CourseScholarshipDto;
import com.azguards.common.lib.dto.institute.ScholarshipResponseDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseScholarshipProcessor {

	@Autowired
	CourseScholarshipDao courseScholarshipDao;

	@Autowired
	InstituteDao instituteDao;

	@Autowired
	CourseDao courseDao;

	@Autowired
	CourseProcessor courseProcessor;

	@Autowired
	private ScholarshipDao scholarshipDao;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void saveUpdateCourseScholarships(String userId, String courseId,
			@Valid CourseScholarshipDto courseScholarshipDto)
			throws ForbiddenException, NotFoundException, ValidationException {
		log.info("inside CourseScholarshipProcessor.saveUpdateCourseScholarship");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("course_scholarship.add.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("course_scholarship.add.no.access"));
		} else {
			CourseScholarship courseScholarship = course.getCourseScholarship();
			if (ObjectUtils.isEmpty(courseScholarship)) {
				courseScholarship = new CourseScholarship();
			}
			Map<String, Scholarship> mapScholarships = getScholarshipByIds(courseScholarshipDto.getScholarshipIds());
			List<CourseScholarshipItem> dbCourseScholarshipItems = courseScholarship.getScholarshipItems();

			log.info("remove the payment items not present in request");
			dbCourseScholarshipItems.removeIf(
					e -> !Utils.contains(courseScholarshipDto.getScholarshipIds(), e.getScholarship().getId()));
			final CourseScholarship finalCourseScholarship = courseScholarship;
			courseScholarshipDto.getScholarshipIds().stream().forEach(e -> {
				Optional<CourseScholarshipItem> existingCourseScholarshipItemOp = dbCourseScholarshipItems.stream()
						.filter(t -> t.getScholarship().getId().equals(e)).findAny();
				if (!existingCourseScholarshipItemOp.isPresent()) {
					CourseScholarshipItem courseScholarshipItem = new CourseScholarshipItem();
					log.info("scholarship item not present so going to create");
					courseScholarshipItem.setScholarship(mapScholarships.get(e));
					courseScholarshipItem.setCourseScholarship(finalCourseScholarship);
					courseScholarshipItem.setAuditFields(userId);
					dbCourseScholarshipItems.add(courseScholarshipItem);
				}
			});
			courseScholarship.setDescription(courseScholarshipDto.getDescription());
			courseScholarship.setCourse(course);
			courseScholarship.setAuditFields(userId);
			log.info("going to save record in db");
			courseScholarshipDao.save(courseScholarship);
			log.info("Calling elastic service to save/update course on elastic index having courseId: ", courseId);
//			elasticHandler.saveUpdateData(Arrays.asList(DTOUtils.convertToCourseDTOElasticSearchEntity(course)));
		}
	}

	@Transactional
	public void deleteCourseScholarship(String userId, String courseId, List<String> linkedCourseIds)
			throws ForbiddenException, NotFoundException, InternalServerException {
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("course_scholarship.delete.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("course_scholarship.delete.no.access"));
		}
		CourseScholarship courseScholarship = course.getCourseScholarship();
		if (!ObjectUtils.isEmpty(courseScholarship)) {
			course.setCourseScholarship(null);
			try {
				courseDao.addUpdateCourse(course);
				log.info("Calling elastic service to save/update course on elastic index having courseId: ", courseId);
//				elasticHandler.saveUpdateData(Arrays.asList(DTOUtils.convertToCourseDTOElasticSearchEntity(course)));
			} catch (ValidationException e) {
				log.error(messageTranslator.toLocale("course_scholarship.delete.error",Locale.US));
				throw new InternalServerException(messageTranslator.toLocale("course_scholarship.delete.error"));
			}
		} else {
			log.error(messageTranslator.toLocale("course_scholarship.notfound",Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_scholarship.notfound"));
		}
	}

	private Map<String, Scholarship> getScholarshipByIds(List<String> scholarshipIds) throws ValidationException {
		log.info("inside CourseScholarshipProcessor.getScholarshipByIds");
		Map<String, Scholarship> scholarshipsMap = scholarshipDao.getScholarshipByIds(new ArrayList<>(scholarshipIds))
				.stream().collect(Collectors.toMap(Scholarship::getId, e -> e));
		if (scholarshipsMap.size() != scholarshipIds.size()) {
			log.error(messageTranslator.toLocale("course_scholarship.ids.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course_scholarship.ids.invalid"));
		} else {
			return scholarshipsMap;
		}
	}

	@Transactional(readOnly = true)
	public CourseScholarshipDto getCourseScholarship(String courseId) throws NotFoundException {
		CourseScholarship courseScholarship = courseScholarshipDao.findByCourseId(courseId);
		if (!ObjectUtils.isEmpty(courseScholarship)) {
			CourseScholarshipDto dto = new CourseScholarshipDto();
			dto.setId(courseScholarship.getId());
			dto.setDescription(courseScholarship.getDescription());
			dto.setScholarships(courseScholarship.getScholarshipItems().stream()
					.map(e -> modelMapper.map(e.getScholarship(), ScholarshipResponseDto.class))
					.collect(Collectors.toList()));
			return dto;
		} else {
			log.error(messageTranslator.toLocale("course_scholarship.course.id.notfound",courseId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_scholarship.course.id.notfound",courseId));
		}
	}
}