package com.azguards.app.exception;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.azguards.common.lib.exception.BaseException;
import com.azguards.common.lib.exception.BaseRuntimeException;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice(basePackages = "com.azguards.app")
@Slf4j
public class ErrorHandlingController {
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	/**
	 * Central exception handler and generate common custom response
	 *
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	ResponseEntity<Object> handleControllerException(final HttpServletRequest request, final Throwable exception) {
		HttpStatus status = null;
		String message = null;
		if (exception instanceof ParseException) {
			status = ((BaseException) exception).getStatus();
			message = exception.getMessage();
		}else if (exception instanceof DataIntegrityViolationException) {
			status = HttpStatus.BAD_REQUEST;
			message = exception.getMessage();
			DataIntegrityViolationException ex = (DataIntegrityViolationException)exception;
			if (ex.getCause() instanceof ConstraintViolationException) {
				String constraintName = ((ConstraintViolationException) ex.getCause()).getConstraintName();
				if (!ObjectUtils.isEmpty(constraintName)) {
					String msg =  messageTranslator.toLocale(constraintName);
					if (!ObjectUtils.isEmpty(msg)) {
						message = msg;	
					}
				}
			}
		} else if (exception instanceof ForbiddenException) {
			status = ((ForbiddenException) exception).getStatus();
			message = exception.getMessage();
		} else if (exception instanceof ValidationException) {
			status = HttpStatus.BAD_REQUEST;
			message = exception.getMessage();
		} else if (exception instanceof MethodArgumentNotValidException) {
			status = HttpStatus.BAD_REQUEST;
			List<String> errors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()
					.stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
			message = errors.toString();
		} else if (exception instanceof BaseException) {
			status = ((BaseException) exception).getStatus();
			message = exception.getMessage();
		} else if (exception instanceof BaseRuntimeException) {
			status = ((BaseRuntimeException) exception).getStatus();
			message = exception.getMessage();
		} else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = exception.getMessage();
		}
		StringBuffer requestedURL = request.getRequestURL();
		log.info("Requested URL:{}", requestedURL);
		log.error("exception : {}", exception);
		return new GenericResponseHandlers.Builder().setStatus(status).setMessage(message).create();
	}

}
