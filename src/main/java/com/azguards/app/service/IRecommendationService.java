package com.azguards.app.service;

import java.util.List;
import java.util.Set;

import com.azguards.app.bean.Course;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.app.dto.MyHistoryDto;
import com.azguards.common.lib.dto.institute.ScholarshipDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

public interface IRecommendationService {

	List<InstituteResponseDto> getRecommendedInstitutes(String userId, /* Long startIndex, Long pageSize, Long pageNumber, */ String language)
			throws ValidationException, NotFoundException;

	void getOtherPeopleSearch();

	List<CourseResponseDto> getRecommendedCourses(String userId) throws ValidationException;

	List<Course> getTopSearchedCoursesForFaculty(String facultyId, String userId);

	Set<Course> displayRelatedCourseAsPerUserPastSearch(String userId) throws ValidationException;

	List<InstituteResponseDto> getinstitutesBasedOnOtherPeopleSearch(String userId);

	List<ScholarshipDto> getRecommendedScholarships(String userId, String language) throws ValidationException, NotFoundException;

	List<MyHistoryDto> getRecommendedMyHistory(String userId);
}
