package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.InstituteCampus;

public interface InstituteCampusDao {
	List<InstituteCampus> saveAll(List<InstituteCampus> entities);

	List<InstituteCampus> findInstituteCampuses(String instituteId);

	void deleteAll(List<InstituteCampus> courseInstitutes);
}