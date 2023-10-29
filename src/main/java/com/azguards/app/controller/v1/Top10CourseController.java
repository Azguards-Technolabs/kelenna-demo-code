package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.service.ITop10CourseService;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("top10CourseControllerV1")
@RequestMapping("/api/v1/top10")
public class Top10CourseController {

	@Autowired
	private ITop10CourseService iTop10CourseService;
	@Autowired
	private MessageTranslator messageTranslator;
	@GetMapping("/random")
	public ResponseEntity<?> getTop10RandomCoursesForGlobalSearchLandingPage(@RequestHeader final String userId) throws ValidationException, NotFoundException, InvokeException {

		List<CourseResponseDto> courseResponseDto = iTop10CourseService.getTop10RandomCoursesForGlobalSearchLandingPage();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("randomlist.retrieved")).setData(courseResponseDto).create();
	}
}
