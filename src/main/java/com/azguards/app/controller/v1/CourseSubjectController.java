package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseSemesterRequestWrapper;
import com.azguards.app.endpoint.CourseSubjectInterface;
import com.azguards.app.processor.CourseSemesterProcessor;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseSubjectController implements CourseSubjectInterface {

	@Autowired
	private CourseSemesterProcessor courseSubjectProcessor;

	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveUpdateCourseSemesters(String userId, String courseId,
			@Valid CourseSemesterRequestWrapper request) throws ValidationException, NotFoundException {
		log.info("inside CourseSubjectController.saveUpdateCourseSemesters");
		courseSubjectProcessor.saveUpdateCourseSemesters(userId, courseId, request);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_subject.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteByCourseSemesterIds(String userId, String courseId, List<String> courseSubjectIds,
			List<String> linkedCourseIds) throws ValidationException, NotFoundException {
		log.info("inside CourseSubjectController.deleteByCourseSemesterIds");
		courseSubjectProcessor.deleteByCourseSemesterIds(userId, courseId, courseSubjectIds, linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_subject.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
