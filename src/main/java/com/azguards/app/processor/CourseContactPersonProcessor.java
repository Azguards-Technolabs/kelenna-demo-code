package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseContactPerson;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.CourseContactPersonRequestWrapper;
import com.azguards.common.lib.dto.institute.CourseContactPersonDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseContactPersonProcessor {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private CommonProcessor commonProcessor;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void saveCourseContactPersons(String userId, String courseId, CourseContactPersonRequestWrapper request)
			throws NotFoundException, ValidationException, InvokeException {
		log.info("inside CourseContactPersonProcessor.saveCourseContactPersons for courseId: {}", courseId);
		List<CourseContactPersonDto> courseContactPersonDtos = request.getCourseContactPersonDtos();
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		log.info("going to see if user ids are valid");
		
		commonProcessor.validateAndGetUsersByUserIds(
				userId,
				courseContactPersonDtos.stream().map(CourseContactPersonDto::getUserId).collect(Collectors.toList()));
		log.debug("going to process the request");
		List<CourseContactPerson> courseContactPersons = course.getCourseContactPersons();
		courseContactPersonDtos.stream().forEach(e -> {
			CourseContactPerson courseContactPerson = new CourseContactPerson();
			courseContactPerson.setAuditFields(userId);
			courseContactPerson.setCourse(course);
			courseContactPerson.setUserId(e.getUserId());
			courseContactPersons.add(courseContactPerson);
		});
		log.debug("going to save the list in db");
		
		List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
		coursesToBeSavedOrUpdated.add(course);
		if (!CollectionUtils.isEmpty(request.getLinkedCourseIds())) {
			List<CourseContactPersonDto> dtosToReplicate = courseContactPersons.stream()
					.map(e -> modelMapper.map(e, CourseContactPersonDto.class)).collect(Collectors.toList());
			coursesToBeSavedOrUpdated
					.addAll(replicateCourseContactPersons(userId, request.getLinkedCourseIds(), dtosToReplicate));
		}
		courseDao.saveAll(coursesToBeSavedOrUpdated);
		
		if(!CollectionUtils.isEmpty(coursesToBeSavedOrUpdated)) {
			log.info("Notify course information changed");
			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);
		}

		commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
	}

	@Transactional
	public void deleteCourseContactPersonsByUserIds(String userId, String courseId, List<String> userIds,
			List<String> linkedCourseIds) throws NotFoundException, ValidationException {
		log.info("inside CourseContactPersonProcessor.deleteCourseContactPersonsByUserIds");

		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<CourseContactPerson> courseContactPersons = course.getCourseContactPersons();
		if (courseContactPersons.stream().map(CourseContactPerson::getUserId).collect(Collectors.toSet())
				.containsAll(userIds)) {
			if (courseContactPersons.stream().anyMatch(e -> !e.getCreatedBy().equals(userId))) {
				log.error(messageTranslator.toLocale("course_contact_person.delete.no.access" , userId,Locale.US));
				throw new ForbiddenException(messageTranslator.toLocale(
						"course_contact_person.delete.no.access" , userId));
			}
			courseContactPersons.removeIf(e -> Utils.contains(userIds, e.getUserId()));
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			if (!CollectionUtils.isEmpty(linkedCourseIds)) {
				List<CourseContactPersonDto> dtosToReplicate = courseContactPersons.stream()
						.map(e -> modelMapper.map(e, CourseContactPersonDto.class)).collect(Collectors.toList());
				coursesToBeSavedOrUpdated
						.addAll(replicateCourseContactPersons(userId, linkedCourseIds, dtosToReplicate));
			}
			courseDao.saveAll(coursesToBeSavedOrUpdated);
			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);
			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error(messageTranslator.toLocale("course_contact_person.user.id.invalid",Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_contact_person.user.id.invalid"));
		}
	}

	private List<Course> replicateCourseContactPersons(String userId, List<String> courseIds,
			List<CourseContactPersonDto> courseContactPersonDtos) throws ValidationException, NotFoundException {
		log.info("inside courseProcessor.replicateCourseContactPersons");
		Set<String> userIds = courseContactPersonDtos.stream().map(CourseContactPersonDto::getUserId)
				.collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(courseIds)) {
			List<Course> courses = courseProcessor.validateAndGetCourseByIds(courseIds);
			courses.stream().forEach(course -> {
				List<CourseContactPerson> courseContactPersons = course.getCourseContactPersons();
				if (CollectionUtils.isEmpty(courseContactPersonDtos)) {
					courseContactPersons.clear();
				} else {
					courseContactPersons.removeIf(e -> !Utils
							.containsIgnoreCase(userIds.stream().collect(Collectors.toList()), e.getUserId()));
					courseContactPersonDtos.stream().forEach(dto -> {
						Optional<CourseContactPerson> existingContactPersonOp = courseContactPersons.stream()
								.filter(e -> e.getUserId().equals(dto.getUserId())).findAny();
						CourseContactPerson courseContactPerson = null;
						if (existingContactPersonOp.isPresent()) {
							courseContactPerson = existingContactPersonOp.get();
						} else {
							courseContactPerson = new CourseContactPerson();
							courseContactPerson.setCourse(course);
							courseContactPersons.add(courseContactPerson);
						}
						courseContactPerson.setAuditFields(userId);
						courseContactPerson.setUserId(dto.getUserId());
					});
				}
			});
			return courses;
		}
		return new ArrayList<>();
	}
}