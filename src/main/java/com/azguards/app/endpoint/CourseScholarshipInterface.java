package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.common.lib.dto.institute.CourseScholarshipDto;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/course/{courseId}/scholarship")
public interface CourseScholarshipInterface {

	@PostMapping()
	public ResponseEntity<?> saveUpdateCourseScholarship(@RequestHeader(value = "userId") final String userId,
			@PathVariable final String courseId,
			@Valid @RequestBody(required = true) final CourseScholarshipDto courseScholarshipDto)
			throws ValidationException, NotFoundException;

	@GetMapping()
	public ResponseEntity<?> getCourseScholarships(@PathVariable final String courseId)
			throws ValidationException, NotFoundException;

	@DeleteMapping()
	public ResponseEntity<?> deleteAllCourseScholarship(@RequestHeader(value = "userId") final String userId,
			@PathVariable final String courseId,
			@RequestParam(value = "linked_course_ids", required = false) final List<String> linkedCourseIds)
			throws InternalServerException, NotFoundException;
}