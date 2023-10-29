package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CourseEnglishEligibility;

public interface CourseEnglishEligibilityDao {
	List<CourseEnglishEligibility> getAllEnglishEligibilityByCourse(String courseID);
}
