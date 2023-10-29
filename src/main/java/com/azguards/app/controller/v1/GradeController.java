package com.azguards.app.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.GradeInterface;
import com.azguards.app.processor.EducationSystemProcessor;
import com.azguards.common.lib.dto.institute.GradeDto;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("gradeControllerV1")
public class GradeController implements GradeInterface {

	@Autowired
	private EducationSystemProcessor educationSystemProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> calculate(final GradeDto gradeDto) throws Exception {
		Double averageGPA = educationSystemProcessor.calculateGrade(gradeDto);
		return new GenericResponseHandlers.Builder().setData(averageGPA)
				.setMessage(messageTranslator.toLocale("grade.average.calculated")).setStatus(HttpStatus.OK).create();
	}
}
