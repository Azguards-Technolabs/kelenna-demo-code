package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.InstituteService;
import com.azguards.app.dao.InstituteServiceDao;
import com.azguards.app.repository.InstituteServiceRepository;
import com.azguards.common.lib.dto.CountDto;

@Service
public class InstituteServiceDaoImpl implements InstituteServiceDao {

	@Autowired
	private InstituteServiceRepository instituteServiceRepository;

	@Override
	public InstituteService get(String id) {
		return instituteServiceRepository.getOne(id);
	}

	@Override
	public List<InstituteService> getAllInstituteService(String instituteId) {
		return instituteServiceRepository.findByInstituteId(instituteId);
	}

	@Override
	public List<InstituteService> saveAll(List<InstituteService> listOfInstituteService) {
		return instituteServiceRepository.saveAll(listOfInstituteService);
	}

	@Transactional
	@Override
	public void delete(String instituteServiceId) {
		instituteServiceRepository.deleteById(instituteServiceId);
	}

	@Override
	public List<CountDto> countByInstituteIds(List<String> instituteIds) {
		return instituteServiceRepository.countByInstituteIdsIn(instituteIds);
	}
}