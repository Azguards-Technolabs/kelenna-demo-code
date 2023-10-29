package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping(path = "/api/v1")
public interface InstituteCampusInterface {

	@PostMapping("campus/instituteId/{instituteId}")
	public ResponseEntity<?> addCampus(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId, @RequestBody final @NotEmpty List<String> instituteIds)
			throws NotFoundException, ValidationException;

	@GetMapping("/campus/instituteId/{instituteId}")
	public ResponseEntity<?> getInstituteCampuses(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId) throws NotFoundException;

	@DeleteMapping("/campus/instituteId/{instituteId}")
	public ResponseEntity<?> removeCampus(@RequestHeader(value = "userId", required = true) final String userId,
			@PathVariable final String instituteId, @RequestBody final @NotEmpty List<String> instituteIds)
			throws NotFoundException;
}
