package com.azguards.app.service;

import java.util.List;

import com.azguards.app.bean.GlobalData;

public interface IGlobalStudentData {

	void saveGlobalStudentData(GlobalData globalStudentDataDto);

	void deleteAllGlobalStudentData();
	
	List<GlobalData> getCountryWiseStudentList(String countryName);
	
	long checkForPresenceOfUserCountryInGlobalDataFile(String countryName);
	
	List<String> getDistinctMigratedCountryForUserCountry (String countryName);
	
	List<String> getDistinctMigratedCountryForUserCountryOrderbyStudentCount (String countryName);
}
