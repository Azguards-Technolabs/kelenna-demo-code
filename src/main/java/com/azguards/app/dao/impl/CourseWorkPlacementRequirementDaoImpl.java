package com.azguards.app.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.dao.CourseWorkPlacementRequirementDao;
import com.azguards.app.repository.CourseWorkPlacementRequirementRepository;

@Component
public class CourseWorkPlacementRequirementDaoImpl implements CourseWorkPlacementRequirementDao {

	@Autowired
	private CourseWorkPlacementRequirementRepository repository;

	@Override
	public void deleteById(String id) {
		repository.deleteById(id);
	}

}
