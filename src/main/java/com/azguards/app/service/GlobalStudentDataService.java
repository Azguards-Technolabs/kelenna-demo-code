package com.azguards.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.GlobalData;
import com.azguards.app.dao.IGlobalStudentDataDAO;

@Service
@Transactional(rollbackFor = Throwable.class)
public class GlobalStudentDataService implements IGlobalStudentData {

	@Autowired
	private IGlobalStudentDataDAO globalStudentDataDao;
	
	@Override
	public void saveGlobalStudentData(GlobalData globalStudentDataDto) {
		
		globalStudentDataDto.setUpdatedBy("API");
		globalStudentDataDto.setCreatedBy("API");
		globalStudentDataDto.setCreatedOn(new java.util.Date(System.currentTimeMillis()));
		globalStudentDataDto.setUpdatedOn(new java.util.Date(System.currentTimeMillis()));
		globalStudentDataDao.save(globalStudentDataDto);
	}
	
	@Override
	public void deleteAllGlobalStudentData() {
		globalStudentDataDao.deleteAll();
	}

	@Override
	public List<GlobalData> getCountryWiseStudentList(String countryName) {
		return globalStudentDataDao.getCountryWiseStudentList(countryName);
	}

	@Override
	public long checkForPresenceOfUserCountryInGlobalDataFile(String countryName) {
		return globalStudentDataDao.getNonZeroCountOfStudentsForCountry(countryName);
	}
	
	@Override
	public List<String> getDistinctMigratedCountryForUserCountry (String countryName) {
		return globalStudentDataDao.getDistinctMigratedCountryForStudentCountry(countryName);
	}
	
	@Override
	public List<String> getDistinctMigratedCountryForUserCountryOrderbyStudentCount (String countryName) {
		return globalStudentDataDao.getDistinctMigratedCountryForStudentCountry(countryName);
	}
}
