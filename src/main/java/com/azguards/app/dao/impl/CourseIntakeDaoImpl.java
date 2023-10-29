package com.azguards.app.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.dao.CourseIntakeDao;
import com.azguards.app.repository.CourseIntakeRepository;

@Component
public class CourseIntakeDaoImpl implements CourseIntakeDao {

	@Autowired
	private CourseIntakeRepository courseIntakeRepository;

	@Transactional
	@Override
	public void deleteById(String id) {
		courseIntakeRepository.deleteById(id);
	}
}
