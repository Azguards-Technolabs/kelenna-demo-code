package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CourseEnglishEligibility;
import com.azguards.app.dao.CourseEnglishEligibilityDao;
import com.azguards.app.repository.CourseEnglishEligibilityRepository;

@Component
public class CourseEnglishEligibilityDaoImpl implements CourseEnglishEligibilityDao {

	@Autowired
	private CourseEnglishEligibilityRepository courseEnglishEligibilityRepository;

	@Override
	public List<CourseEnglishEligibility> getAllEnglishEligibilityByCourse(final String courseID) {
		return courseEnglishEligibilityRepository.findByCourseId(courseID);
	}
}
