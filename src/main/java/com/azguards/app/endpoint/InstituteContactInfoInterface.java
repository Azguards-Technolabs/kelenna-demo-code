package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.app.dto.InstituteContactInfoDto;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteContactInfoInterface {

	@PostMapping("/contact/info/{instituteId}")
	public ResponseEntity<?> addUpdateInstituteContactInfo (@RequestHeader("userId") final String userId,@PathVariable final String instituteId, @RequestBody @Valid InstituteContactInfoDto instituteContactInfoDto) throws Exception;

	@GetMapping("/contact/info/{instituteId}")
	public ResponseEntity<?> getInstituteContactInfo (@RequestHeader("userId") final String userId,@PathVariable final String instituteId) throws Exception;
	
	@GetMapping("/public/contact/info/{instituteId}")
	public ResponseEntity<?> getInstitutePublicContactInfo (@PathVariable final String instituteId) throws Exception; 

}
