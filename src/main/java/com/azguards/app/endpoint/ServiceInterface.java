package com.azguards.app.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1/service")
public interface ServiceInterface {

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getAllServices(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize) throws ValidationException, NotFoundException, InvokeException;
}
