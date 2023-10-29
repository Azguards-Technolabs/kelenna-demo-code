package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseEnglishEligibilityRequestWrapper;
import com.azguards.app.endpoint.CourseEnglishEligibilityInterface;
import com.azguards.app.processor.CourseEnglishEligibilityProcessor;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseEnglishEligibilityController implements CourseEnglishEligibilityInterface {

	@Autowired
	private CourseEnglishEligibilityProcessor courseEnglishEligibilityProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveUpdateCourseEnglishEligibility(String userId, String courseId,
			@Valid CourseEnglishEligibilityRequestWrapper request) throws ValidationException, NotFoundException {
		log.info("inside CourseEnglishEligibilityController.saveUpdateCourseEnglishEligibility");
		courseEnglishEligibilityProcessor.saveUpdateCourseEnglishEligibilities(userId, courseId, request);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("course_eligibility.added")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteByCourseEnglishEligibilityIds(String userId, String courseId,
			List<String> courseEnglishEligibilityIds, List<String> linkedCourseIds)
			throws ValidationException, NotFoundException {
		log.info("inside CourseEnglishEligibilityController.deleteByCourseEnglishEligibilityIds");
		courseEnglishEligibilityProcessor.deleteByCourseEnglishEligibilityIds(userId, courseId,
				courseEnglishEligibilityIds, linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_eligibility.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
