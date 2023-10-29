package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CoursePrerequisite;
import com.azguards.app.bean.CoursePrerequisiteSubjects;

public interface CoursePrerequisiteDao {

	public List<CoursePrerequisite> getCoursePrerequisite(String courseId);
	
	public List<CoursePrerequisiteSubjects> getCoursePrerequisiteSubjects(String coursePrerequisiteId);
}
