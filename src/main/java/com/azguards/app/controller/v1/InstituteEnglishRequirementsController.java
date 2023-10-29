package com.azguards.app.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteEnglishRequirementsDto;
import com.azguards.app.endpoint.InstituteEnglishRequirementsInterface;
import com.azguards.app.processor.InstituteEnglishRequirementsProcessor;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
public class InstituteEnglishRequirementsController implements InstituteEnglishRequirementsInterface {
	
	@Autowired
	private InstituteEnglishRequirementsProcessor instituteEnglishRequirementsProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> addInstituteEnglishRequirements(String userId, String instituteId,
		 @RequestBody @Valid InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception {
		instituteEnglishRequirementsProcessor.addInstituteEnglishRequirements(userId, instituteId, instituteEnglishRequirementsDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("english_requirement.added")).create();
	}

	@Override
	public ResponseEntity<?> updateInstituteEnglishRequirements(String userId, String englishRequirementsId,
			@RequestBody @Valid InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception {
		instituteEnglishRequirementsProcessor.updateInstituteEnglishRequirements(userId, englishRequirementsId, instituteEnglishRequirementsDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("english_requirement.updated")).create();
	}

	@Override
	public ResponseEntity<?> getInstituteEnglishRequirementsByInstiuteId(String userId, String instituteId)
			throws Exception {
		List<InstituteEnglishRequirementsDto> listOfInstituteEnglishRequirementDto = instituteEnglishRequirementsProcessor.getListOfInstituteEnglishRequirements(userId, instituteId, "PRIVATE");
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(listOfInstituteEnglishRequirementDto).setMessage(messageTranslator.toLocale("english_requirement.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getInstitutePublicEnglishRequirementsByInstituteId(String instituteId) throws Exception {
		List<InstituteEnglishRequirementsDto> listOfInstituteEnglishRequirementDto = instituteEnglishRequirementsProcessor.getListOfInstituteEnglishRequirements(null, instituteId, "PUBLIC");
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(listOfInstituteEnglishRequirementDto).setMessage(messageTranslator.toLocale("english_requirement.public.added")).create();
	}

	@Override
	public ResponseEntity<?> deleteInstituteEnglishRequirementsByRequirementsId(String userId,
			String englishRequirementsId) {
		instituteEnglishRequirementsProcessor.deleteInstituteEnglishRequirements(userId, englishRequirementsId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("english_requirement.deleted")).create();
	}
}
