package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseMinRequirementRequestWrapper;
import com.azguards.app.endpoint.CourseMinRequirementInterface;
import com.azguards.app.processor.CourseMinRequirementProcessor;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseMinRequirementController implements CourseMinRequirementInterface {

	@Autowired
	private CourseMinRequirementProcessor courseMinRequirementProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<Object> saveUpdateCourseMinRequirements(String userId, String courseId,
			@Valid CourseMinRequirementRequestWrapper request) {
		log.info("inside CourseMinRequirementController.saveCourseMinRequirement");
		return new GenericResponseHandlers.Builder()
				.setData(courseMinRequirementProcessor.saveUpdateCourseMinRequirements(userId, courseId,
						request.getCoursePreRequisiteDtos()))
				.setMessage(messageTranslator.toLocale("course_min.req.added")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> getAllCourseMinimumRequirements(String userId, String courseId, Integer pageNumber,
			Integer pageSize) {
		log.info("inside CourseMinRequirementController.getAllCourseMinimumRequirements");
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("course_min.req.retrieved")).setData(courseMinRequirementProcessor
						.getAllCourseMinimumRequirements(userId, courseId, pageNumber, pageSize))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> deleteCourseMinRequirements(String userId, String courseId,
			List<String> courseMinRequirementIds, List<String> linkedCourseIds) {
		log.info("inside CourseMinRequirementController.deleteCourseMinRequirement");
		courseMinRequirementProcessor.deleteCourseMinRequirements(userId, courseId, courseMinRequirementIds,
				linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_min.req.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
