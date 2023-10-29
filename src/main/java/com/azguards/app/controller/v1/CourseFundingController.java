package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseFundingRequestWrapper;
import com.azguards.app.endpoint.CourseFundingInterface;
import com.azguards.app.processor.CourseFundingProcessor;
import com.azguards.common.lib.dto.institute.CourseFundingDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseFundingController implements CourseFundingInterface {

	@Autowired
	private CourseFundingProcessor courseFundingProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> addFundingToAllInstituteCourses(String userId, String instituteId,
			CourseFundingDto courseFundingDto) throws ValidationException, NotFoundException, InvokeException {
		log.info("inside CourseFundingController.addFundingToAllInstituteCourses");
		courseFundingProcessor.addFundingToAllInstituteCourses(userId, instituteId,
				courseFundingDto.getFundingNameId());
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_funding.all.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> saveAll(String userId, String courseId, @Valid CourseFundingRequestWrapper request)
			throws ValidationException, NotFoundException, InvokeException {
		log.info("inside CourseFundingController.saveAll");
		courseFundingProcessor.saveCourseFundings(userId, courseId, request);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_funding.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteByFundingNameIds(String userId, String courseId, List<String> fundingNameIds,
			List<String> linkedCourseIds) throws ValidationException, NotFoundException, ForbiddenException {
		log.info("inside CourseFundingController.deleteByFundingNameIds");
		courseFundingProcessor.deleteCourseFundingsByFundingNameIds(userId, courseId, fundingNameIds, linkedCourseIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course_funding.deleted"))
				.setStatus(HttpStatus.OK).create();
	}
}
