package com.azguards.app.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.common.lib.dto.institute.FacultyDto;

@RequestMapping("/api/v1/faculty")
public interface FacultyEndpoint {

	@PostMapping
	public ResponseEntity<?> saveFaculty(@RequestBody FacultyDto facultyDto) throws Exception;

	@GetMapping
	public ResponseEntity<?> getAll() throws Exception;

	@GetMapping("/{facultyId}")
	public ResponseEntity<?> getFacultyById(@PathVariable String facultyId) throws Exception;

	@GetMapping("/name/{facultyName}")
	public ResponseEntity<?> getFacultyByFacultyName(@PathVariable("facultyName") String facultyName) throws Exception;
	
}
