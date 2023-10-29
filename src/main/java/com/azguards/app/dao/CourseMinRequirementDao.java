package com.azguards.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.CourseMinRequirement;
import com.azguards.common.lib.exception.ValidationException;

public interface CourseMinRequirementDao {

	CourseMinRequirement save(final CourseMinRequirement courseMinRequirement) throws ValidationException;

	Page<CourseMinRequirement> findByCourseId(final String courseId, Pageable pageable);

	CourseMinRequirement findByCourseIdAndId(String courseId, String id);

	long deleteByCourseIdAndId(String courseId, String id);
}
