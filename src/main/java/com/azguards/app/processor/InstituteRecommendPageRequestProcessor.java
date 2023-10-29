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

import com.azguards.app.bean.InstituteRecommendRequest;
import com.azguards.app.constant.RecommendRequestStatus;
import com.azguards.app.dao.InstituteRecommendationRequestDao;
import com.azguards.app.dto.InstituteRecommendPageRequestDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;
@Service
@CommonsLog
public class InstituteRecommendPageRequestProcessor {

	@Autowired
	private InstituteRecommendationRequestDao instituteRecommendationRequestDao;

	@Autowired
	private MessageTranslator messageTranslator;

	public void recommendInstiutePageRequest (String userId, InstituteRecommendPageRequestDto instituteRecommendPageRequestDto) throws Exception {
		log.debug("inside recommendInstiutePageRequest() method");
		log.info("getting institute recommend request by institute name "+instituteRecommendPageRequestDto.getInstituteName()+ " and user id "+userId);
		List<InstituteRecommendRequest> listOfInstituteRecommendRequest = instituteRecommendationRequestDao.getInstituteRecommendationByInstituteName(instituteRecommendPageRequestDto.getInstituteName());
		InstituteRecommendRequest instituteRecommendRequestFromDB = listOfInstituteRecommendRequest.stream().filter(instituteRecommendRequest -> instituteRecommendRequest.getAddressInstitute().equalsIgnoreCase(instituteRecommendPageRequestDto.getAddressInstitute())).findAny().orElse(null);
		if (!ObjectUtils.isEmpty(instituteRecommendRequestFromDB)) {
			log.error("recommedation for institute name "+instituteRecommendPageRequestDto.getInstituteName()+ " having address "+instituteRecommendPageRequestDto.getAddressInstitute()+ " have been processed");
			throw new ValidationException("recommedation for institute name "+instituteRecommendPageRequestDto.getInstituteName()+ " having address "+instituteRecommendPageRequestDto.getAddressInstitute()+ " have been processed");
		}
		log.info("creating new recommendation request for institute name "+instituteRecommendPageRequestDto.getInstituteName()+ " having address "+instituteRecommendPageRequestDto.getAddressInstitute());
		InstituteRecommendRequest instituteRecommendRequest = new InstituteRecommendRequest(userId, instituteRecommendPageRequestDto.getInstituteName(), instituteRecommendPageRequestDto.getIsWorking(), instituteRecommendPageRequestDto.getKnowingSomeoneWorking(), instituteRecommendPageRequestDto.getName(), instituteRecommendPageRequestDto.getTitle(),
				instituteRecommendPageRequestDto.getEmail(), instituteRecommendPageRequestDto.getPhoneNumber(), instituteRecommendPageRequestDto.getAddressInstitute(), instituteRecommendPageRequestDto.getWebsite(), RecommendRequestStatus.PENDING, new Date(), new Date(), "API", "API");
		log.info("persisting institute recommendation request for institute name "+instituteRecommendPageRequestDto.getInstituteName()+ " having address "+instituteRecommendPageRequestDto.getAddressInstitute());
		instituteRecommendationRequestDao.addInstituteRecommendationRequest(instituteRecommendRequest);
	}
	
	public List<InstituteRecommendPageRequestDto> getInstituteRecommendRequestByStatus(String status) {
		List<InstituteRecommendPageRequestDto> listOfInstituteRecommendPageRequestDto = new ArrayList<InstituteRecommendPageRequestDto>();
		log.debug("inside getInstituteRecommendRequestByStatus() method");
		log.info("getting institute recommend request by status "+status);
		List<InstituteRecommendRequest> listOfInstituteRecommendRequest = instituteRecommendationRequestDao.getInstituteRecommendationRequestByStatus(RecommendRequestStatus.valueOf(status));
		if (!CollectionUtils.isEmpty(listOfInstituteRecommendRequest)) {
			log.info("listOfInstituteRecommendRequest is not empty for  status "+status+ " creating response dto");
			listOfInstituteRecommendRequest.stream().forEach(instituteRecommendationRequest -> {
				InstituteRecommendPageRequestDto instituteRecommendPageRequestDto = new InstituteRecommendPageRequestDto(instituteRecommendationRequest.getId(), instituteRecommendationRequest.getInstituteName(), 
						instituteRecommendationRequest.getIsWorking(), instituteRecommendationRequest.getKnowingSomeoneWorking(), instituteRecommendationRequest.getName(), instituteRecommendationRequest.getEmail(),
						instituteRecommendationRequest.getPhoneNumber(), instituteRecommendationRequest.getTitle(), instituteRecommendationRequest.getAddressInstitute(), instituteRecommendationRequest.getWebsiteLink());
						listOfInstituteRecommendPageRequestDto.add(instituteRecommendPageRequestDto);
			});
		}
		return listOfInstituteRecommendPageRequestDto;
	}
	
	@Transactional(rollbackOn = Throwable.class)
	public void updateInstituteRecommedRequestStatus(String instituteRecommendRequestId, String status) throws Exception{
		log.debug("inside updateInstituteRecommedRequestStatus() method");
		log.info("Updating institute recommend request for id "+instituteRecommendRequestId+ " to status "+status );
		Optional<InstituteRecommendRequest> instituteRecommendRequestFromDb = instituteRecommendationRequestDao.getInstituteRecommendationById(instituteRecommendRequestId);
		if (!instituteRecommendRequestFromDb.isPresent()) {
			log.error(messageTranslator.toLocale("institute-recommend.id.not_found",instituteRecommendRequestId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("institute-recommend.id.not_found" ,instituteRecommendRequestId));
		}
		InstituteRecommendRequest instituteRecommendRequest = instituteRecommendRequestFromDb.get();
		instituteRecommendRequest.setRecommendRequestStatus(RecommendRequestStatus.PROCESSED);
		instituteRecommendRequest.setUpdatedBy("API");
		instituteRecommendRequest.setUpdatedOn(new Date());
		instituteRecommendationRequestDao.addInstituteRecommendationRequest(instituteRecommendRequest);		
	}
}
