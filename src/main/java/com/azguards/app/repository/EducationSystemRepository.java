package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.EducationSystem;

@Repository
public interface EducationSystemRepository extends JpaRepository<EducationSystem, String> {

	EducationSystem findByNameAndCountryNameAndStateName(String name, String countryName, String stateName);
}
