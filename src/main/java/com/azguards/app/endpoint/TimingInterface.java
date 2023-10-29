package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.app.dto.TimingRequestDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1")
public interface TimingInterface {

	@PostMapping("/timing")
	public ResponseEntity<?> saveOrUpdate(@RequestHeader(value = "userId", required = true) final String userId,
			@Valid @RequestBody(required = true) final TimingRequestDto courseFundingDto)
			throws ValidationException, NotFoundException;

	@DeleteMapping("/entityType/{entityType}/entityId/{entityId}/timing/{timingId}")
	public ResponseEntity<?> deleteByTimingId(@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String entityType, @PathVariable final String entityId,
			@PathVariable final String timingId) throws ValidationException, NotFoundException, ForbiddenException;
}