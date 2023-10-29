package com.azguards.app.endpoint;

import java.text.ParseException;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.common.lib.exception.IOException;

@RequestMapping("/api/v1/upload")
public interface UploadInterface {

	@PostMapping("/service")
	public ResponseEntity<Object> uploadServices(@RequestParam("services") final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, java.io.IOException;

	@PostMapping("/institute/institute-type")
	public ResponseEntity<Object> uploadInstituteType(@RequestParam("institute_types") final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, java.io.IOException, ParseException;

	@PostMapping("/course")
	public ResponseEntity<Object> uploadCourse(@RequestParam("courses") final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException;

	@PostMapping("/course/keyword")
	public ResponseEntity<Object> uploadCourseKeyword(@RequestParam("keyword") final MultipartFile multipartFile);

	@PostMapping("/institute")
	public ResponseEntity<Object> uploadInstitute(@RequestParam("institutes") final MultipartFile multipartFile)
			throws ParseException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException,
			java.io.IOException;

	@PostMapping("/scholarship")
	public ResponseEntity<Object> uploadScholarship(@RequestParam("scholarship") final MultipartFile multipartFile)
			throws java.io.IOException, ParseException;

	@PostMapping("/grade")
	public ResponseEntity<Object> uploadGrade(@RequestParam("grade") final MultipartFile multipartFile);

	@PostMapping("/subjects")
	public ResponseEntity<Object> uploadSubject(@RequestParam("subjects") final MultipartFile multipartFile)
			throws ParseException, java.io.IOException;

	@PostMapping("/globalStudent")
	public ResponseEntity<Object> uploadGlobalStudentData(@RequestParam("students") MultipartFile multipartFile)
			throws java.io.IOException;

	@PostMapping("/level")
	public ResponseEntity<Object> uploadLevel(@RequestParam("level") MultipartFile multipartFile)
			throws java.io.IOException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException;

	@PostMapping("/faculty")
	public ResponseEntity<Object> uploadFaculty(@RequestParam("faculty") MultipartFile multipartFile)
			throws java.io.IOException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException;
}
