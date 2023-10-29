package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseEnglishEligibility;

@Repository
public interface CourseEnglishEligibilityRepository extends JpaRepository<CourseEnglishEligibility, String> {

	public List<CourseEnglishEligibility> findByCourseId(String courseId);
}