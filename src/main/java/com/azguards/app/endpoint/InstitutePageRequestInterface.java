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

import com.azguards.app.dto.InstitutePageRequestDto;


@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstitutePageRequestInterface {

	@PostMapping("/request/page/{instituteId}")
	public ResponseEntity<?> requestInstitutePageAccess(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId, @Valid @RequestBody InstitutePageRequestDto institutePageRequestDto)
			throws Exception;

	@GetMapping("/request/page/{instituteId}")
	public ResponseEntity<?> getRequestsOfInstitutePageAccess(@PathVariable final String instituteId, @RequestParam(name = "status") String status) throws Exception;

	
	@PutMapping("/request/page/update-status/{institutePageRequestId}") 
	public ResponseEntity<?> updateInstituetPageRequestStatus(@PathVariable final String institutePageRequestId, @RequestParam(name = "status") String status) throws Exception;
	 
}
