package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.GradeDetails;

@Repository
public interface GradeDetailRepository extends JpaRepository<GradeDetails, String> {
	GradeDetails findByCountryNameAndStateNameAndGradeAndEducationSystem(String countryName, String stateName,
			String grade, EducationSystem educationSystem);
}
