package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.AccrediatedDetailDto;
import com.azguards.app.endpoint.AccrediatedDetailInterface;
import com.azguards.app.processor.AccrediatedDetailProcessor;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.ValidationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@RestController("accrediationDetailV1")
@CommonsLog
public class AccrediationDetailController implements AccrediatedDetailInterface {

	@Autowired
	private AccrediatedDetailProcessor accrediatedDetailProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> addAccrediationDetail(AccrediatedDetailDto accrediatedDetailDto) throws ValidationException {
		log.info("start adding accrediation for entityId "+accrediatedDetailDto.getEntityId());
		ValidationUtil.validatEntityType(accrediatedDetailDto.getEntityType());
		AccrediatedDetailDto detailDto = accrediatedDetailProcessor.addAccrediatedDetail(accrediatedDetailDto);
		return new GenericResponseHandlers.Builder().setData(detailDto).setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.added")).create();
	}

	@Override
	public ResponseEntity<?> getAccrediationDetailByEntityId(String entityId) throws NotFoundException {
		log.info("start getting accrediation for entityId "+entityId);
		List<AccrediatedDetailDto> accrediatedDetailDtos = accrediatedDetailProcessor.getAccrediationDetailByEntityId(entityId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(accrediatedDetailDtos)
				.setMessage(messageTranslator.toLocale("accrediation.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> deleteAccrediationDetailByEntityId(String entityId) throws NotFoundException, InvokeException {
		log.info("start deleting accrediation for entityId "+entityId);
		accrediatedDetailProcessor.deleteAccrediationDetailByEntityId(entityId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.deleted")).create();
	}

	@Override
	public ResponseEntity<?> updateAccrediationDetail(String id, AccrediatedDetailDto accrediatedDetailDto) throws NotFoundException, ValidationException {
		log.info("start updating accrediation for entityId "+id);
		ValidationUtil.validatEntityType(accrediatedDetailDto.getEntityType());
		AccrediatedDetailDto detailDto = accrediatedDetailProcessor.updateAccrediatedDetails(id, accrediatedDetailDto);
		return new GenericResponseHandlers.Builder().setData(detailDto).setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.updated")).create();
	}

	@Override
	public ResponseEntity<?> getAccrediationDetailById(String id) throws NotFoundException, InvokeException {
		log.info("start getting accrediation details for id "+id);
		AccrediatedDetailDto detailDto = accrediatedDetailProcessor.getAccrediatedById(id);
		return new GenericResponseHandlers.Builder().setData(detailDto).setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> deleteAccrediationDetailById(String id) throws NotFoundException, InvokeException {
		log.info("start deleting accrediation details for id "+id);
		accrediatedDetailProcessor.deleteAccrediationDetailById(id);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.deleted")).create();
	}

	@Override
	public ResponseEntity<?> getAllAccrediationDetails() {
		log.info("start getting all accrediation details from DB");
		List<AccrediatedDetailDto> accrediatedDetailDtos = accrediatedDetailProcessor.getAllAccrediationDetails();
		return new GenericResponseHandlers.Builder().setData(accrediatedDetailDtos).setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("accrediation.retrieved")).create();
	}
}
