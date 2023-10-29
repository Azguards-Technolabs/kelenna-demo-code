package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.InstituteJoinRequest;
import com.azguards.app.constant.InstituteJoinStatus;
import com.azguards.app.dao.InstituteJoinRequestDao;
import com.azguards.app.repository.InstituteJoinRequestRepository;

@Component
public class InstituteJoinRequestDaoImpl implements InstituteJoinRequestDao {
	
	@Autowired
	private InstituteJoinRequestRepository instituteJoinRequestRepository;

	@Override
	public InstituteJoinRequest getInstituteJoinRequestByInstituteNameAndUserId(String instituteName, String userId) {
		return instituteJoinRequestRepository.findByInstituteNameAndUserId(instituteName, userId);
	}

	@Override
	public InstituteJoinRequest addInstituteJoinRequest(InstituteJoinRequest instituteJoinRequest) {
		return instituteJoinRequestRepository.save(instituteJoinRequest);
	}

	@Override
	public List<InstituteJoinRequest> getInstituteJoinRequestByStatus(InstituteJoinStatus instituteJoinStatus) {
		return instituteJoinRequestRepository.findByInstituteJoinStatus(instituteJoinStatus);
	}

	@Override
	public Optional<InstituteJoinRequest> getInstituteJoinRequestById(String instituteJoinRequestId) {
		return instituteJoinRequestRepository.findById(instituteJoinRequestId);
	}

}
