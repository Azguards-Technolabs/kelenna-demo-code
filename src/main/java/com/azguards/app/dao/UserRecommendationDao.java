package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.Course;

public interface UserRecommendationDao {

	List<Course> getRecommendCourse(String facultyId, String instituteId, String countryId, String cityId, Double price, Double variablePrice,
			int pageSize, List<String> courseIds);

	List<Course> getRelatedCourse(String facultyId, String instituteId, String countryId, String cityId, Double price, Double variablePrice,
			int pageSize, List<String> courseIds, String courseName);

	List<Course> getCourseNoResultRecommendation(String facultyId, String countryId, List<String> courseIds, Integer startIndex, Integer pageSize);

	List<Course> getCheapestCourse(String facultyId, String countryId, String levelId, String cityId, List<String> courseIds,
			Integer startIndex, Integer pageSize);

}
