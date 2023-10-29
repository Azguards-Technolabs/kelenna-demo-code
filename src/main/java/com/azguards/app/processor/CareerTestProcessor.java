package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.CareerTestResult;
import com.azguards.app.bean.RelatedCareer;
import com.azguards.app.dao.CareerTestDao;
import com.azguards.app.dao.CareerTestResultDao;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.CareerJobDto;
import com.azguards.app.dto.CareerJobSkillDto;
import com.azguards.app.dto.CareerJobSubjectDto;
import com.azguards.app.dto.CareerJobTypeDto;
import com.azguards.app.dto.CareerJobWorkingStyleDto;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.JobDto;
import com.azguards.app.dto.RelatedCareerDto;
import com.azguards.app.enumeration.CareerTestEntityType;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CareerTestProcessor {

	@Autowired
	private CareerTestDao careerTestDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CareerTestResultDao careerTestResultDao;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	public PaginationResponseDto<List<CareerJobSkillDto>> getCareerJobSkills(String userId, Integer pageNumber,
			Integer pageSize, String levelId, String jobId) {
		log.debug("Inside getCareerJobSkills() method");
		List<CareerJobSkillDto> careerJobSkillDtos = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting career job skills from DB for levelId ", levelId);

		Page<CareerJobSkill> careerJobSkillsFromDB = careerTestDao.getCareerJobSkills(levelId, jobId, pageable);
		if (!CollectionUtils.isEmpty(careerJobSkillsFromDB.getContent())) {
			log.info("Career JobSkills fetched from DB, start iterating data to make final response");
			List<String> favouriteIds = getfavaouriteIds(userId, CareerTestEntityType.JOB_SKILL);
			careerJobSkillsFromDB.getContent().stream().forEach(careerJobSkill -> {

				CareerJobSkillDto careerJobSkillDto = new CareerJobSkillDto(careerJobSkill.getId(),
						careerJobSkill.getSkill(), careerJobSkill.getDescription(),
						careerJobSkill.getCareerJobs().getId(),
						favouriteIds.stream().anyMatch(e -> e.equals(careerJobSkill.getId())));
				careerJobSkillDtos.add(careerJobSkillDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobSkillsFromDB, careerJobSkillDtos);
	}

	public PaginationResponseDto<List<CareerJobWorkingStyleDto>> getCareerJobWorkingStyles(String userId,
			List<String> jobIds, Integer pageNumber, Integer pageSize) {
		log.debug("Inside getCareerJobWorkingStyles() method");
		List<CareerJobWorkingStyleDto> careerJobWorkingStyleDtos = new ArrayList<>();

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting career job workingStyles from DB");
		Page<CareerJobWorkingStyle> careerJobWorkingStyles = careerTestDao.getCareerJobWorkingStyle(jobIds, pageable);
		if (!CollectionUtils.isEmpty(careerJobWorkingStyles.getContent())) {
			log.info("Career Working Style fetched from DB, start iterating data");
			List<String> favouriteIds = getfavaouriteIds(userId, CareerTestEntityType.WORKING_STYLE);
			careerJobWorkingStyles.getContent().stream().forEach(careerJobWorkingStyle -> {
				log.info("Start adding values in DTO class");
				CareerJobWorkingStyleDto careerJobWorkingStyleDto = new CareerJobWorkingStyleDto(
						careerJobWorkingStyle.getId(), careerJobWorkingStyle.getWorkStyle(),
						careerJobWorkingStyle.getCareerJobs().getId(),
						favouriteIds.stream().anyMatch(e -> e.equals(careerJobWorkingStyle.getId())));
				careerJobWorkingStyleDtos.add(careerJobWorkingStyleDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobWorkingStyles, careerJobWorkingStyleDtos);
	}

	public PaginationResponseDto<List<CareerJobSubjectDto>> getCareerJobSubjects(String userId, List<String> jobIds,
			Integer pageNumber, Integer pageSize) {
		log.debug("Inside getCareerJobSubjects() method");
		List<CareerJobSubjectDto> careerJobSubjectDtos = new ArrayList<>();

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting career job Subjects from DB.");
		Page<CareerJobSubject> careerJobSubjects = careerTestDao.getCareerJobSubject(jobIds, pageable);
		if (!CollectionUtils.isEmpty(careerJobSubjects.getContent())) {
			log.info("Career JobSubjects fetched from DB, start iterating data");
			List<String> favouriteIds = getfavaouriteIds(userId, CareerTestEntityType.JOB_SUBJECT);
			careerJobSubjects.getContent().stream().forEach(careerJobSubject -> {
				log.info("Start adding values in DTO class");
				CareerJobSubjectDto careerJobSubjectDto = new CareerJobSubjectDto(careerJobSubject.getId(),
						careerJobSubject.getSubject(), careerJobSubject.getCareerJobs().getId(),
						favouriteIds.stream().anyMatch(e -> e.equals(careerJobSubject.getId())));
				careerJobSubjectDtos.add(careerJobSubjectDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobSubjects, careerJobSubjectDtos);
	}

	public PaginationResponseDto<List<CareerJobTypeDto>> getCareerJobTypes(String userId, List<String> jobIds,
			Integer pageNumber, Integer pageSize) {
		log.debug("Inside getCareerJobTypes() method");
		List<CareerJobTypeDto> careerJobTypeDtos = new ArrayList<>();

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting career job types from DB");
		Page<CareerJobType> careerJobTypesPage = careerTestDao.getCareerJobType(jobIds, pageable);

		List<CareerJobType> careerJobTypes = careerJobTypesPage.getContent();
		if (!CollectionUtils.isEmpty(careerJobTypes)) {
			log.info("Career Job Types fetched from DB, start iterating data");
			List<String> favouriteIds = getfavaouriteIds(userId, CareerTestEntityType.JOB_TYPE);
			careerJobTypes.stream().forEach(careerJob -> {

				List<String> careerjobIds = careerTestDao.getCareerJobIdsByJobTypeId(careerJob.getId()).stream()
						.map(e -> e.getJobId()).collect(Collectors.toList());

				CareerJobTypeDto careerJobTypeDto = new CareerJobTypeDto(careerJob.getId(), careerJob.getJobType(),
						careerjobIds, favouriteIds.stream().anyMatch(e -> e.equals(careerJob.getId())));
				careerJobTypeDtos.add(careerJobTypeDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobTypesPage, careerJobTypeDtos);
	}

	public PaginationResponseDto<List<JobDto>> getCareerJobsByName(String userId, String name, Integer pageNumber,
			Integer pageSize) {
		log.debug("Inside getCareerJobs() method");
		List<JobDto> jobDtos = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

		Page<CareerJob> careerJobs = careerTestDao.getCareerJobByName(name, pageable);
		log.info("Extracting total Count of jobs from DB");
		if (!CollectionUtils.isEmpty(careerJobs.getContent())) {
			log.info("Career Jobs fetched from DB, start iterating data");
			careerJobs.getContent().stream().forEach(careerJob -> {
				jobDtos.add(new JobDto(careerJob.getId(), careerJob.getJob(), careerJob.getJobDescription()));
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobs, jobDtos);
	}

	public PaginationResponseDto<List<CareerJobDto>> getCareerJobs(String userId, List<String> jobIds,
			Integer pageNumber, Integer pageSize) {
		log.debug("Inside getCareerJobs() method");
		List<CareerJobDto> careerJobDtos = new ArrayList<>();

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting career jobs from DB");
		Page<CareerJob> careerJobs = careerTestDao.getCareerJob(jobIds, pageable);
		log.info("Extracting total Count of jobs from DB");
		if (!CollectionUtils.isEmpty(careerJobs.getContent())) {
			log.info("Career Jobs fetched from DB, start iterating data");
			List<String> favouriteIds = getfavaouriteIds(userId, CareerTestEntityType.JOB);
			careerJobs.getContent().stream().forEach(careerJob -> {
				CareerJobDto careerJobDto = modelMapper.map(careerJob, CareerJobDto.class);
				careerJobDto.setLastSelected(favouriteIds.stream().anyMatch(e -> e.equals(careerJob.getId())));
				careerJobDtos.add(careerJobDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(careerJobs, careerJobDtos);
	}

	public PaginationResponseDto<List<RelatedCareerDto>> getRealtedCareers(List<String> careerIds, Integer pageNumber,
			Integer pageSize) {
		log.debug("Inside getRealtedCareers() method");
		List<RelatedCareerDto> realtedCareerDtos = new ArrayList<>();

		log.info("Calculating Pageable size based on pageNumber and pageSize");
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting related careers from DB");
		Page<RelatedCareer> relatedCarrersPage = careerTestDao.getRelatedCareers(careerIds, pageable);
		List<RelatedCareer> realtedCarrers = relatedCarrersPage.getContent();
		if (!CollectionUtils.isEmpty(realtedCarrers)) {
			log.info("Career Jobs fetched from DB, start iterating data");
			realtedCarrers.stream().forEach(careerJob -> {
				RelatedCareerDto careerJobDto = new RelatedCareerDto(careerJob.getId(), careerJob.getRelatedCareer(),
						careerJob.getCareers().getId());
				realtedCareerDtos.add(careerJobDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(relatedCarrersPage, realtedCareerDtos);
	}

	public PaginationResponseDto getRelatedCourseBasedOnCareerTest(List<String> jobIds, Integer pageNumber,
			Integer pageSize) {
		log.debug("Inside getRelatedCourseBasedOnCareerTest() method");
		List<String> courseSearchkeyword = new ArrayList<>();
		log.info("Extracting job courseSearchKeyword from DB to match possible courses");
		List<CareerJobCourseSearchKeyword> careerJobCourseSearchKeywords = careerTestDao
				.getCareerJobCourseSearchKeyword(jobIds);
		Long startIndex = Long.valueOf(pageNumber - 1);
		log.info("Calculating Pageable size based on pageNumber nad pageSize");

		if (!CollectionUtils.isEmpty(careerJobCourseSearchKeywords)) {
			log.info("Career courseSearchKeyword fetched, iterating data to extract search keywords");
			careerJobCourseSearchKeywords.stream().forEach(careerJobCourseSearchKeyword -> {
				log.info("Adding searchKeyword in list, to fetch all possible course having searchKeyword from DB");
				courseSearchkeyword.add(careerJobCourseSearchKeyword.getCourseSearchKeyword());
			});
		}
		log.info("Extracting total count of courses based on courseSearchKeywords");
		Integer totalCount = courseDao.getRelatedCourseBasedOnCareerTestCount(courseSearchkeyword);
		log.info("Extracting courses data based on job courseSearchKeywords from DB");
		List<CourseResponseDto> careerJobRelatedCourseDtos = courseDao
				.getRelatedCourseBasedOnCareerTest(courseSearchkeyword, startIndex.intValue(), pageSize);
		log.info("Calculating pagination based on startIndex, pageSize and pageNumber");
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, pageSize, totalCount);
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto(careerJobRelatedCourseDtos,
				Long.valueOf(totalCount), paginationUtilDto.getPageNumber(), paginationUtilDto.isHasPreviousPage(),
				paginationUtilDto.isHasNextPage(), paginationUtilDto.getTotalPages());
		return paginationResponseDto;
	}

	public CareerJobDto getCareerJobById(String jobId) throws NotFoundException {
		Optional<CareerJob> careerJob = careerTestDao.getCareerJob(jobId);
		if (!careerJob.isPresent()) {
			log.debug(messageTranslator.toLocale("career_test.job.notfound" , jobId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("career_test.job.notfound" , jobId));
		}
		return modelMapper.map(careerJob.get(), CareerJobDto.class);
	}

	private List<String> getfavaouriteIds(String userId, CareerTestEntityType entityType) {
		log.info("inside CareerTestProcessor.getfavaouriteIds");
		List<String> favouriteIds = new ArrayList<>();
		try {
			favouriteIds = careerTestResultDao.findByUserIdAndEntityType(userId, entityType).stream()
					.map(CareerTestResult::getEntityId).toList();
		} catch (Exception e) {
			log.error("exception::: {}", e);
		}
		return favouriteIds;
	}
}
