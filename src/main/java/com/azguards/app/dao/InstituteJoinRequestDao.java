package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import com.azguards.app.bean.InstituteJoinRequest;
import com.azguards.app.constant.InstituteJoinStatus;

public interface InstituteJoinRequestDao {
	
	public InstituteJoinRequest getInstituteJoinRequestByInstituteNameAndUserId (String instituteName, String userId);
	
	public InstituteJoinRequest addInstituteJoinRequest (InstituteJoinRequest instituteJoinRequest);

	public List<InstituteJoinRequest> getInstituteJoinRequestByStatus (InstituteJoinStatus instituteJoinStatus);
	
	public Optional<InstituteJoinRequest> getInstituteJoinRequestById (String instituteJoinRequestId);
}
