package com.azguards.app.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseOtherRequirementDto;
import com.azguards.app.endpoint.CourseOtherRequirementInterface;
import com.azguards.app.processor.CourseOtherRequirementProcessor;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseOtherRequirementController implements CourseOtherRequirementInterface {

	@Autowired
	private CourseOtherRequirementProcessor courseOtherRequirementProcessor;

	@Override
	public ResponseEntity<?> saveOrUpdate(String userId, String courseId,
			@Valid CourseOtherRequirementDto courseOtherRequirementDto) throws ValidationException, NotFoundException {
		log.info("inside CourseOtherRequirementController.save");
		courseOtherRequirementProcessor.saveOrUpdateOtherRequirements(userId, courseId, courseOtherRequirementDto);
		return new GenericResponseHandlers.Builder().setMessage("Course Other requirement  successfully.")
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getCourseOtherRequirements(String userId, String courseId)
			throws ValidationException, NotFoundException {
		log.info("inside CourseOtherRequirementController.save");
		return new GenericResponseHandlers.Builder().setMessage("Course Other requirement successfully.")
				.setData(courseOtherRequirementProcessor.getOtherRequirements(userId, courseId))
				.setStatus(HttpStatus.OK).create();
	}
}
