package com.azguards.app.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.TimingRequestDto;
import com.azguards.app.endpoint.TimingInterface;
import com.azguards.app.processor.TimingProcessor;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.ValidationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TimingController implements TimingInterface {

	@Autowired
	private TimingProcessor timingProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveOrUpdate(String userId, @Valid TimingRequestDto timingRequestDto)
			throws ValidationException, NotFoundException {
		log.info("inside TimingController.saveOrUpdate");
		if (StringUtils.isEmpty(timingRequestDto.getEntityId())) {
			log.error("entity_id must not be null or empty");
		}
		ValidationUtil.validatEntityType(timingRequestDto.getEntityType());
		com.azguards.app.util.ValidationUtil.validatTimingType(timingRequestDto.getTimingType());
		timingProcessor.saveUpdateTiming(userId, timingRequestDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("timing.added"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> deleteByTimingId(String userId, String entityType, String entityId, String timingId)
			throws ValidationException, NotFoundException, ForbiddenException {
		log.info("inside TimingController.deleteByTimingId");
		ValidationUtil.validatEntityType(entityType);
		timingProcessor.deleteTiming(userId, EntityTypeEnum.valueOf(entityType), entityId, timingId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("timing.deleted")).setStatus(HttpStatus.OK)
				.create();
	}
}
