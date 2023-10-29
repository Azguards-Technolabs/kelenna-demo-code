package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteCampus;
import com.azguards.app.dao.InstituteCampusDao;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dto.InstituteCampusDto;
import com.azguards.app.util.CommonUtil;
import com.azguards.common.lib.dto.institute.TimingDto;
import com.azguards.common.lib.exception.ConstraintVoilationException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InstituteCampusProcessor {
	
	@Autowired
	TimingProcessor timingProcessor;

	@Autowired
	@Lazy
	InstituteProcessor instituteProcessor;
	
	@Autowired
	InstituteDao instituteDao;

	@Autowired
	InstituteCampusDao instituteCampusDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void addCampus(String userId, String instituteId, List<String> campusInstituteIds)
			throws NotFoundException, ValidationException {
		log.info("Inside InstituteCampusProcessor.createCampus");
		campusInstituteIds.add(instituteId);
		List<Institute> institutes = instituteProcessor.validateAndGetInstituteByIds(campusInstituteIds);
		Institute institute = institutes.stream().filter(e->e.getId().equals(instituteId)).findAny().orElse(null);
		institutes.removeIf(e->e.getId().equals(instituteId));
		campusInstituteIds.removeIf(e->e.equals(instituteId));
		Set<String> dbCampusInstituteIds = getInstituteCampuses("", instituteId).stream().map(e->e.getId()).collect(Collectors.toSet());
		if (campusInstituteIds.stream().anyMatch(e -> e.equals(instituteId))) {
			log.error(messageTranslator.toLocale("institute.id.same",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute.id.same"));
		}
		if (!Collections.disjoint(dbCampusInstituteIds, campusInstituteIds)) {
			log.error(messageTranslator.toLocale("institute.already_campus",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute.already_campus"));
		}
		List<InstituteCampus> campuses = new ArrayList<>();
		institutes.stream().forEach(e -> {
			InstituteCampus instituteCampus = new InstituteCampus();
			instituteCampus.setAuditFields(userId);
			instituteCampus.setSourceInstitute(institute);
			instituteCampus.setDestinationInstitute(e);
			campuses.add(instituteCampus);
		});
		instituteCampusDao.saveAll(campuses);
	}

	@Transactional(rollbackFor = {ConstraintVoilationException.class,Exception.class})
	public List<InstituteCampusDto> getInstituteCampuses(String userId, String instituteId) throws NotFoundException {
		log.debug("inside InstituteCampusProcessor.getInstitutCampuses method.");
		Institute institute = instituteDao.get(instituteId);
		if (!ObjectUtils.isEmpty(institute)) {
			List<InstituteCampus> instituteCampuses = instituteCampusDao.findInstituteCampuses(instituteId);
			
			List<Institute> institutes = new ArrayList<>();
			instituteCampuses.stream().forEach(e->{
				if(!institutes.stream().anyMatch(in->in.getId().equals(e.getSourceInstitute().getId()))) {
					institutes.add(e.getSourceInstitute());
				}
				if(!institutes.stream().anyMatch(in->in.getId().equals(e.getDestinationInstitute().getId()))) {
					institutes.add(e.getDestinationInstitute());
				}
			});
			
			List<InstituteCampusDto> instituteCampuseDtos = institutes.stream().map(e -> {
				InstituteCampusDto campusDto = modelMapper.map(e, InstituteCampusDto.class);
				campusDto.setCampusName(e.getName());
				if (e.getCreatedBy().equals(userId)) {
					campusDto.setHasEditAccess(true);
				} else {
					campusDto.setHasEditAccess(false);
				}
				return campusDto;
			}).collect(Collectors.toList());

			instituteCampuseDtos.stream().forEach(e -> {
				TimingDto instituteTimingResponseDto = timingProcessor
						.getTimingResponseDtoByInstituteId(e.getId());
				e.setInstituteTimings(CommonUtil.convertTimingResponseDtoToDayTimingDto(instituteTimingResponseDto));

			});
			return instituteCampuseDtos;
		} else {
			log.error(messageTranslator.toLocale("institute.id.invalid", instituteId, Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("institute.id.invalid", instituteId));
		}
	}

	@Transactional
	public void removeCampuses(String userId, String instituteId, @Valid List<String> instituteIds)
			throws NotFoundException {
		log.info("inside InstituteCampusProcessor.getLinkedInstituteIds");
		instituteIds.add(instituteId);
		List<Institute> institutes = instituteProcessor.validateAndGetInstituteByIds(instituteIds);
		institutes.removeIf(e->e.getId().equals(instituteId));
		instituteIds.removeIf(e->e.equals(instituteId));
		if (instituteIds.stream().anyMatch(e -> e.equals(instituteId))) {
			log.error(messageTranslator.toLocale("institute.id.same",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute.id.same"));
		}
		List<InstituteCampus> deleteList = new ArrayList<>(); 
		List<InstituteCampus> instituteCampuses = instituteCampusDao.findInstituteCampuses(instituteId);
		institutes.stream().forEach(campus -> {
			log.debug("going to see if institute to be unLinked is linked as primary or secondary");
			Optional<InstituteCampus> secondaryLink = instituteCampuses.stream()
					.filter(e -> e.getDestinationInstitute().getId().equals(campus.getId()))
					.findAny();
			InstituteCampus toBeDeleted = null;
			if (secondaryLink.isPresent()) {
				log.debug("institute is secondary institute");
				toBeDeleted = secondaryLink.get();
			} else {
				log.debug("institute is primary institute");
				Institute firstSecondayInstitute = instituteCampuses.get(0).getDestinationInstitute();
				instituteCampuses.stream().forEach(e -> {
					e.setSourceInstitute(firstSecondayInstitute);
				});
				toBeDeleted = instituteCampuses.get(0);
			}
			deleteList.add(toBeDeleted);
		});
		
	}
}
