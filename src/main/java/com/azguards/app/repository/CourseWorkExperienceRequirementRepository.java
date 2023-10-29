package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseWorkExperienceRequirement;

@Repository
public interface CourseWorkExperienceRequirementRepository extends JpaRepository<CourseWorkExperienceRequirement, String> {

}
