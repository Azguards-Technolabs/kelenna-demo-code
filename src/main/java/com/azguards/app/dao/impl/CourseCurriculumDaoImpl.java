package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CourseCurriculum;
import com.azguards.app.dao.CourseCurriculumDao;
import com.azguards.app.repository.CourseCurriculumRepository;

@Component
public class CourseCurriculumDaoImpl implements CourseCurriculumDao {

	@Autowired
	private CourseCurriculumRepository courseCurriculumRespository;

	@Override
	public Optional<CourseCurriculum> getById(String id) {
		return courseCurriculumRespository.findById(id);
	}

	@Override
	public List<CourseCurriculum> getAll() {
		return courseCurriculumRespository.findAll();
	}

}
