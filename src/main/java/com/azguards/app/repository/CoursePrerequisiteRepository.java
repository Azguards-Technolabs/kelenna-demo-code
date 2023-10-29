package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CoursePrerequisite;

@Repository
public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, String> {

	public List<CoursePrerequisite> findByCourseId (String courseId);
	
}
