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

import com.azguards.app.dto.InstituteAssociationDto;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteAssociationInterface {

	@PostMapping("/institute/association")
	public ResponseEntity<?> addInstituteAssociation (@RequestHeader("userId") final String userId,@RequestBody @Valid InstituteAssociationDto instituteAssociationDto) throws Exception;
	
	@GetMapping("/institute/association/{instituteId}")
	public ResponseEntity<?> getInstituteAssociationByAssociationTypeAndStatus(@RequestHeader("userId") final String userId,@PathVariable final String instituteId , @RequestParam(name = "association_type") String associationType , @RequestParam(name = "status") String status ) throws Exception;
	
	// public api to fetch association of institute
	@GetMapping("/public/institute/association/{instituteId}")
	public ResponseEntity<?> getPublicInstituteAssociationByAssociationType(@PathVariable final String instituteId , @RequestParam(name = "association_type") String associationType ) throws Exception;
	
	@PutMapping("/institute/association/{instituteId}")
	public ResponseEntity<?> updateInstituteAssociation(@RequestHeader("userId") final String userId, @PathVariable final String instituteId ,@RequestParam(name = "institute_association_id") String instituteAssociationId, @RequestParam(name = "status") String status ) throws Exception;

}
