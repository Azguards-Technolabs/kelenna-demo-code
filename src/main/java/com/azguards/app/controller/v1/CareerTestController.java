package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.CareerJobDto;
import com.azguards.app.endpoint.CareerTestInterface;
import com.azguards.app.processor.CareerTestProcessor;
import com.yuzee.common.lib.dto.PaginationResponseDto;
import com.yuzee.common.lib.exception.NotFoundException;
import com.yuzee.common.lib.exception.ValidationException;
import com.yuzee.common.lib.handler.GenericResponseHandlers;
import com.yuzee.common.lib.util.PaginationUtil;
import com.yuzee.local.config.MessageTranslator;

@RestController("careerTestControllerV1")
public class CareerTestController implements CareerTestInterface {

	@Autowired
	private CareerTestProcessor careerTestProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> getCareerJobSkills(String userId, Integer pageNumber, Integer pageSize, String levelId, String jobId) {
		PaginationResponseDto careerJobSkillDtos = careerTestProcessor.getCareerJobSkills(userId, pageNumber,
				pageSize, levelId, jobId);
		return new GenericResponseHandlers.Builder().setData(careerJobSkillDtos).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.skills.list.retrieved")


).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobWorkingStyles(String userId, List<String> jobIds, Integer pageNumber, Integer pageSize) {
		PaginationResponseDto careerJobWorkingStyles = careerTestProcessor.getCareerJobWorkingStyles(userId, jobIds, pageNumber,
				pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobWorkingStyles).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.work_style.list.retrieved")


).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobSubjects(String userId, List<String> jobIds, Integer pageNumber, Integer pageSize) {
		PaginationResponseDto careerJobSubjects = careerTestProcessor.getCareerJobSubjects(userId, jobIds, pageNumber,
				pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobSubjects).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.subject.list.retrieved")


).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobTypes(String userId, List<String> jobIds, Integer pageNumber, Integer pageSize) {
		PaginationResponseDto careerJobTypes = careerTestProcessor.getCareerJobTypes(userId, jobIds, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobTypes).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.job_type.retrieved")


).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobs(String userId, List<String> jobIds, Integer pageNumber, Integer pageSize) {
		PaginationResponseDto careerJobs = careerTestProcessor.getCareerJobs(userId, jobIds, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobs).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.list.retrieved")


).create();
	}

	@Override
	public ResponseEntity<?> getRelatedCareers(List<String> careerIds, Integer pageNumber, Integer pageSize) {
		PaginationResponseDto careerJobs = careerTestProcessor.getRealtedCareers(careerIds, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobs).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.related.list.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getRelatedCourseBasedOnCareerTest(List<String> jobIds, Integer pageNumber,
			Integer pageSize) {
		PaginationResponseDto careerJobRelatedCourse = careerTestProcessor.getRelatedCourseBasedOnCareerTest(jobIds,
				pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobRelatedCourse).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.related.course.retrievedd")


).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobById(String jobId) throws NotFoundException {
		CareerJobDto careerJobDto = careerTestProcessor.getCareerJobById(jobId);
		return new GenericResponseHandlers.Builder().setData(careerJobDto).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.job.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getCareerJobsByName(String userId, String name, Integer pageNumber, Integer pageSize) throws ValidationException {
		PaginationUtil.validatePageNoAndPageSize(pageNumber, pageSize);
		PaginationResponseDto careerJobs = careerTestProcessor.getCareerJobsByName(userId, name, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(careerJobs).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("career_test.job.list.retrieved")).create();
	}
}
