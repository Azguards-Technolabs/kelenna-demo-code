package com.azguards.app.dao;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.azguards.app.bean.Faculty;

public interface FacultyDao {

	public void saveOrUpdateFaculty(final Faculty obj);

	public Faculty get(final String id);

	public List<Faculty> getAll();

	public List<Faculty> getFacultyListByFacultyNames(final List<String> facultyNameList);

	public Faculty getFacultyByFacultyName(String facultyName);

	@Cacheable(value = "cacheFacultyMap", unless = "#result == null")
	Map<String, String> getFacultyNameIdMap();
	
	@Cacheable(value = "cacheFaculty", key = "#id", unless = "#result == null", condition="#id!=null")
	Faculty getFaculty(final String id);
}
