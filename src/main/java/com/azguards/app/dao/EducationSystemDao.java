package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.Subject;
import com.azguards.common.lib.dto.institute.EducationSystemDto;

public interface EducationSystemDao {

	public void save(final EducationSystem hobbiesObj);
	
	public void saveAll(List<EducationSystem> educationSystems);

	public void update(final EducationSystem hobbiesObj);

	public EducationSystem get(final String id);

	public List<EducationSystem> getAll();

	public List<EducationSystem> getAllGlobeEducationSystems();

	public List<EducationSystem> getEducationSystemsByCountryName(final String countryId);

	public List<Subject> getSubject();

	public List<Subject> getSubjectByEducationSystem(final String educationSystemId);

	public List<EducationSystemDto> getEducationSystemByCountryNameAndStateName(String countryName, String stateName);

	public EducationSystem findByNameAndCountryNameAndStateName(String name,String countryName,String stateName);
}
