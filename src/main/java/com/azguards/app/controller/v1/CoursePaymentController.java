package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.CoursePaymentInterface;
import com.azguards.app.processor.CoursePaymentProcessor;
import com.azguards.common.lib.dto.institute.CoursePaymentDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CoursePaymentController implements CoursePaymentInterface {

	@Autowired
	private CoursePaymentProcessor coursePaymentProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveUpdateCoursePayment(String userId, String courseId,
			@Valid CoursePaymentDto coursePaymentDto)
			throws ValidationException, NotFoundException, ForbiddenException {
		log.info("inside CoursePaymentController.saveUpdateCoursePayment method");
		coursePaymentProcessor.saveUpdateCoursePayment(userId, courseId, coursePaymentDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_payment.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteCoursePayment(String userId, String courseId, List<String> linkedCourseIds)
			throws InternalServerException, NotFoundException, ForbiddenException {
		log.info("inside CoursePaymentController.deleteCoursePayment method");
		coursePaymentProcessor.deleteCoursePayment(userId, courseId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_payment.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
