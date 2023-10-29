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

import com.azguards.common.lib.exception.ValidationException;
import com.azguards.app.dto.InstituteIdsRequestWrapperDto;
import com.azguards.app.dto.UnLinkInsituteDto;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1/course/{courseId}/institute")
public interface CourseInstituteInterface {

	@PostMapping
	public ResponseEntity<?> createLinks(@RequestHeader("userId") final String userId,
			@PathVariable final String courseId, @Valid @RequestBody final InstituteIdsRequestWrapperDto request)
			throws NotFoundException, ValidationException;

	@GetMapping
	public ResponseEntity<?> getLinkedInstitutes(@RequestHeader("userId") final String userId,
			@PathVariable final String courseId) throws Exception;

	@DeleteMapping
	public ResponseEntity<?> unLinkInstitutes(@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String courseId, @Valid @RequestBody final List<UnLinkInsituteDto> request) throws NotFoundException;
}