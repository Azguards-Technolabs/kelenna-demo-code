package com.azguards.app.controller.v1;

import java.text.ParseException;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.endpoint.UploadInterface;
import com.azguards.app.processor.CourseProcessor;
import com.azguards.app.processor.EducationSystemProcessor;
import com.azguards.app.processor.FacultyProcessor;
import com.azguards.app.processor.GlobalStudentProcessor;
import com.azguards.app.processor.GradeDetailProcessor;
import com.azguards.app.processor.InstituteProcessor;
import com.azguards.app.processor.InstituteTypeProcessor;
import com.azguards.app.processor.LevelProcessor;
import com.azguards.app.processor.ScholarshipProcessor;
import com.azguards.app.processor.ServiceProcessor;
import com.azguards.app.processor.SubjectProcessor;
import com.azguards.common.lib.exception.IOException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("uploadControllerV1")
@Slf4j
public class UploadController implements UploadInterface {

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private InstituteProcessor instituteProcessor;

	@Autowired
	private ScholarshipProcessor scholarshipProcessor;

	@Autowired
	private SubjectProcessor subjectProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Autowired
	private GlobalStudentProcessor globalStudentProcessor;

	@Autowired
	private InstituteTypeProcessor instituteTypeProcessor;

	@Autowired
	private ServiceProcessor serviceProcessor;

	@Autowired
	private FacultyProcessor facultyProcessor;

	@Autowired
	private EducationSystemProcessor educationSystemProcessor;
	
	@Autowired
	private GradeDetailProcessor gradeDetailProcessor;
	
	@Autowired
	private LevelProcessor levelProcessor;

	@Override
	public ResponseEntity<Object> uploadInstituteType(final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, java.io.IOException, ParseException {
		log.info("Started process to Upload Activity");
		instituteTypeProcessor.importInstituteType(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.institute_type.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadServices(final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, java.io.IOException {
		log.info("Started process to Upload Service");
		serviceProcessor.importServices(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.services.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadCourse(final MultipartFile multipartFile)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException {
		log.info("Started process to Upload Course");
		courseProcessor.uploadCourseData(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.courses.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadCourseKeyword(final MultipartFile multipartFile) {
		log.info("Started process to Upload CourseKeyword");
		courseProcessor.importCourseKeyword(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.course_keywords.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadInstitute(final MultipartFile multipartFile) throws 
			ParseException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, java.io.IOException {
		log.info("Started process to Upload Institute");
		instituteProcessor.importInstitute(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.institute.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadScholarship(final MultipartFile multipartFile)
			throws java.io.IOException, ParseException {
		log.info("Started process to Upload Scholarship");
//		scholarshipProcessor.importScholarship(multipartFile);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("upload.scholarship.uploader")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadGrade(final MultipartFile multipartFile) {
		log.info("Started process to Upload Grade");
		educationSystemProcessor.importEducationSystem(multipartFile);
		gradeDetailProcessor.uploadGrade(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.grades.uploader")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<Object> uploadSubject(final MultipartFile multipartFile)
			throws ParseException, java.io.IOException {
		log.info("Started process to Upload Subject");
		subjectProcessor.importSubject(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.subjects.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadGlobalStudentData(MultipartFile multipartFile) throws java.io.IOException {
		log.info("Started process to Upload Global Student");
		globalStudentProcessor.uploadGlobalStudentData(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.students.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> uploadLevel(MultipartFile multipartFile)
			throws java.io.IOException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.info("Started process to Upload Level");
		levelProcessor.importLevel(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.level.uploader")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<Object> uploadFaculty(MultipartFile multipartFile)
			throws java.io.IOException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.info("Started process to Upload Faculty");
		facultyProcessor.importFaculty(multipartFile);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("upload.faculty.uploader"))
				.setStatus(HttpStatus.OK).create();
	}

}
