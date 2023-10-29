package com.azguards.app.processor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Faculty;
import com.azguards.app.bean.Institute;
import com.azguards.app.bean.Level;
import com.azguards.app.bean.Scholarship;
import com.azguards.app.bean.ScholarshipCountry;
import com.azguards.app.bean.ScholarshipEligibleNationality;
import com.azguards.app.bean.ScholarshipLanguage;
import com.azguards.app.dao.FacultyDao;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.LevelDao;
import com.azguards.app.dao.ScholarshipDao;
import com.azguards.app.dto.ScholarshipLevelCountDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.institute.ScholarshipRequestDto;
import com.azguards.common.lib.dto.institute.ScholarshipResponseDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScholarshipProcessor {

	@Autowired
	private ScholarshipDao scholarshipDAO;

	@Autowired
	private LevelDao levelDao;

	@Autowired
	private FacultyDao facultyDAO;

	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;

	@Autowired
	private StorageHandler storageHandler;

	@Autowired
	private InstituteDao instituteDao;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	ReadableIdProcessor readableIdProcessor;
	
	@Autowired
	private ConversionProcessor conversionProcessor;

	private Scholarship prepareModel(final String userId, final ScholarshipRequestDto scholarshipDto,
			final String existingScholarshipId) throws ValidationException {
		Scholarship scholarship = new Scholarship();
		if (!StringUtils.isEmpty(existingScholarshipId)) {
			scholarship = getScholarshipFomDb(existingScholarshipId);
		}
		BeanUtils.copyProperties(scholarshipDto, scholarship);
		if (StringUtils.isEmpty(existingScholarshipId)) {
			readableIdProcessor.setReadableIdForScholarship(scholarship);
			scholarship.setIsActive(true);
		}
		scholarship.setId(existingScholarshipId);
		scholarship.setAuditFields(userId);

		Set<Level> dbLevels = scholarship.getLevels();
		dbLevels.clear();
		if (!CollectionUtils.isEmpty(scholarshipDto.getLevelIds())) {
			log.info("Scholarship levels is not empty, start iterating data");
			List<Level> levels = levelDao.findByIdIn(scholarshipDto.getLevelIds());
			dbLevels.addAll(levels);
		}

		if (scholarshipDto.getFacultyId() != null) {
			log.info("faculty_id is not null, hence fetching faculty data from DB");
			Faculty faculty = facultyDAO.get(scholarshipDto.getFacultyId());
			if (faculty == null) {
				log.error(messageTranslator.toLocale("scolarship-processor.not_found.faculty_id", scholarshipDto.getFacultyId(),Locale.US));
				throw new ValidationException(messageTranslator.toLocale("scolarship-processor.not_found.faculty_id", scholarshipDto.getFacultyId()));
			}
			scholarship.setFaculty(faculty);
		}

		if (!ObjectUtils.isEmpty(scholarshipDto.getInstituteId())) {
			log.info("CourseId is not null hence fetching course data from DB");
			Institute institute = instituteDao.get(scholarshipDto.getInstituteId());
			if (ObjectUtils.isEmpty(institute)) {
				log.error(messageTranslator.toLocale("scolarship-processor.not_found.institute_id", scholarshipDto.getInstituteId(), Locale.US));
				throw new ValidationException(messageTranslator.toLocale("scolarship-processor.not_found.institute_id", scholarshipDto.getInstituteId()));
			}
			scholarship.setInstitute(institute);
		}

		final Scholarship finalScholarship = scholarship;

		List<ScholarshipLanguage> dbLanguages = scholarship.getScholarshipLanguages();

		if (!CollectionUtils.isEmpty(scholarshipDto.getLanguages())) {
			log.info("Scholarship languages is not null, start iterating data");

			scholarshipDto.getLanguages().stream().forEach(language -> {

				Optional<ScholarshipLanguage> existingLanguageOptional = dbLanguages.stream()
						.filter(e -> e.getName().equalsIgnoreCase(language)).findAny();

				String scholarshipLanguageId = null;
				ScholarshipLanguage scholarshipLanguage = new ScholarshipLanguage();
				if (existingLanguageOptional.isPresent()) {
					scholarshipLanguage = existingLanguageOptional.get();
					scholarshipLanguageId = scholarshipLanguage.getId();
				}
				scholarshipLanguage.setId(scholarshipLanguageId);
				scholarshipLanguage.setName(language);
				scholarshipLanguage.setAuditFields(userId,
						StringUtils.isEmpty(scholarshipLanguageId) ? null : scholarshipLanguage);
				scholarshipLanguage.setScholarship(finalScholarship);
				if (StringUtils.isEmpty(scholarshipLanguageId)) {
					dbLanguages.add(scholarshipLanguage);
				}

			});
			dbLanguages.removeIf(e -> !Utils.containsIgnoreCase(scholarshipDto.getLanguages(), e.getName()));
		}

		List<ScholarshipCountry> dbCountries = scholarship.getScholarshipCountries();

		if (!CollectionUtils.isEmpty(scholarshipDto.getCountryNames())) {
			log.info("Scholarship country_names is not empty, start iterating data");

			scholarshipDto.getCountryNames().stream().forEach(countryName -> {

				Optional<ScholarshipCountry> existingCountryOptional = dbCountries.stream()
						.filter(e -> e.getCountryName().equalsIgnoreCase(countryName)).findAny();

				String scholarshipCountryId = null;
				ScholarshipCountry scholarshipCountry = new ScholarshipCountry();
				if (existingCountryOptional.isPresent()) {
					scholarshipCountry = existingCountryOptional.get();
					scholarshipCountryId = scholarshipCountry.getId();
				}
				scholarshipCountry.setId(scholarshipCountryId);
				scholarshipCountry.setCountryName(countryName);
				scholarshipCountry.setAuditFields(userId);
				scholarshipCountry.setScholarship(finalScholarship);
				if (StringUtils.isEmpty(scholarshipCountryId)) {
					dbCountries.add(scholarshipCountry);
				}

			});
			dbCountries.removeIf(e -> !Utils.containsIgnoreCase(scholarshipDto.getCountryNames(), e.getCountryName()));
		}

		List<ScholarshipEligibleNationality> dbNationalitites = scholarship.getScholarshipEligibleNationalities();

		if (!CollectionUtils.isEmpty(scholarshipDto.getEligibleNationalities())) {
			log.info("Scholarship eligible nationalitites is not null, start iterating data");

			scholarshipDto.getEligibleNationalities().stream().forEach(countryName -> {

				Optional<ScholarshipEligibleNationality> existingNationalityOptional = dbNationalitites.stream()
						.filter(e -> e.getCountryName().equalsIgnoreCase(countryName)).findAny();

				String eligibleNationalityId = null;
				ScholarshipEligibleNationality eligibleNationality = new ScholarshipEligibleNationality();
				if (existingNationalityOptional.isPresent()) {
					eligibleNationality = existingNationalityOptional.get();
					eligibleNationalityId = eligibleNationality.getId();
				}
				eligibleNationality.setAuditFields(userId,
						StringUtils.isEmpty(eligibleNationalityId) ? null : eligibleNationality);
				eligibleNationality.setCountryName(countryName);
				eligibleNationality.setScholarship(finalScholarship);
				if (StringUtils.isEmpty(eligibleNationalityId)) {
					dbNationalitites.add(eligibleNationality);
				}
			});
			dbNationalitites.removeIf(
					e -> !Utils.containsIgnoreCase(scholarshipDto.getEligibleNationalities(), e.getCountryName()));
		}
		return scholarship;
	}

	@Transactional(rollbackOn = Throwable.class)
	public String saveScholarship(final String userId, final ScholarshipRequestDto scholarshipDto)
			throws ValidationException {

		log.debug("Inside saveScholarship() method");

		log.info("Calling DAO layer to save scholarship in DB");
		Scholarship scholarship = scholarshipDAO.saveScholarship(prepareModel(userId, scholarshipDto, null));

		log.info("Calling elastic search service to save data on elastic index");
		publishSystemEventHandler
				.syncScholarships(Arrays.asList(conversionProcessor.convertToScholarshipSyncDtoSyncDataEntity(scholarship)));

		return scholarship.getId();
	}

	@Transactional
	public ScholarshipResponseDto getScholarshipById(String userId, String id, boolean isReadableId) throws ValidationException {
		log.debug("Inside getScholarshipById() method");
		Scholarship scholarship = null;
		if (isReadableId) {
			scholarship = scholarshipDAO.findByReadableId(id);
		}else {
			scholarship = getScholarshipFomDb(id);
		}
		ScholarshipResponseDto scholarshipResponseDTO = createScholarshipResponseDtoFromModel(scholarship);
		if (scholarship.getCreatedBy().equals(userId)) {
			scholarshipResponseDTO.setHasEditAccess(true);
		} else {
			scholarshipResponseDTO.setHasEditAccess(false);
		}
		try {
			List<StorageDto> storageDTOList = storageHandler.getStorages(Arrays.asList(id), EntityTypeEnum.SCHOLARSHIP,
					Arrays.asList(EntitySubTypeEnum.COVER_PHOTO, EntitySubTypeEnum.LOGO, EntitySubTypeEnum.MEDIA));
			scholarshipResponseDTO.setMedia(storageDTOList);
		} catch (NotFoundException | InvokeException e) {
			log.error(e.getMessage());
		}

		return scholarshipResponseDTO;
	}

	@Transactional(rollbackOn = Throwable.class)
	public void updateScholarship(final String userId, final ScholarshipRequestDto scholarshipDto,
			final String scholarshipId) throws ValidationException {
		log.debug("Inside updateScholarship() method");

		log.info("Calling DAO layer to save scholarship in DB");
		Scholarship scholarship = scholarshipDAO.saveScholarship(prepareModel(userId, scholarshipDto, scholarshipId));

		log.info("Calling elastic search service to update existing scholarship data in DB");
		publishSystemEventHandler
				.syncScholarships(Arrays.asList(conversionProcessor.convertToScholarshipSyncDtoSyncDataEntity(scholarship)));
	}

	@Transactional(rollbackOn = Throwable.class)
	public String saveOrUpdateBasicScholarship(final String userId, final ScholarshipRequestDto scholarshipDto,
			final String scholarshipId) throws ValidationException {
		log.debug("Inside saveOrUpdateBasicScholarship() method");
		Scholarship scholarship = null;
		if (!StringUtils.isEmpty(scholarshipId)) {
			scholarship = getScholarshipFomDb(scholarshipId);
		} else {
			scholarship = new Scholarship();
		}

		BeanUtils.copyProperties(scholarshipDto, scholarship);
		if (StringUtils.isEmpty(scholarshipId)) {
			readableIdProcessor.setReadableIdForScholarship(scholarship);
		}
		scholarship.setAuditFields(userId);

		log.info("Calling DAO layer to save/update scholarship in DB");
		scholarship = scholarshipDAO.saveScholarship(scholarship);

		log.info("Calling elastic search service to update existing scholarship data in DB");
		publishSystemEventHandler
				.syncScholarships(Arrays.asList(conversionProcessor.convertToScholarshipSyncDtoSyncDataEntity(scholarship)));
		return scholarship.getId();
	}

	@Transactional
	public PaginationResponseDto getScholarshipList(final Integer pageNumber, final Integer pageSize,
			final String countryName, final String instituteId, final String searchKeyword) {
		log.debug("Inside getScholarshipList() method");

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<Scholarship> scholarshipsPage = scholarshipDAO.getScholarshipList(countryName, instituteId, searchKeyword,
				pageable);

		List<ScholarshipResponseDto> scholarshipResponseDTOs = scholarshipsPage.getContent().stream()
				.map(this::createScholarshipResponseDtoFromModel).collect(Collectors.toList());

		try {
			List<StorageDto> storageDTOList = storageHandler.getStorages(
					scholarshipResponseDTOs.stream().map(ScholarshipResponseDto::getId).collect(Collectors.toList()),
					EntityTypeEnum.SCHOLARSHIP,
					Arrays.asList(EntitySubTypeEnum.COVER_PHOTO, EntitySubTypeEnum.LOGO, EntitySubTypeEnum.MEDIA));
			scholarshipResponseDTOs.stream().forEach(e -> e.setMedia(storageDTOList.stream()
					.filter(f -> e.getId().equals(f.getEntityId())).collect(Collectors.toList())));
		} catch (NotFoundException | InvokeException e) {
			log.error(e.getMessage());
		}

		return PaginationUtil.calculatePaginationAndPrepareResponse(PaginationUtil.getStartIndex(pageNumber, pageSize),
				pageSize, ((Long) scholarshipsPage.getTotalElements()).intValue(), scholarshipResponseDTOs);
	}

	@Transactional(rollbackOn = Throwable.class)
	public void deleteScholarship(final String userId, final String scholarshipId)
			throws ValidationException, NotFoundException, InvokeException {
		log.debug("Inside deleteScholarship() method");
		Scholarship scholarship = getScholarshipFomDb(scholarshipId);
		if (!scholarship.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("scolarship-processor.no_access.deleted",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("scolarship-processor.no_access.deleted"));
		}
		scholarshipDAO.deleteScholarship(scholarshipId);
		storageHandler.deleteStorageBasedOnEntityId(scholarshipId);
	}

	public List<ScholarshipLevelCountDto> getScholarshipCountByLevel() {
		log.debug("Inside getScholarshipCountByLevelId() method");
		return scholarshipDAO.getScholarshipCountGroupByLevel();
	}

	@Transactional
	public List<ScholarshipResponseDto> getScholarshipByIds(List<String> scholarshipIds) {
		log.debug("Inside getScholarshipByIds() in ScholarshipProcessor method");
		List<Scholarship> scholarships = scholarshipDAO.getScholarshipByIds(scholarshipIds);
		return scholarships.stream().map(this::createScholarshipResponseDtoFromModel).collect(Collectors.toList());
	}

	private ScholarshipResponseDto createScholarshipResponseDtoFromModel(Scholarship scholarship) {
		ScholarshipResponseDto responseDto = modelMapper.map(scholarship, ScholarshipResponseDto.class);
		responseDto.setEligibleNationalities(scholarship.getScholarshipEligibleNationalities().stream()
				.map(ScholarshipEligibleNationality::getCountryName).collect(Collectors.toList()));

		responseDto.setCountryNames(scholarship.getScholarshipCountries().stream()
				.map(ScholarshipCountry::getCountryName).collect(Collectors.toList()));

		responseDto.setLanguages(scholarship.getScholarshipLanguages().stream().map(ScholarshipLanguage::getName)
				.collect(Collectors.toList()));
		return responseDto;
	}
	
	@Transactional
	public void changeScholarshipStatus(String userId, String scholarshipId, boolean status) {
		log.info("Inside ScholarhipProcessor.changeScholarshipStatus method");

		Scholarship existingScholarship = getScholarshipFomDb(scholarshipId);

		boolean existingStatus = false;
		if (!ObjectUtils.isEmpty(existingScholarship.getIsActive())) {
			existingStatus = existingScholarship.getIsActive().booleanValue();
		}
		log.info("Update scholarship {} status {}", scholarshipId, status);
		if (ObjectUtils.isEmpty(existingScholarship.getIsActive()) || status != existingStatus) {
			existingScholarship.setIsActive(status);
			existingScholarship.setUpdatedBy(userId);
			existingScholarship.setUpdatedOn(new Date());
			scholarshipDAO.saveScholarship(existingScholarship);

			log.info("Calling elastic search service to save data on elastic index");
			publishSystemEventHandler.syncScholarships(Arrays.asList(
					conversionProcessor.convertToScholarshipSyncDtoSyncDataEntity(existingScholarship)));
		}
	}
	
	private Scholarship getScholarshipFomDb(String scholarshipId) throws ValidationException {
		Optional<Scholarship> scholarshipOptional = scholarshipDAO.getScholarshipById(scholarshipId);
		if (scholarshipOptional.isPresent()) {
			return scholarshipOptional.get();
		} else {
			log.error(messageTranslator.toLocale("scolarship-processor.not_found.id"), scholarshipId);
			throw new ValidationException( messageTranslator.toLocale("scolarship-processor.not_found.id", scholarshipId));
		}
	}
	
}
