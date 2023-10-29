package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.app.dto.CourseOtherRequirementDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/course/{courseId}/other-requirement")
public interface CourseOtherRequirementInterface {

	@PostMapping
	public ResponseEntity<?> saveOrUpdate(@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String courseId,
			@Valid @RequestBody(required = true) final CourseOtherRequirementDto courseIntakeDto)
			throws ValidationException, NotFoundException;

	@GetMapping
	ResponseEntity<?> getCourseOtherRequirements(@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String courseId)
			throws ValidationException, NotFoundException;
}