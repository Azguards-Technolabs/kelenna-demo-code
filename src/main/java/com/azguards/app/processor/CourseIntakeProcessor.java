package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseIntake;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseIntakeDao;
import com.azguards.common.lib.dto.institute.CourseIntakeDto;
import com.azguards.common.lib.enumeration.IntakeType;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseIntakeProcessor {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private CourseIntakeDao courseIntakeDao;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommonProcessor commonProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void saveCourseIntake(String userId, String courseId, CourseIntakeDto courseIntakeDto)
			throws NotFoundException, ValidationException {
		log.info("inside CourseIntakeProcessor.saveCourseIntake");
		Course course = courseDao.get(courseId);
		if (!ObjectUtils.isEmpty(course)) {

			CourseIntake courseIntake = course.getCourseIntake();
			if (ObjectUtils.isEmpty(courseIntake)) {
				courseIntake = new CourseIntake();
			}
			courseIntake.setAuditFields(userId);
			courseIntake.setCourse(course);
			courseIntake.setType(IntakeType.valueOf(courseIntakeDto.getType()));
			courseIntake.setDates(courseIntakeDto.getDates());
			course.setCourseIntake(courseIntake);
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			courseDao.saveAll(coursesToBeSavedOrUpdated);

			log.info("Send notification for course content updates");
			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);

			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error(messageTranslator.toLocale("course_intake.course.id.invalid",courseId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_intake.course.id.invalid",courseId));
		}
	}

	@Transactional
	public void deleteCourseIntake(String userId, String courseId, List<String> linkedCourseIds)
			throws NotFoundException, ForbiddenException, ValidationException {
		log.info("inside CourseIntakeProcessor.deleteByCourseIntakeIds");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		if (!ObjectUtils.isEmpty(course.getCourseIntake())) {
			courseIntakeDao.deleteById(course.getCourseIntake().getId());
			course.setCourseIntake(null);
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);

			courseDao.saveAll(coursesToBeSavedOrUpdated);

			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);

			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		}
	}
}