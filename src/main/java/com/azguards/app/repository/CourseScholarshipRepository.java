package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseScholarship;

@Repository
public interface CourseScholarshipRepository extends JpaRepository<CourseScholarship, String> {
	CourseScholarship findByCourseId(String courseId);
}
