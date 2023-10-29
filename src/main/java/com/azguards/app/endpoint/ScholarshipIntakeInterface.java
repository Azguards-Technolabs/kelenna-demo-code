package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.app.dto.ValidList;
import com.azguards.common.lib.dto.institute.ScholarshipIntakeDto;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1/scholarship/{scholarshipId}/intake")
public interface ScholarshipIntakeInterface {

	@PostMapping
	public ResponseEntity<?> saveUpdateScholarshipIntake(
			@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String scholarshipId,
			@Valid @RequestBody(required = true) final ValidList<ScholarshipIntakeDto> scholarshipIntakeDtos)
			throws ValidationException, NotFoundException;

	@DeleteMapping
	public ResponseEntity<?> deleteByScholarshipIntakeIds(
			@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String scholarshipId,
			@RequestParam(value = "scholarship_intake_ids", required = true) @NotEmpty final List<String> scholarshipIntakeIds)
			throws ValidationException, NotFoundException, ForbiddenException;
}