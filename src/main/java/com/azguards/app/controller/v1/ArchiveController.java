package com.azguards.app.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.constant.ArchiveEntityType;
import com.azguards.app.processor.HelpProcessor;
import com.azguards.app.service.IErrorReportService;
import com.yuzee.common.lib.exception.ValidationException;
import com.yuzee.common.lib.handler.GenericResponseHandlers;
import com.yuzee.local.config.MessageTranslator;

@RestController("archiveControllerV1")
@RequestMapping("/api/v1/archive")
public class ArchiveController {

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private HelpProcessor helpProcessor;

	@Autowired
	private IErrorReportService iErrorReportService;

	@PostMapping("/entityType/{entityType}/entityId/{entityId}")
	public ResponseEntity<Object> addArchive(@PathVariable final String entityType, @PathVariable final String entityId,
			@RequestParam final boolean isArchive) throws ValidationException {
		if (entityType.equals(ArchiveEntityType.ENROLLMENT.name())) {
			// TODO call enrollment API to make it archive
			// iEnrollmentService.archiveEnrollment(entityId, isArchive);
		} else if (entityType.equals(ArchiveEntityType.ERROR_REPORT.name())) {
			iErrorReportService.archiveErrorReport(entityId, isArchive);
		} else if (entityType.equals(ArchiveEntityType.HELP_SUPPORT.name())) {
			helpProcessor.archiveHelpSupport(entityId, isArchive);
		}
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("archive.added")).setStatus(HttpStatus.OK).create();

	}

}
