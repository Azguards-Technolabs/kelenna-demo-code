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

import com.azguards.app.dto.InstituteJoinRequestDto;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteJoinRequestInterface {

	@PostMapping("/request/join")
	public ResponseEntity<?> requestJoinInstitute(@RequestHeader("userId") final String userId,
			 @Valid @RequestBody InstituteJoinRequestDto instituteJoinRequestDto)
			throws Exception;

	@GetMapping("/request/join")
	public ResponseEntity<?> getRequestsOfInstituteJoinRequest(@RequestParam(name = "status") String status) throws Exception;

	
	@PutMapping("/request/join/update-status/{instituteJoinRequestId}") 
	public ResponseEntity<?> updateInstituetJoinRequestStatus(@PathVariable final String instituteJoinRequestId, @RequestParam(name = "status") String status) throws Exception;
	
}
