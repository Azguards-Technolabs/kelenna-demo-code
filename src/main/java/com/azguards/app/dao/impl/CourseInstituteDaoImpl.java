package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseInstitute;
import com.azguards.app.dao.CourseInstituteDao;
import com.azguards.app.repository.CourseInstituteRepository;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CourseInstituteDaoImpl implements CourseInstituteDao {

	@Autowired
	private CourseInstituteRepository courseInstituteRepository;

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public List<CourseInstitute> saveAll(List<CourseInstitute> courseInstitutes) throws ValidationException {
		try {
			return courseInstituteRepository.saveAll(courseInstitutes);
		} catch (DataIntegrityViolationException e) {
			log.error("course-institute.already.exist",Locale.US);
			throw new ValidationException(messageTranslator.toLocale("course-institute.already.exist"));
		}
	}

	@Override
	public List<CourseInstitute> findLinkedInstitutes(String courseId) {
		return courseInstituteRepository.findLinkedInstitutes(courseId);
	}

	@Override
	public CourseInstitute findByDestinationCourseId(String destinationCourseId) {
		return courseInstituteRepository.findByDestinationCourseId(destinationCourseId);
	}

	@Override
	public void deleteAll(List<CourseInstitute> courseInstitutes) {
		courseInstituteRepository.deleteAll(courseInstitutes);
	}
}
