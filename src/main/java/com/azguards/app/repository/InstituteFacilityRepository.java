package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteFacility;

@Repository
public interface InstituteFacilityRepository extends JpaRepository<InstituteFacility, String> {

    public List<InstituteFacility> findByInstituteId (String instituteId);
	
	public void deleteByIdAndInstituteId (String facilityId, String instituteId);
	
}
