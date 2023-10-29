package com.azguards.app.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteContactInfoDto;
import com.azguards.app.endpoint.InstituteContactInfoInterface;
import com.azguards.app.processor.InstituteContactInfoProcessor;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
public class InstituteContactInfoController implements InstituteContactInfoInterface {
	
	@Autowired
	private InstituteContactInfoProcessor instituteContactInfoProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> addUpdateInstituteContactInfo(String userId, String instituteId,
		@RequestBody @Valid InstituteContactInfoDto instituteContactInfoDto) throws Exception {
		instituteContactInfoProcessor.addUpdateInstituteContactInfo(userId, instituteId, instituteContactInfoDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute_contact_info.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteContactInfo(String userId, String instituteId) throws Exception {
		InstituteContactInfoDto instituteContactInfoDto =instituteContactInfoProcessor.getInstituteContactInfo(userId, instituteId, "PRIVATE");
		return new GenericResponseHandlers.Builder().setData(instituteContactInfoDto).setMessage(messageTranslator.toLocale("institute_contact_info.added"))
				.setStatus(HttpStatus.OK).create();
	}

	// sending null in as user id as dont want to resure same method
	@Override
	public ResponseEntity<?> getInstitutePublicContactInfo(String instituteId) throws Exception {
		InstituteContactInfoDto instituteContactInfoDto =instituteContactInfoProcessor.getInstituteContactInfo(null, instituteId, "PUBLIC");
		return new GenericResponseHandlers.Builder().setData(instituteContactInfoDto).setMessage(messageTranslator.toLocale("institute_contact_info.added"))
				.setStatus(HttpStatus.OK).create();
	}

}
