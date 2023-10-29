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

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteFacility;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.InstituteFacilityDao;
import com.azguards.app.dao.ServiceDao;
import com.azguards.app.dto.FacilityDto;
import com.azguards.app.dto.InstituteFacilityDto;
import com.azguards.app.util.CommonUtil;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InstituteFacilityProcessor {

	@Autowired
	private InstituteFacilityDao instituteFacilityDao;

	@Autowired
	private InstituteDao instituteDao;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private MessageTranslator messageTranslator;
	/*
	 * @Autowired private UserAccessUtils userAccessUtils;
	 */

	public void addInstituteFacility(String instituteId, InstituteFacilityDto instituteFacilityDto)
			throws NotFoundException {
		List<InstituteFacility> listOfFacilityToBeSaved = new ArrayList<>();
		log.debug("inside addInstituteFacility() method");
		//	userAccessUtils.validateUserAccess(userId, instituteId, "facility page", "add");

		Optional<Institute> institute = instituteDao.getInstituteByInstituteId(instituteId);
		if (!institute.isPresent()) {
			log.error(messageTranslator.toLocale("institute.id.illegal",instituteId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("institute.id.illegal",instituteId));
		}

		log.info("checking all exsisting facility to match with facility passed in request ");
		List<InstituteFacility> listOfExsistingInstituteFacility = instituteFacilityDao
				.getAllInstituteFacility(instituteId);

		for (FacilityDto facilityDto : instituteFacilityDto.getFacilities()) {
			InstituteFacility instituteFacilityFromDB = listOfExsistingInstituteFacility.stream().filter(
					facilityFromDB -> facilityFromDB.getService().getId().equalsIgnoreCase(facilityDto.getFacilityId()))
					.findAny().orElse(null);

			if (ObjectUtils.isEmpty(instituteFacilityFromDB)) {
				log.info("No facility present for institute facility Id {} adding it to list",
						facilityDto.getFacilityId());

				Optional<com.azguards.app.bean.Service> service = serviceDao.getServiceById(facilityDto.getFacilityId());
				if (!service.isPresent()) {
					log.error(messageTranslator.toLocale("institute.facility.id.illegal",facilityDto.getFacilityId(),Locale.US));
					throw new NotFoundException(messageTranslator.toLocale("institute.facility.id.illegal",facilityDto.getFacilityId()));
				}
				InstituteFacility instituteFacility = new InstituteFacility(institute.get(), service.get(), new Date(),
						new Date(), "API", "API");

				listOfFacilityToBeSaved.add(instituteFacility);
			} else {
				log.info("Institute facility present for institute facility id {} skipping it",
						facilityDto.getFacilityId());
			}
		}

		log.info("Persisting facility list to DB ");
		instituteFacilityDao.saveInstituteFacility(listOfFacilityToBeSaved);
	}

	@Transactional(rollbackOn = Throwable.class)
	public void deleteInstituteFacilities(String instituteId, List<String> instituteFacilitiesId) {
		log.debug("inside deleteInstituteFacilities() method");
		//userAccessUtils.validateUserAccess(userId, instituteId, "facility page", "delete");

		if (!CollectionUtils.isEmpty(instituteFacilitiesId)) {
			instituteFacilitiesId.stream().forEach(instituteFacilityId -> {
				log.info("deleting facility having institute facility Id {} and institute id {}", instituteFacilityId,
						instituteId);
				instituteFacilityDao.deleteFacilityByIdAndInstituteId(instituteFacilityId, instituteId);
			});
		} else {
			log.warn("no institute facilities id passed in request");
		}
	}

	public InstituteFacilityDto getFacilitiesByInstituteId(String instituteId) {
		InstituteFacilityDto instituteFacilityDto = new InstituteFacilityDto();
		log.debug("inside getFacilitiesByInstituteId() method");
		//	userAccessUtils.validateUserAccess(userId, instituteId, "facility page", "get");

		log.info("Getting all facilites for institute id " + instituteId);
		List<InstituteFacility> listOfExsistingInstituteFacility = instituteFacilityDao
				.getAllInstituteFacility(instituteId);
		if (!CollectionUtils.isEmpty(listOfExsistingInstituteFacility)) {
			log.info("Facility from db not empty for institute id {}", instituteId);
			//instituteFacilityDto = DTOUtils.createInstituteFacilityResponseDto(listOfExsistingInstituteFacility);

			instituteFacilityDto = CommonUtil.createInstituteFacilityResponseDto(listOfExsistingInstituteFacility);
		}
		return instituteFacilityDto;
	}

	public InstituteFacilityDto getPublicServiceByInstituteId(String instituteId) {
		InstituteFacilityDto instituteFacilityDto = new InstituteFacilityDto();
		log.debug("inside getFacilitiesByInstituteId() method");
		log.info("Getting all facilites for institute id {}", instituteId);
		List<InstituteFacility> listOfExsistingInstituteFacility = instituteFacilityDao
				.getAllInstituteFacility(instituteId);
		if (!CollectionUtils.isEmpty(listOfExsistingInstituteFacility)) {
			log.info("Facility from db not empty for institute id {}", instituteId);
			//instituteFacilityDto = DTOUtils.createInstituteFacilityResponseDto(listOfExsistingInstituteFacility);

			CommonUtil.createInstituteFacilityResponseDto(listOfExsistingInstituteFacility);
		}
		return instituteFacilityDto;
	}
}
