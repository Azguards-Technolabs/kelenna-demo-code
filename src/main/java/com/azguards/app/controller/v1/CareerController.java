package com.azguards.app.controller.v1;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.CareerInterface;
import com.azguards.app.processor.CareerProcessor;
import com.yuzee.common.lib.handler.GenericResponseHandlers;
import com.yuzee.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("careerControllerV1")
@Slf4j
public class CareerController implements CareerInterface {
	@Autowired
	private MessageTranslator messageTranslator;
	@Autowired
	private CareerProcessor careerProcessor;

	@Override
	public ResponseEntity<?> findByName(@NotNull String name, Integer pageNumber, Integer pageSize) {
		log.info("inside CareerController.findByName");
		return new GenericResponseHandlers.Builder()
				.setData(careerProcessor.findCareerByName(name, pageNumber, pageSize)).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("careers.retrieved")).create();
	}
}
