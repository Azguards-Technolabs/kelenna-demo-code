package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteRecommendRequest;
import com.azguards.app.constant.RecommendRequestStatus;

@Repository
public interface InstituteRecommendationRequestRepository extends JpaRepository<InstituteRecommendRequest, String> {

	public List<InstituteRecommendRequest> findByInstituteName (String instituteName);
	
	public List<InstituteRecommendRequest> findByRecommendRequestStatus (RecommendRequestStatus recommendRequestStatus);
}
