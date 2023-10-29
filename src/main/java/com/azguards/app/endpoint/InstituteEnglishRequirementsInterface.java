package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.app.dto.InstituteEnglishRequirementsDto;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteEnglishRequirementsInterface {

	@PostMapping("/englishrequirements/{instituteId}")
	public ResponseEntity<?> addInstituteEnglishRequirements(@RequestHeader("userId") final String userId,@PathVariable final String instituteId,@Valid @RequestBody InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception;

	@PutMapping("/englishrequirements/{englishRequirementsId}")
	public ResponseEntity<?> updateInstituteEnglishRequirements(@RequestHeader("userId") final String userId,@PathVariable final String englishRequirementsId,@Valid @RequestBody InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception;
	
	@GetMapping("/englishrequirements/{instituteId}")
	public ResponseEntity<?> getInstituteEnglishRequirementsByInstiuteId (@RequestHeader("userId") final String userId,@PathVariable final String instituteId) throws Exception;
	
	@GetMapping("/public/englishrequirements/{instituteId}")
	public ResponseEntity<?> getInstitutePublicEnglishRequirementsByInstituteId (@PathVariable final String instituteId) throws Exception;
	
	@DeleteMapping("/englishrequirements/{englishRequirementsId}")
	public ResponseEntity<?> deleteInstituteEnglishRequirementsByRequirementsId (@RequestHeader("userId") final String userId,@PathVariable final String englishRequirementsId);
	
}
