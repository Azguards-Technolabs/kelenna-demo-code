package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteEnglishRequirements;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.InstituteEnglishRequirementsDao;
import com.azguards.app.dto.InstituteEnglishRequirementsDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class InstituteEnglishRequirementsProcessor {
	
	@Autowired
	private InstituteDao iInstituteDAO;
	
	@Autowired
	private InstituteEnglishRequirementsDao instituteEnglishRequirementsDao;

	@Autowired
	private MessageTranslator messageTranslator;
	
	public void addInstituteEnglishRequirements(String userId, String instituteId, InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception {
		log.debug("Inside InstituteEnglishRequirementsDao method()");
		// TODO validate userId have access for institute Id
		log.info("Getting institute having institute id "+instituteId);
		Optional<Institute> sourceInstituteFromDB = iInstituteDAO.getInstituteByInstituteId(instituteId);
		if (!sourceInstituteFromDB.isPresent()) {
			log.error(messageTranslator.toLocale("institute_info.id.notfound",instituteId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("institute_info.id.notfound",instituteId));
		}
		
		log.info("Adding institute english requirements with name "+instituteEnglishRequirementsDto.getExamName());
		InstituteEnglishRequirements instituteEnglishRequirements = new InstituteEnglishRequirements(instituteId, instituteEnglishRequirementsDto.getExamName(),
				instituteEnglishRequirementsDto.getReadingMarks(), instituteEnglishRequirementsDto.getListningMarks(), instituteEnglishRequirementsDto.getWritingMarks(), instituteEnglishRequirementsDto.getOralMarks(), 
				  new Date(), new Date(), "API", "API");
		log.info("Persisting institute english requirements in DB");
		instituteEnglishRequirementsDao.addUpdateInsituteEnglishRequirements(instituteEnglishRequirements);
	}
	
	public void updateInstituteEnglishRequirements (String userId, String instituteEnglishRequirementsId, InstituteEnglishRequirementsDto instituteEnglishRequirementsDto) throws Exception {
		log.debug("Inside updateInstituteEnglishRequirements method()");
		// TODO validate userId have access for institute Id
		log.info("Getting institute english requirements having requirement id  "+instituteEnglishRequirementsId);
		Optional<InstituteEnglishRequirements> optionalInstituteEnglishRequirement = instituteEnglishRequirementsDao.getInsituteEnglishRequirementsById(instituteEnglishRequirementsId);
		if (!optionalInstituteEnglishRequirement.isPresent()) {
			log.error(messageTranslator.toLocale("english_requirement.id.notfound",instituteEnglishRequirementsId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("english_requirement.id.notfound",instituteEnglishRequirementsId));
		}
		InstituteEnglishRequirements instituteEnglishRequirements = optionalInstituteEnglishRequirement.get();
		log.info("Getting institute id from InstituteEnglishRequirements and validate it with user id");
		// TODO validate userId have access for institute Id 
		
		log.info("updating institute english requirements with id "+instituteEnglishRequirementsId);
		instituteEnglishRequirements.setExamName(instituteEnglishRequirementsDto.getExamName());
		instituteEnglishRequirements.setListningMarks(instituteEnglishRequirementsDto.getListningMarks());
		instituteEnglishRequirements.setOralMarks(instituteEnglishRequirementsDto.getOralMarks());
		instituteEnglishRequirements.setReadingMarks(instituteEnglishRequirementsDto.getReadingMarks());
		instituteEnglishRequirements.setWritingMarks(instituteEnglishRequirementsDto.getWritingMarks());
		instituteEnglishRequirements.setUpdatedBy("API");
		instituteEnglishRequirements.setUpdatedOn(new Date());
		log.info("Persisting institute english requirements in DB");
		instituteEnglishRequirementsDao.addUpdateInsituteEnglishRequirements(instituteEnglishRequirements);
	}
	
	public List<InstituteEnglishRequirementsDto> getListOfInstituteEnglishRequirements (String userId, String instituteId,String caller) {
		List<InstituteEnglishRequirementsDto> listOfInstituteEnglishRequirementsResponseDto = new ArrayList<InstituteEnglishRequirementsDto>();
		if (caller.equalsIgnoreCase("PRIVATE")) {
			//TODO check user id have appropriated access for institute id
		}
		log.info("Getting list of institute english requirements for institute id "+instituteId);
		List<InstituteEnglishRequirements> listOfInstituteEnglishRequirements = instituteEnglishRequirementsDao.getInsituteEnglishRequirementsByInstituteId(instituteId);
		if (!CollectionUtils.isEmpty(listOfInstituteEnglishRequirements)) {
			listOfInstituteEnglishRequirements.stream().forEach(instituteEnglishQualification -> {
				InstituteEnglishRequirementsDto instituteEnglishRequirementsDto = new InstituteEnglishRequirementsDto(instituteEnglishQualification.getId(), instituteEnglishQualification.getExamName(), instituteEnglishQualification.getReadingMarks(), instituteEnglishQualification.getListningMarks(),
						instituteEnglishQualification.getWritingMarks(), instituteEnglishQualification.getOralMarks());
				listOfInstituteEnglishRequirementsResponseDto.add(instituteEnglishRequirementsDto);
			});
		}
		return listOfInstituteEnglishRequirementsResponseDto;	
	}
	
	public void deleteInstituteEnglishRequirements(String userId, String instituteEnglishRequirementsId) {
		log.debug("Inside deleteInstituteEnglishRequirements method()");
		// TODO validate userId have access for institute Id
		log.info("Getting institute english requirements having requirement id  " + instituteEnglishRequirementsId);
		Optional<InstituteEnglishRequirements> optionalInstituteEnglishRequirement = instituteEnglishRequirementsDao
				.getInsituteEnglishRequirementsById(instituteEnglishRequirementsId);
		if (optionalInstituteEnglishRequirement.isPresent()) {
			log.info("Deleting institute english requirement by Id " + instituteEnglishRequirementsId);
			instituteEnglishRequirementsDao.deleteInstituteEnglishRequirementsById(instituteEnglishRequirementsId);
		}
	}
}
