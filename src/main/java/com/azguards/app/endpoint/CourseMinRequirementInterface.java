package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.app.dto.CourseMinRequirementRequestWrapper;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/course/{courseId}/min-requirement")
public interface CourseMinRequirementInterface {

	@PostMapping
	public ResponseEntity<Object> saveUpdateCourseMinRequirements(@RequestHeader(value = "userId") final String userId,
			@PathVariable final String courseId,
			@Valid @RequestBody(required = true) final CourseMinRequirementRequestWrapper request)
			throws ValidationException, NotFoundException, ForbiddenException;

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<Object> getAllCourseMinimumRequirements(@RequestHeader(value = "userId") final String userId,
			@PathVariable final String courseId, @PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize) throws InternalServerException, NotFoundException, ForbiddenException;

	@DeleteMapping
	public ResponseEntity<Object> deleteCourseMinRequirements(@RequestHeader(value = "userId") final String userId,
			@PathVariable final String courseId,
			@RequestParam(value = "course_min_requirement_ids", required = true) @NotEmpty final List<String> courseMinIds,
			@RequestParam(value = "linked_course_ids", required = false) final List<String> linkedCourseIds)
			throws InternalServerException, NotFoundException, ForbiddenException, ValidationException;
}