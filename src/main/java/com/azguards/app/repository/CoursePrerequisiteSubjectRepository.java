package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CoursePrerequisiteSubjects;

@Repository
public interface CoursePrerequisiteSubjectRepository extends JpaRepository<CoursePrerequisiteSubjects, String>{

	public List<CoursePrerequisiteSubjects> findByCoursePrerequisiteId(String coursePrerequisiteId);
}
