package com.azguards.app.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.dao.CourseWorkExperienceRequirementDao;
import com.azguards.app.repository.CourseWorkExperienceRequirementRepository;

@Component
public class CourseWorkExperienceRequirementDaoImpl implements CourseWorkExperienceRequirementDao {

	@Autowired
	private CourseWorkExperienceRequirementRepository repository;

	@Override
	public void deleteById(String id) {
		repository.deleteById(id);
	}

}
