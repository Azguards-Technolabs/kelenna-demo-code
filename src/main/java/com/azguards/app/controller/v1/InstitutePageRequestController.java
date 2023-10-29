package com.azguards.app.controller.v1;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.constant.PageRequestStatus;
import com.azguards.app.dto.InstitutePageRequestDto;
import com.azguards.app.endpoint.InstitutePageRequestInterface;
import com.azguards.app.processor.InstitutePageRequestProcessor;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
public class InstitutePageRequestController implements InstitutePageRequestInterface {

	@Autowired
	private InstitutePageRequestProcessor institutePageRequestProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> requestInstitutePageAccess(String userId, String instituteId,
		 @RequestBody	@Valid InstitutePageRequestDto institutePageRequestDto) throws Exception {
		InstitutePageRequestDto institutePageRequestDtoResponse = institutePageRequestProcessor.requestInstiutePageRequest(userId, instituteId, institutePageRequestDto);
		return new GenericResponseHandlers.Builder().setData(institutePageRequestDtoResponse).setMessage("Institute page request successfully")
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getRequestsOfInstitutePageAccess(String instituteId, String status) throws Exception {

		boolean isStatusValid = EnumUtils.isValidEnum(PageRequestStatus.class, status);
		if (!isStatusValid) {
			log.error(messageTranslator.toLocale("page-request.status.invalid" , Utils.getEnumNamesAsString(PageRequestStatus.class),Locale.US));
			throw new ValidationException(messageTranslator.toLocale("page-request.status.invalid" , Utils.getEnumNamesAsString(PageRequestStatus.class)));
		}
		List<InstitutePageRequestDto> listOfInstitutePageRequestDto = institutePageRequestProcessor.getInstitutePageRequestByInstituteIdAndStatus(instituteId, status);
		return new GenericResponseHandlers.Builder().setData(listOfInstitutePageRequestDto).setMessage("Institute page request list fetched successfully")
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> updateInstituetPageRequestStatus(String institutePageRequestId, String status)
			throws Exception {
		boolean isStatusValid = EnumUtils.isValidEnum(PageRequestStatus.class, status);
		if (!isStatusValid) {
			log.error(messageTranslator.toLocale("page-request.status.invalid" , Utils.getEnumNamesAsString(PageRequestStatus.class),Locale.US));
			throw new ValidationException(messageTranslator.toLocale("page-request.status.invalid" , Utils.getEnumNamesAsString(PageRequestStatus.class)));
		}
		institutePageRequestProcessor.updateInstitutePageRequestStatus(institutePageRequestId, status);
		return new GenericResponseHandlers.Builder().setMessage("Institute page request updated successfully")
				.setStatus(HttpStatus.OK).create();
	}
}
