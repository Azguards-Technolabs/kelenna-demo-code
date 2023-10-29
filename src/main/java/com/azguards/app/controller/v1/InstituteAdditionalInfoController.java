package com.azguards.app.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteAdditionalInfoDto;
import com.azguards.app.endpoint.InstituteAdditionalInfoInterface;
import com.azguards.app.processor.InstituteAdditionalInfoProcessor;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController
public class InstituteAdditionalInfoController implements InstituteAdditionalInfoInterface {

	@Autowired
	private InstituteAdditionalInfoProcessor instituteAdditionalInfoProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> addInstituteAdditionalInfo(String userId, String instituteId,
			@Valid InstituteAdditionalInfoDto instituteAdditionalInfoDto) throws Exception {
		instituteAdditionalInfoProcessor.addUpdateInstituteAdditionalInfo(userId, instituteId,
				instituteAdditionalInfoDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute_info.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteAdditionalInfo(String instituteId) throws Exception {
		InstituteAdditionalInfoDto instituteAdditionalInfoDto = instituteAdditionalInfoProcessor
				.getInstituteAdditionalInfo(instituteId);
		return new GenericResponseHandlers.Builder().setData(instituteAdditionalInfoDto)
				.setMessage(messageTranslator.toLocale("institute_info.retrieved")).setStatus(HttpStatus.OK).create();
	}

}
