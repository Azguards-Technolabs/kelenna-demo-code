package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.GradeDetails;

public interface GradeDao {

	public String getGradeDetails(final String countryId, final String educationSystemId, final String grade);

	public List<GradeDetails> getGrades(final String countryId, final String systemId);

	public void saveAll(List<GradeDetails> grades);

	public GradeDetails findByCountryNameAndStateNameAndGradeAndEducationSystem(String countryName, String stateName,
			String grade, EducationSystem educationSystem);
}
