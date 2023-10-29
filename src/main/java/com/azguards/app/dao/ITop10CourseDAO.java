package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.Top10Course;

public interface ITop10CourseDAO {

	void save(final Top10Course top10Course);

	void deleteAll();

	List<String> getAllDistinctFaculty();

	List<Top10Course> getTop10CourseKeyword(String faculty);

	List<String> getCourseIdsOfTop10CoursesFromEveryFaculty();

	List<Course> getRandomCourseFromTop10Course(String countryName, List<String> levelList, List<String> top10CourseIds);
}
