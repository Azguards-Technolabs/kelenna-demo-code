package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import com.azguards.app.bean.InstituteRecommendRequest;
import com.azguards.app.constant.RecommendRequestStatus;

public interface InstituteRecommendationRequestDao {

	public List<InstituteRecommendRequest> getInstituteRecommendationByInstituteName (String instituteName);
	
	public void addInstituteRecommendationRequest (InstituteRecommendRequest instituteRecommendRequest) ;
	
	public List<InstituteRecommendRequest> getInstituteRecommendationRequestByStatus (RecommendRequestStatus recommendRequestStatus) ;
	
	public Optional<InstituteRecommendRequest>  getInstituteRecommendationById (String instituteRecommendationId);
}
