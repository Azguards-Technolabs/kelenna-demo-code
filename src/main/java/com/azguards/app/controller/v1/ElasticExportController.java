package com.azguards.app.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.ElasticExportInterface;
import com.azguards.app.processor.CourseProcessor;
import com.azguards.app.processor.FacultyProcessor;
import com.azguards.app.processor.InstituteProcessor;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("elasticExportControllerV1")
public class ElasticExportController implements ElasticExportInterface {

	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticExportController.class);
	
	@Autowired
	private InstituteProcessor instituteProcessor;

	@Autowired
	private CourseProcessor courseProcessor;
	
	@Autowired
	private FacultyProcessor facultyProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public ResponseEntity<Object> exportInstitutes() throws InvokeException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		LOGGER.debug("exportInstitutes() start");
		LOGGER.info("Going to save institutes on elasticSearch");
		instituteProcessor.exportInstituteToElastic();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("institute.export.success")).create();
	}

	@Override
	public ResponseEntity<Object> exportCourses() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		LOGGER.debug("exportCourses() start");
		LOGGER.info("Going to save courses on elasticSearch");
		courseProcessor.exportCourseToElastic();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("course.export.success")).create();
	}

	@Override
	public ResponseEntity<Object> exportFaculties() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		LOGGER.debug("exportCourses() start");
		LOGGER.info("Going to save faculties on elasticSearch");
		facultyProcessor.exportFacultyToElastic();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faculty.export.success")).create();
	}
}
