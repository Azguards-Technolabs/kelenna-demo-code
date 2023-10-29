package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/career-test-result")
public interface CareerTestResultInterface {
	@PostMapping("/{entityType}")
	public ResponseEntity<?> saveOrUpdateResult(@RequestHeader(required = true) final String userId,
			@PathVariable String entityType, @RequestBody @NotEmpty List<String> entityIds);

	@GetMapping("/is-completed")
	public ResponseEntity<?> isTestCompleted(@RequestHeader(required = true) final String userId);
}
