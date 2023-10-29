package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.InstituteService;
import com.azguards.common.lib.dto.CountDto;

public interface InstituteServiceDao {

	public InstituteService get(String id);

	public List<InstituteService> getAllInstituteService(String instituteId);

	public List<InstituteService> saveAll(List<InstituteService> listOfInstituteService);

	public void delete(String instituteServiceId);
	
	public List<CountDto> countByInstituteIds(List<String> instituteIds);
}
