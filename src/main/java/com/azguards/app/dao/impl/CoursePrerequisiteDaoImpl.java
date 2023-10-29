package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CoursePrerequisite;
import com.azguards.app.bean.CoursePrerequisiteSubjects;
import com.azguards.app.dao.CoursePrerequisiteDao;
import com.azguards.app.repository.CoursePrerequisiteRepository;
import com.azguards.app.repository.CoursePrerequisiteSubjectRepository;

@Component
public class CoursePrerequisiteDaoImpl implements CoursePrerequisiteDao {

	@Autowired
	private CoursePrerequisiteRepository coursePrerequisiteRepository;
	
	@Autowired
	private CoursePrerequisiteSubjectRepository coursePrerequisiteSubjectRepository;

	@Override
	public List<CoursePrerequisite> getCoursePrerequisite(String courseId) {
		return coursePrerequisiteRepository.findByCourseId(courseId);
	}

	@Override
	public List<CoursePrerequisiteSubjects> getCoursePrerequisiteSubjects(String coursePrerequisiteId) {
		return coursePrerequisiteSubjectRepository.findByCoursePrerequisiteId(coursePrerequisiteId);
	}
}
