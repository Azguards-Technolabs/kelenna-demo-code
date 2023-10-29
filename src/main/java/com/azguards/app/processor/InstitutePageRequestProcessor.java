package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.InstitutePageRequest;
import com.azguards.app.constant.PageRequestStatus;
import com.azguards.app.dao.InstitutePageRequestDao;
import com.azguards.app.dto.InstitutePageRequestDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class InstitutePageRequestProcessor {
	
	@Autowired
	private InstitutePageRequestDao institutePageRequestDao;

	@Autowired
	private MessageTranslator messageTranslator;

	public InstitutePageRequestDto requestInstiutePageRequest (String userId, String instituteId , InstitutePageRequestDto institutePageRequestDto) throws Exception {
		log.debug("inside requestInstiutePageRequest() method");
		log.info("getting institute page request by institute id "+instituteId);
		List<InstitutePageRequest> listOfInstitutePageRequest = institutePageRequestDao.getInstitutePageRequestByInstituteId(instituteId);
		InstitutePageRequest institutePageRequestGranted = listOfInstitutePageRequest.stream().filter(institutePageRequest -> institutePageRequest.getPageRequestStatus().equals(PageRequestStatus.GRANTED)).findAny().orElse(null);
		if (!ObjectUtils.isEmpty(institutePageRequestGranted)) {
			log.error("page for institute id "+instituteId+ " have been alloted");
			throw new ValidationException("page for institute id "+instituteId+ " have been alloted");
		}
		log.info("getting institute page request by institute id "+instituteId+ " and user id "+userId);
		InstitutePageRequest institutePageRequestObjectForInstituteIdAndUserId = listOfInstitutePageRequest.stream().filter(institutePageRequest -> institutePageRequest.getUserId().equalsIgnoreCase(userId)).findAny().orElse(null);
		if (ObjectUtils.isEmpty(institutePageRequestObjectForInstituteIdAndUserId)) {
			log.info("No institute page request found for institute id "+instituteId+ " and user id "+userId+ " creating new request");
			InstitutePageRequest institutePageRequest = new InstitutePageRequest(instituteId, userId, institutePageRequestDto.getFirstName(), institutePageRequestDto.getLastName(), institutePageRequestDto.getTitle(), institutePageRequestDto.getEmail(),
					institutePageRequestDto.getPhoneNumber(), institutePageRequestDto.getManagementName(), institutePageRequestDto.getManagementEmail(),
					institutePageRequestDto.getManagementPhoneNumber(), PageRequestStatus.PENDING, new Date(), new Date(), "API", "API");
			InstitutePageRequest institutePageRequestSaved = institutePageRequestDao.addInstitutePageRequest(institutePageRequest);
			institutePageRequestDto.setInstitutePageRequestId(institutePageRequestSaved.getId());
		} else if (institutePageRequestObjectForInstituteIdAndUserId.getPageRequestStatus().equals(PageRequestStatus.PENDING)) {
			log.warn("institute page request for institute id "+instituteId+ " by user id " +userId+ " is already in pending state" );
			throw new ValidationException("institute page request for institute id "+instituteId+ " by user id " +userId+ " is already in pending state");
		} else if (institutePageRequestObjectForInstituteIdAndUserId.getPageRequestStatus().equals(PageRequestStatus.REJECTED)) {
			log.error("institute page request for institute id "+instituteId+ " by user id " +userId+ " is rejected" );
			throw new ValidationException("institute page request for institute id "+instituteId+ " by user id " +userId+ " is rejected");
		}
		return institutePageRequestDto;
	}
	
	public List<InstitutePageRequestDto> getInstitutePageRequestByInstituteIdAndStatus(String instituteId, String status) {
		List<InstitutePageRequestDto> listOfInstitutePageRequestDto = new ArrayList<InstitutePageRequestDto>();
		log.debug("inside getInstitutePageRequestByInstituteIdAndStatus() method");
		log.info("getting institute page request by institute id "+instituteId+ " and status "+status);
		List<InstitutePageRequest> listOfInstitutePageRequest = institutePageRequestDao.getInstitutePageRequestByInstituteIdAndStatus(instituteId, PageRequestStatus.valueOf(status));
		if (!CollectionUtils.isEmpty(listOfInstitutePageRequest)) {
			listOfInstitutePageRequest.stream().forEach(institutePageRequest -> {
				InstitutePageRequestDto institutePageRequestDto = new InstitutePageRequestDto(institutePageRequest.getId(),institutePageRequest.getUserId() , institutePageRequest.getFirstName(),
						institutePageRequest.getLastName(), institutePageRequest.getTitle(), institutePageRequest.getEmail(),
						institutePageRequest.getPhoneNumber(), institutePageRequest.getManagementName(), institutePageRequest.getManagementEmail(), institutePageRequest.getPhoneNumber());
						listOfInstitutePageRequestDto.add(institutePageRequestDto);
			});
		}
		return listOfInstitutePageRequestDto;
	}
	
	@Transactional(rollbackOn = Throwable.class)
	public void updateInstitutePageRequestStatus(String institutePageRequestId, String status) throws Exception{
		log.debug("inside updateInstitutePageRequestStatus() method");
		log.info("Updating institute page request for id "+institutePageRequestId+ " to status "+status );
		Optional<InstitutePageRequest> institutePageRequestFromDb = institutePageRequestDao.getInstitutePageRequestById(institutePageRequestId);
		if (!institutePageRequestFromDb.isPresent()) {
			log.error(messageTranslator.toLocale("page_request.id.notfound",institutePageRequestId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("page_request.id.notfound",institutePageRequestId));
		}
		InstitutePageRequest institutePageRequest = institutePageRequestFromDb.get();
		if (!institutePageRequest.getPageRequestStatus().equals(PageRequestStatus.PENDING)) {
			log.error(messageTranslator.toLocale("join_request.status.not.changed",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("join_request.status.not.changed"));
		}
		institutePageRequest.setPageRequestStatus(PageRequestStatus.valueOf(status));
		institutePageRequest.setUpdatedBy("API");
		institutePageRequest.setUpdatedOn(new Date());
		institutePageRequestDao.addInstitutePageRequest(institutePageRequest);
		
		if (status.equalsIgnoreCase(PageRequestStatus.GRANTED.toString())) {
			log.info("making user id "+institutePageRequest.getUserId()+ " as super admin for institute id "+institutePageRequest.getInstituteId() );
			// make user id as super admin call identity for granting access
		}
		
	}
}
