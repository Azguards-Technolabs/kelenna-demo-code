package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.app.dto.InstituteServiceDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteServiceInterface {

	@PostMapping("/institute/service/instituteId/{instituteId}")
	public ResponseEntity<?> addInstituteService(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId, @Valid @RequestBody List<InstituteServiceDto> instituteServiceDto)
			throws Exception;

	@DeleteMapping("/institute/service/{instituteServiceId}")
	public ResponseEntity<?> deleteById(@RequestHeader("userId") final String userId, @PathVariable final String instituteServiceId) throws NotFoundException, InvokeException;

	@GetMapping("/institute/service/instituteId/{instituteId}")
	public ResponseEntity<?> getInstituteServices(@PathVariable final String instituteId);

}