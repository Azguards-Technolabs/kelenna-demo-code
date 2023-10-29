package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.app.dto.InstituteRecommendPageRequestDto;


@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteRecommedPageRequestInterface {

	@PostMapping("/recommend/institute")
	public ResponseEntity<?> recommendInstitutePage(@RequestHeader("userId") final String userId,
			 @Valid @RequestBody InstituteRecommendPageRequestDto InstituteRecommendPageRequestDto)
			throws Exception;

	@GetMapping("/recommend/institute")
	public ResponseEntity<?> getRecommendationOfInstitutePage(@RequestParam(name = "status") String status) throws Exception;

	
	@PutMapping("/recommend/institute/update-status/{instituteRecommendationRequestId}") 
	public ResponseEntity<?> updateInstitueRecommendationStatus(@PathVariable final String instituteRecommendationRequestId, @RequestParam(name = "status") String status) throws Exception;
	 
}
