package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CourseFunding;
import com.azguards.app.dao.CourseFundingDao;
import com.azguards.app.repository.CourseFundingRepository;
import com.azguards.common.lib.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CourseFundingDaoImpl implements CourseFundingDao {

	@Autowired
	private CourseFundingRepository courseFundingRepository;

	@Override
	public List<CourseFunding> saveAll(List<CourseFunding> courseFundings) throws ValidationException {
		try {
			return courseFundingRepository.saveAll(courseFundings);
		} catch (DataIntegrityViolationException e) {
			log.error("one or more course funding already exists");
			throw new ValidationException("one or more course funding already exists");
		}
	}
}
