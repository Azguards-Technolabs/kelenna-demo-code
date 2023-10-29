package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.app.dto.InstituteFacilityDto;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public interface InstituteFacilitiesInterface {

	@PostMapping("/institute/facilities/{instituteId}")
	public ResponseEntity<?> addInstituteFacilities(@PathVariable final String instituteId,
			@RequestBody @Valid InstituteFacilityDto instituteFacilityDto) throws NotFoundException;

	@DeleteMapping("/institute/facilities/{instituteId}")
	public ResponseEntity<?> deleteInstituteFacilitiesById(@PathVariable final String instituteId,
			@RequestParam(value = "institute_facility_id", required = false) List<String> institutefacilitiesId);

	@GetMapping("/institute/facilities/{instituteId}")
	public ResponseEntity<?> getInstituteFacilities(@PathVariable final String instituteId);

	@GetMapping("/institute/public/facilities/{instituteId}")
	public ResponseEntity<?> getInstitutePublicFacilities(@PathVariable final String instituteId);

}
