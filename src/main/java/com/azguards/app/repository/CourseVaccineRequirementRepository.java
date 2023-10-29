package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseVaccineRequirement;

@Repository
public interface CourseVaccineRequirementRepository extends JpaRepository<CourseVaccineRequirement, String> {

}
