package com.azguards.app.controller.v1;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.InstituteServiceDto;
import com.azguards.app.endpoint.InstituteServiceInterface;
import com.azguards.app.processor.InstituteServiceProcessor;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class InstituteServiceController implements InstituteServiceInterface {

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private InstituteServiceProcessor instituteServiceProcessor;

	@Override
	public ResponseEntity<?> addInstituteService(String userId, String instituteId,
			List<InstituteServiceDto> instituteServiceDtos) throws Exception {
		log.info("Adding services for institute Id " + instituteId);
		if (CollectionUtils.isEmpty(instituteServiceDtos)) {
			log.error(messageTranslator.toLocale("institute.null.services",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute.null.services"));
		}
		boolean isInValidData = instituteServiceDtos.stream()
				.anyMatch(e -> ObjectUtils.isEmpty(e.getService()) || StringUtils.isEmpty(e.getService().getServiceId())
						&& StringUtils.isEmpty(e.getService().getServiceName()));
		if (isInValidData) {
			log.error(messageTranslator.toLocale("institute-service.request.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute-service.request.invalid"));
		}
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setData(instituteServiceProcessor.addInstituteService(userId, instituteId, instituteServiceDtos))
				.setMessage(messageTranslator.toLocale("institute_services.added")).create();
	}

	@Override
	public ResponseEntity<?> deleteById(final String userId, String instituteServiceId) throws NotFoundException, InvokeException {
		log.info("deleting instituteService Id: {}" + instituteServiceId);
		instituteServiceProcessor.deleteInstituteService(userId, instituteServiceId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("institute_services.deleted")).create();
	}

	@Override
	public ResponseEntity<?> getInstituteServices(String instituteId) {
		log.info("getting services for institute Id " + instituteId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setData(instituteServiceProcessor.getInstituteServices(instituteId))
				.setMessage(messageTranslator.toLocale("institute_services.retrieved")).create();
	}

}
