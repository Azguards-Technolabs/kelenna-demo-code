package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CourseInstitute;
import com.azguards.common.lib.exception.ValidationException;

public interface CourseInstituteDao {
	List<CourseInstitute> saveAll(List<CourseInstitute> courseInstitutes) throws ValidationException;

	List<CourseInstitute> findLinkedInstitutes(String courseId);

	CourseInstitute findByDestinationCourseId(String destinationCourseId);

	void deleteAll(List<CourseInstitute> courseInstitutes);
}