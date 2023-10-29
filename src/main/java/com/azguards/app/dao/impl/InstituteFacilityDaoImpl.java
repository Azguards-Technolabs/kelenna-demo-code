package com.azguards.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.InstituteFacility;
import com.azguards.app.dao.InstituteFacilityDao;
import com.azguards.app.repository.InstituteFacilityRepository;

@Component
public class InstituteFacilityDaoImpl implements InstituteFacilityDao {

	@Autowired
	private InstituteFacilityRepository instituteFacilityRepository;

	@Override
	public List<InstituteFacility> getAllInstituteFacility(String instituteId) {
		return instituteFacilityRepository.findByInstituteId(instituteId);
	}

	@Override
	public void saveInstituteFacility(List<InstituteFacility> listOfInstituteFacility) {
		instituteFacilityRepository.saveAll(listOfInstituteFacility);
	}

	@Override
	public void deleteFacilityByIdAndInstituteId(String instituteFacilityId, String instituteId) {
		instituteFacilityRepository.deleteByIdAndInstituteId(instituteFacilityId, instituteId);
	}
}
