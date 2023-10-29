package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteDomesticRankingHistory;

@Repository
public interface InstituteDomesticRankingHistoryRepository extends JpaRepository<InstituteDomesticRankingHistory, String>{

	public List<InstituteDomesticRankingHistory> findByInstituteId (String instituteId);
	
}
