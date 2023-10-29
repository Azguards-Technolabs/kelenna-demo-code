package com.azguards.app.endpoint;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1/career/job")
public interface CareerTestInterface {

	@GetMapping("/skill/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobSkills(@RequestHeader("userId") String userId,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@RequestParam(value = "level_id", required = false) String levelId,
			@RequestParam(value = "job_id", required = false) String jobId);

	@GetMapping("/working/style/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobWorkingStyles(@RequestHeader("userId") String userId,
			@RequestParam List<String> jobIds, @PathVariable Integer pageNumber, @PathVariable Integer pageSize);

	@GetMapping("/subject/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobSubjects(@RequestHeader("userId") String userId,
			@RequestParam List<String> jobIds, @PathVariable Integer pageNumber, @PathVariable Integer pageSize);

	@GetMapping("/type/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobTypes(@RequestHeader("userId") String userId,
			@RequestParam List<String> jobIds, @PathVariable Integer pageNumber, @PathVariable Integer pageSize);

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobs(@RequestHeader("userId") String userId, @RequestParam List<String> jobIds,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize);

	@GetMapping("/autosuggest/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getCareerJobsByName(@RequestHeader("userId") String userId, @RequestParam String name,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize) throws ValidationException;

	@GetMapping("/related/career/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getRelatedCareers(@RequestParam List<String> careerIds, @PathVariable Integer pageNumber,
			@PathVariable Integer pageSize);

	@GetMapping("/related/course/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getRelatedCourseBasedOnCareerTest(@RequestParam List<String> jobIds,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize);

	@GetMapping("/{jobId}")
	public ResponseEntity<?> getCareerJobById(@PathVariable String jobId) throws NotFoundException;
}
