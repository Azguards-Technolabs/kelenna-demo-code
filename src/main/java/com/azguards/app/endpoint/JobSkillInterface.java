package com.azguards.app.endpoint;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/job")
public interface JobSkillInterface {

	@GetMapping("/skill/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getJobSkills(@RequestHeader("userId") String userId,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@RequestParam(value = "job_names", required = false) List<String> jobNames);
}
