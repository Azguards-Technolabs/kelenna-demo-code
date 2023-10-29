package com.azguards.app.controller.v1;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.CareerTestResultInterface;
import com.azguards.app.enumeration.CareerTestEntityType;
import com.azguards.app.processor.CareerTestResultProcessor;
import com.yuzee.common.lib.exception.ValidationException;
import com.yuzee.common.lib.handler.GenericResponseHandlers;
import com.yuzee.common.lib.util.Utils;
import com.yuzee.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("careerTestResultControllerV1")
@Slf4j
public class CareerTestResultController implements CareerTestResultInterface {

	@Autowired
	private CareerTestResultProcessor careerTestResultProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<?> saveOrUpdateResult(String userId, String entityType, List<String> entityIds) {
		log.info("inside CareerController.findByName");
		if (!EnumUtils.isValidEnum(CareerTestEntityType.class, entityType)) {
			log.error(messageTranslator.toLocale("test-result.entity.type"),Arrays.toString(Utils.getEnumNames(CareerTestEntityType.class)),Locale.US);
			throw new ValidationException(messageTranslator.toLocale("test-result.entity.type", Arrays.toString(Utils.getEnumNames(CareerTestEntityType.class))));
		}
		careerTestResultProcessor.saveOrUpdateResult(userId, entityType, entityIds);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.list.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> isTestCompleted(String userId) {
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("career_test.date.retrieved"))
				.setStatus(HttpStatus.OK).setData(careerTestResultProcessor.isTestCompleted(userId)).create();
	}
}
