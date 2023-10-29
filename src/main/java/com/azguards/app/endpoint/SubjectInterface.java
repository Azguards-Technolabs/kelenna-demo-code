package com.azguards.app.endpoint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/subject")
public interface SubjectInterface {

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getAllSubjects(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize, @NotNull @RequestParam("name") String name,
			@NotEmpty @RequestParam("education_system_id") String educationSystemId)
			throws ValidationException, NotFoundException, InvokeException;
}
