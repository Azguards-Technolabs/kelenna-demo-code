package com.azguards.app.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.InstituteBasicInfoInterface;
import com.azguards.app.processor.InstituteBasicInfoProcessor;
import com.azguards.common.lib.dto.institute.InstituteBasicInfoDto;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
public class InstituteBasicInfoController implements InstituteBasicInfoInterface {
	
	@Autowired
	private InstituteBasicInfoProcessor instituteBasicInfoProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> addUpdateInstituteBasicInfo(String userId, String instituteId,
		@RequestBody @Valid InstituteBasicInfoDto instituteBasicInfoDto) throws Exception {
		instituteBasicInfoProcessor.addUpdateInstituteBasicInfo(userId, instituteId, instituteBasicInfoDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute_basic_info.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteBasicInfo(String userId, String instituteId) throws Exception {
		InstituteBasicInfoDto instituteBasicInfoDto = instituteBasicInfoProcessor.getInstituteBasicInfo(userId, instituteId, "PRIVATE", true, false);
		return new GenericResponseHandlers.Builder().setData(instituteBasicInfoDto).setMessage(messageTranslator.toLocale("institute_basic_info.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	// passing null user id as dont want to duplicate same set of code 
	@Override
	public ResponseEntity<?> getInstitutePublicBasicInfo(String instituteId, boolean includeInstituteLogo, boolean includeDetail) throws Exception {
		InstituteBasicInfoDto instituteBasicInfoDto = instituteBasicInfoProcessor.getInstituteBasicInfo(null, instituteId, "PUBLIC",includeInstituteLogo,includeDetail);
		return new GenericResponseHandlers.Builder().setData(instituteBasicInfoDto).setMessage(messageTranslator.toLocale("institute_basic_info.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

}
