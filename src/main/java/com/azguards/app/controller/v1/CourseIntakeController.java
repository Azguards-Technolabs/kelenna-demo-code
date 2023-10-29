package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.CourseIntakeInterface;
import com.azguards.app.processor.CourseIntakeProcessor;
import com.azguards.common.lib.dto.institute.CourseIntakeDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.ValidationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseIntakeController implements CourseIntakeInterface {

	@Autowired
	private CourseIntakeProcessor courseIntakeProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveAll(String userId, String courseId, @Valid CourseIntakeDto courseIntakeDto)
			throws ValidationException, NotFoundException {
		log.info("inside CourseIntakeController.saveAll");
		ValidationUtil.validateIntakeType(courseIntakeDto.getType());
		courseIntakeProcessor.saveCourseIntake(userId, courseId, courseIntakeDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_intake.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteCourseIntake(String userId, String courseId, List<String> linkedCourseIds)
			throws ValidationException, NotFoundException, ForbiddenException {
		log.info("inside CourseIntakeController.deleteCourseIntake");
		courseIntakeProcessor.deleteCourseIntake(userId, courseId, linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_intake.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
