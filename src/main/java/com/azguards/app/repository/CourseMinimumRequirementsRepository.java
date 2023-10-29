package com.azguards.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azguards.app.bean.CourseMinRequirement;

public interface CourseMinimumRequirementsRepository extends JpaRepository<CourseMinRequirement, String> {

	Page<CourseMinRequirement> findByCourseId(String courseId, Pageable pageable);

	CourseMinRequirement findByCourseIdAndId(String courseId, String id);

	long deleteByCourseIdAndId(String courseId, String id);
}
