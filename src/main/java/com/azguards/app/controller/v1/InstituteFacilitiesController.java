package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteFacilityDto;
import com.azguards.app.endpoint.InstituteFacilitiesInterface;
import com.azguards.app.processor.InstituteFacilityProcessor;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class InstituteFacilitiesController implements InstituteFacilitiesInterface {

	@Autowired
	private InstituteFacilityProcessor instituteFacilityProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> addInstituteFacilities(String instituteId,
			@RequestBody @Valid InstituteFacilityDto instituteFacilityDto) throws NotFoundException {
		log.info("Adding facilities for institute Id {}", instituteId);

		instituteFacilityProcessor.addInstituteFacility(instituteId, instituteFacilityDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("institute_facilities.added")).create();
	}

	@Override
	public ResponseEntity<?> deleteInstituteFacilitiesById(String instituteId, List<String> institutefacilitiesId) {
		log.info("deleting facilities for institute Id {}", instituteId);
		instituteFacilityProcessor.deleteInstituteFacilities(instituteId, institutefacilitiesId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("institute_facilities.deleted")).create();
	}

	@Override
	public ResponseEntity<?> getInstituteFacilities(String instituteId) {
		log.info("getting facilities for institute Id {}", instituteId);
		InstituteFacilityDto instituteFacilityDto = instituteFacilityProcessor.getFacilitiesByInstituteId(instituteId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(instituteFacilityDto)
				.setMessage(messageTranslator.toLocale("institute_facilities.list.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getInstitutePublicFacilities(String instituteId) {
		log.info("getting facilities for institute Id {}", instituteId);
		InstituteFacilityDto instituteFacilityDto = instituteFacilityProcessor
				.getPublicServiceByInstituteId(instituteId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(instituteFacilityDto)
				.setMessage(messageTranslator.toLocale("institute_facilities.public.list.retrieved")).create();
	}
}
