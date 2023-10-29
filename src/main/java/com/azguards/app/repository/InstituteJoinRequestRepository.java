package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteJoinRequest;
import com.azguards.app.constant.InstituteJoinStatus;

@Repository
public interface InstituteJoinRequestRepository extends JpaRepository<InstituteJoinRequest, String> {
	
	public InstituteJoinRequest findByInstituteNameAndUserId (String instituteName, String userId);
	
	public List<InstituteJoinRequest> findByInstituteJoinStatus (InstituteJoinStatus instituteJoinStatus);

}
