package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseDeliveryModeRequestWrapper;
import com.azguards.app.endpoint.CourseDeliveryModeInterface;
import com.azguards.app.processor.CourseDeliveryModesProcessor;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseDeliveryModeController implements CourseDeliveryModeInterface {

	@Autowired
	private CourseDeliveryModesProcessor courseDeliveryModeProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveUpdateCourseDeliveryMode(String userId, String courseId,
			@Valid CourseDeliveryModeRequestWrapper request)
			throws ValidationException, NotFoundException, InternalServerException {
		log.info("inside CourseDeliveryModeController.saveUpdateCourseDeliveryMode");
		courseDeliveryModeProcessor.saveUpdateCourseDeliveryModes(userId, courseId, request);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_delivery.mode.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteByCourseDeliveryModeIds(String userId, String courseId,
			List<String> courseDeliveryModeIds, List<String> linkedCourseIds)
			throws ValidationException, NotFoundException {
		log.info("inside CourseDeliveryModeController.deleteByCourseDeliveryModeIds");
		courseDeliveryModeProcessor.deleteByCourseDeliveryModeIds(userId, courseId, courseDeliveryModeIds,
				linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_delivery.mode.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
