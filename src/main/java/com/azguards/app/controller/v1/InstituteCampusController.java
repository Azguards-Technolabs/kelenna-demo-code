package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteCampusDto;
import com.azguards.app.endpoint.InstituteCampusInterface;
import com.azguards.app.processor.InstituteCampusProcessor;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class InstituteCampusController implements InstituteCampusInterface {

	@Autowired
	private InstituteCampusProcessor instituteCampusProcessor;

	@Autowired
	private MessageTranslator messageTranslator;

	@Override
	public ResponseEntity<?> addCampus(String userId, String instituteId, @NotEmpty List<String> instituteIds)
			throws NotFoundException, ValidationException {
		log.info("inside InstituteCampusController.addCampus");
		instituteCampusProcessor.addCampus(userId, instituteId, instituteIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.campus.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteCampuses(String userId, String instituteId) throws NotFoundException {
		log.info("inside InstituteCampusController.getInstituteCampuses");
		List<InstituteCampusDto> instituteCampuses = instituteCampusProcessor.getInstituteCampuses(userId, instituteId);
		return new GenericResponseHandlers.Builder().setData(instituteCampuses)
				.setMessage(messageTranslator.toLocale("institute.campus.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<?> removeCampus(String userId, String instituteId, @NotEmpty List<String> instituteIds)
			throws NotFoundException {
		log.info("inside InstituteCampusController.removeCampus");
		instituteCampusProcessor.removeCampuses(userId, instituteId, instituteIds);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.campus.removed"))
				.setStatus(HttpStatus.OK).create();
	}

}
