package com.azguards.app.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.SubjectInterface;
import com.azguards.app.processor.SubjectProcessor;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("subjectControllerV1")
public class SubjectController implements SubjectInterface {

	@Autowired
	private SubjectProcessor subjectProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> getAllSubjects(final Integer pageNumber, final Integer pageSize, String name,
			String educationSystemId) throws ValidationException, NotFoundException, InvokeException {
		log.debug("inside getAllSubjects(final Integer pageNumber, final Integer pageSize) method");
		PaginationUtil.validatePageNoAndPageSize(pageNumber, pageSize);
		return new GenericResponseHandlers.Builder()
				.setData(subjectProcessor.getAllSubjects(pageNumber, pageSize, name, educationSystemId))
				.setMessage(messageTranslator.toLocale("subjects.list.retrieved")).setStatus(HttpStatus.OK).create();
	}
}
