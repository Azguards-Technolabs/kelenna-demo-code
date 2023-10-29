package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteCampus;
import com.azguards.app.dao.InstituteCampusDao;
import com.azguards.app.repository.InstituteCampusRepository;

@Repository
public class InstituteCampusDaoImpl implements InstituteCampusDao {

	@Autowired
	private InstituteCampusRepository repository;

	@Override
	public List<InstituteCampus> saveAll(List<InstituteCampus> entities) {
		return repository.saveAll(entities);
	}

	@Override
	public List<InstituteCampus> findInstituteCampuses(String instituteId) {
		return repository.findInstituteCampuses(instituteId);
	}

	@Override
	public void deleteAll(List<InstituteCampus> courseInstitutes) {
		repository.deleteAll(courseInstitutes);
	}
}
