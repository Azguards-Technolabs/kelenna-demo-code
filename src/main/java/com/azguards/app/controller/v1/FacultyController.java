package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.FacultyEndpoint;
import com.azguards.app.processor.FacultyProcessor;
import com.azguards.common.lib.dto.institute.FacultyDto;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("facultyControllerV1")
public class FacultyController implements FacultyEndpoint {

	@Autowired
	private FacultyProcessor facultyProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> saveFaculty(FacultyDto faculty) throws Exception {
		facultyProcessor.saveFaculty(faculty);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faculty.added"))
				.create();
	}

	@Override
	public ResponseEntity<?> getFacultyById(String facultyId) throws Exception {
		FacultyDto faculty = facultyProcessor.getFacultyById(facultyId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faculty.retrieved"))
				.setData(faculty).create();
	}

	@Override
	public ResponseEntity<?> getFacultyByFacultyName(String facultyName) throws Exception {
		FacultyDto faculty = facultyProcessor.getFacultyByFacultyName(facultyName);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faculty.retrieved"))
				.setData(faculty).create();
	}
	
	@Override
	public ResponseEntity<?> getAll() throws Exception {
		List<FacultyDto> facultyList = facultyProcessor.getAllFaculties();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faculty.retrieved")).setData(facultyList).create();
	}
}
