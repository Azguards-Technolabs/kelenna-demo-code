package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.AccrediatedDetail;

@Repository
public interface AccrediatedDetailRepository extends JpaRepository<AccrediatedDetail, String>{

	public List<AccrediatedDetail> findByEntityId (String entityId);
	
	public AccrediatedDetail findByAccrediatedNameAndEntityId (String accrediatedName, String entityId);
	
	public void deleteByEntityId (String entityId);
	
	
}
