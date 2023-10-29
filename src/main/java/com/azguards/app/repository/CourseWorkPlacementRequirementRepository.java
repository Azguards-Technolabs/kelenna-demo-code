package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseWorkPlacementRequirement;

@Repository
public interface CourseWorkPlacementRequirementRepository extends JpaRepository<CourseWorkPlacementRequirement, String> {

}
