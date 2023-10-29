package com.azguards.app.dao;

import com.azguards.app.bean.CourseScholarship;
import com.azguards.common.lib.exception.ValidationException;

public interface CourseScholarshipDao {

	CourseScholarship save(CourseScholarship courseScholarships) throws ValidationException;

	CourseScholarship findByCourseId(String courseId);
}
