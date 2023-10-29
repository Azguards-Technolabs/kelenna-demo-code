package com.azguards.app.controller.v1;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.bean.InstituteCategoryType;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseScholarshipAndFacultyCountDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.InstituteDomesticRankingHistoryDto;
import com.azguards.app.dto.InstituteFacultyDto;
import com.azguards.app.dto.InstituteFilterDto;
import com.azguards.app.dto.InstituteGetRequestDto;
import com.azguards.app.dto.InstituteRequestDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.app.dto.InstituteTypeDto;
import com.azguards.app.dto.InstituteWorldRankingHistoryDto;
import com.azguards.app.dto.LatLongDto;
import com.azguards.app.dto.NearestInstituteDTO;
import com.azguards.app.dto.PaginationDto;
import com.azguards.app.dto.ValidList;
import com.azguards.app.endpoint.InstituteInterface;
import com.azguards.app.processor.InstituteProcessor;
import com.azguards.app.processor.InstituteTypeProcessor;
import com.azguards.app.processor.ReadableIdProcessor;
import com.azguards.app.processor.TimingProcessor;
import com.azguards.app.util.CommonUtil;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.dto.institute.TimingDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.BusinessAccountType;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.enumeration.InstituteType;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.common.lib.util.Utils;
import com.azguards.common.lib.util.ValidationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("instituteControllerV1")
public class InstituteController implements InstituteInterface {

	@Autowired
	private InstituteProcessor instituteProcessor;

	@Autowired
	private InstituteTypeProcessor instituteTypeProcessor;

	@Autowired
	private StorageHandler storageHandler;
	
	@Autowired
	private TimingProcessor timingProcessor;
	
	@Autowired
	private ReadableIdProcessor readableIdProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Override
	public ResponseEntity<?> saveInstituteType(final InstituteTypeDto instituteTypeDto) throws Exception {
		log.info("Start process to save new institute types in DB");
		instituteTypeProcessor.addUpdateInstituteType(instituteTypeDto);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.type.added"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getInstituteTypeByCountry(String countryName) throws Exception {
		log.info("Start process to fetch instituteType from DB for countryName = [}",countryName);
		List<InstituteTypeDto> listOfInstituteTypes = instituteTypeProcessor.getInstituteTypeByCountryName(countryName);
		return new GenericResponseHandlers.Builder().setData(listOfInstituteTypes).setMessage(messageTranslator.toLocale("institute.type.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteType() throws Exception {
		log.info("Start process to fetch instituteType");
		List<InstituteType> listOfInstituteTypes = instituteTypeProcessor.getInstituteTypes();
		return new GenericResponseHandlers.Builder().setData(listOfInstituteTypes).setMessage(messageTranslator.toLocale("institute.type.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> instituteSearch(final CourseSearchDto request) throws Exception {
		log.info("Start process to searching institute in DB");
		return getInstitutesBySearchFilters(request, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public ResponseEntity<?> instituteSearch(final Integer pageNumber, final Integer pageSize, final List<String> countryNames, 
			final List<String> facultyIds,final List<String> levelIds, final String cityName, final List<String> instituteType, final Boolean isActive,
			final Date updatedOn, final Integer fromWorldRanking, final Integer toWorldRanking, final String sortByField, final String sortByType, 
			final String searchKeyword, final Double latitutde, final Double longitude) throws ValidationException {
		log.info("Start process to searching institute based on passed filters");
		CourseSearchDto courseSearchDto = new CourseSearchDto();
		courseSearchDto.setCountryNames(countryNames);
		courseSearchDto.setFacultyIds(facultyIds);
		courseSearchDto.setLevelIds(levelIds);
		courseSearchDto.setPageNumber(pageNumber);
		courseSearchDto.setMaxSizePerPage(pageSize);
		courseSearchDto.setLatitude(latitutde);
		courseSearchDto.setLongitude(longitude);
		return getInstitutesBySearchFilters(courseSearchDto, sortByField, sortByType, searchKeyword, cityName, instituteType, isActive, updatedOn,
				fromWorldRanking, toWorldRanking);
	}

	private ResponseEntity<?> getInstitutesBySearchFilters(final CourseSearchDto request, final String sortByField, final String sortByType,
			final String searchKeyword, final String cityId, final List<String> instituteTypeId, final Boolean isActive, final Date updatedOn,
			final Integer fromWorldRanking, final Integer toWorldRanking) throws ValidationException {
		log.debug("Inside getInstitutesBySearchFilters() method");
		Long startIndex = PaginationUtil.getStartIndex(request.getPageNumber(), request.getMaxSizePerPage());
		log.info("Calling DAO layer to search institutes based on passed filters");
		List<InstituteResponseDto> instituteList = instituteProcessor.getAllInstitutesByFilter(request, sortByField, sortByType, searchKeyword, startIndex.intValue(),
				cityId, instituteTypeId, isActive, updatedOn, fromWorldRanking, toWorldRanking);
		if(!CollectionUtils.isEmpty(instituteList)) {
			log.info("Institutes fetched from DB, now iterating data to call Storage service");
			instituteList.stream().forEach(instituteResponseDto -> {
				try {
					log.info("Calling Storage service to get imageCategories for Institute");
					List<StorageDto> storageDTOList = storageHandler.getStorages(instituteResponseDto.getId(), EntityTypeEnum.INSTITUTE,EntitySubTypeEnum.IMAGES);
					instituteResponseDto.setStorageList(storageDTOList);
				} catch (NotFoundException | InvokeException e) {
					log.error("Error invoking Storage service having exception = {}",e);
				}
				log.info("fetching instituteTiming from DB for instituteId ={}", instituteResponseDto.getId());
				TimingDto instituteTimingResponseDto = timingProcessor.getTimingResponseDtoByInstituteId(instituteResponseDto.getId());
				instituteResponseDto.setInstituteTiming(instituteTimingResponseDto);
				if(!ObjectUtils.isEmpty(request.getLatitude()) && !ObjectUtils.isEmpty(request.getLongitude()) && 
						!ObjectUtils.isEmpty(instituteResponseDto.getLatitude()) && !ObjectUtils.isEmpty(instituteResponseDto.getLongitude())) {
					log.info("Calculating distance between institutes lat and long and user lat and long");
					double distance = CommonUtil.getDistanceFromLatLonInKm(request.getLatitude(), request.getLongitude(), 
							instituteResponseDto.getLatitude(), instituteResponseDto.getLongitude());
					instituteResponseDto.setDistance(distance);
				}
			});
		}
		log.info("Fetching totalCount of institutes from DB based on passed filters to calculate pagination");
		int totalCount = instituteProcessor.getCountOfInstitute(request, searchKeyword, cityId, instituteTypeId, isActive, updatedOn, fromWorldRanking,
				toWorldRanking);
		log.info("Calling pagination class to calculate pagination based on startIndex, pageSixe and totalCount of institutes");
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, request.getMaxSizePerPage(), totalCount);
		log.info("Adding values in paginationResponse DTO and returning final response");
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setResponse(instituteList);
		paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
		paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
		paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
		paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
		paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
		return new GenericResponseHandlers.Builder().setData(paginationResponseDto).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getAllRecommendedInstitutes(final CourseSearchDto request) throws Exception {
		log.debug("Inside getAllRecommendedInstitutes() method");
		List<InstituteResponseDto> instituteResponse = new ArrayList<>();
		Boolean showMore = false;
		Integer maxCount = 0, totalCount = 0;
		Long startIndex = PaginationUtil.getStartIndex(request.getPageNumber(), request.getMaxSizePerPage());
		PaginationUtil.validateMaxResultSize(request.getPageNumber());
		if (null == request.getUserId()) {
			log.error(messageTranslator.toLocale("institute.user_id.required",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute.user_id.required")


);
		}
		log.info("Calling DAO layer to get all institutes based on filters");
		List<InstituteResponseDto> instituteResponseDtoList = instituteProcessor.getAllInstitutesByFilter(request, null, null, null,
				startIndex.intValue(), null, null, null, null, null, null);
		if(!CollectionUtils.isEmpty(instituteResponseDtoList)) {
			log.info("Filtered institutes coming from DB, hence start iterating");
			totalCount = instituteResponseDtoList.get(0).getTotalCount();
			maxCount = instituteResponseDtoList.size();
			instituteResponseDtoList.stream().forEach(instituteResponseDto -> {
				try {
					log.info("Invoking storage service to fetch images for institutes");
					List<StorageDto> storageDTOList = storageHandler.getStorages(instituteResponseDto.getId(), EntityTypeEnum.INSTITUTE,EntitySubTypeEnum.IMAGES);
					instituteResponseDto.setStorageList(storageDTOList);
				} catch (NotFoundException | InvokeException e) {
					log.error("Error invoking Storage service having exception {}", e);
				}
				log.info("fetching instituteTiming from DB for instituteId = {}", instituteResponseDto.getId());
				TimingDto instituteTimingResponseDto = timingProcessor.getTimingResponseDtoByInstituteId(instituteResponseDto.getId());
				instituteResponseDto.setInstituteTiming(instituteTimingResponseDto);
				instituteResponse.add(instituteResponseDto);
			});
		}
		if (request.getMaxSizePerPage() == maxCount) {
			log.info("if maxSize and max count are equal then showMore is visible");
			showMore = true;
		}
		log.info("Adding values in PaginationDTO class and return in final response");
		PaginationDto paginationDto = new PaginationDto(totalCount, showMore,instituteResponse);
		return new GenericResponseHandlers.Builder().setData(paginationDto).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteByCityName(final String cityName) throws Exception {
		log.info("Start process to fetch institutes from DB for cityName = {}", cityName);
		List<InstituteResponseDto> institutes = instituteProcessor.getInstituteByCityName(cityName);
		return new GenericResponseHandlers.Builder().setData(institutes).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> save(@Valid final ValidList<InstituteRequestDto> institutes) throws Exception {
		log.info("Start process to add new Institues in DB");
		institutes.stream().forEach(e->{
			validateInstituteType(e,e.getInstituteType());
		});	
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.created"))
				.setData(instituteProcessor.saveInstitute(institutes))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> update(final String userId, final String instituteId, final InstituteRequestDto institute) throws Exception {
		log.info("Start process to update existing Institue having instituteId = {}",instituteId);
		validateInstituteRequest(institute);
		instituteProcessor.updateInstitute(userId, instituteId, institute);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.updated"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getAllInstitute(final Integer pageNumber, final Integer pageSize) throws Exception {
		log.info("Start process to fetch all Institutes from DB based on pagination");
		PaginationResponseDto paginationResponseDto = instituteProcessor.getAllInstitute(pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(paginationResponseDto).setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<?> getAllInstitute(final String searchKey, final Integer pageNumber, final Integer pageSize) throws Exception {
		log.info("Start process to fetch all Institutes from DB based on pagination and searchKeyword");
		PaginationResponseDto paginationResponseDto = instituteProcessor.autoSearch(pageNumber, pageSize, searchKey);
		return new GenericResponseHandlers.Builder().setData(paginationResponseDto).setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<?> get(final String userId, final String instituteId, final boolean isReadableId)
			throws Exception {
		log.info("Start process to fetch Institutes from DB for instituteId = {}", instituteId);
		InstituteRequestDto instituteRequestDtos = instituteProcessor.getById(userId, instituteId, isReadableId);
		return new GenericResponseHandlers.Builder().setData(instituteRequestDtos)
				.setMessage(messageTranslator.toLocale("institute.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> searchInstitute(final String searchText) throws Exception {
		log.info("Start process to search Institutes from DB for searchKeyword = {}", searchText);
		List<InstituteGetRequestDto> instituteGetRequestDtos = instituteProcessor.searchInstitute(searchText);
		return new GenericResponseHandlers.Builder().setData(instituteGetRequestDtos).setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	@Override
	public ResponseEntity<?> instituteFilter(final InstituteFilterDto instituteFilterDto) throws Exception {
		log.info("Start process to fetch institutes from DB based on passed filters");
		PaginationResponseDto instituteResponseDto = instituteProcessor.instituteFilter(instituteFilterDto);
		return new GenericResponseHandlers.Builder().setData(instituteResponseDto)
				.setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getAllCategoryType() throws Exception {
		log.info("Start process to fetch all InstituteCategories from DB");
		List<InstituteCategoryType> categoryTypes = instituteProcessor.getAllCategories();
		return new GenericResponseHandlers.Builder().setData(categoryTypes)
				.setMessage(messageTranslator.toLocale("institute.category-type.list.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getAllInstituteType() throws Exception {
		log.info("Start process to fetch all InstituteTypes from DB");
		List<InstituteTypeDto> instituteTypes = instituteTypeProcessor.getAllInstituteType();
		return new GenericResponseHandlers.Builder().setData(instituteTypes)
				.setMessage(messageTranslator.toLocale("institute.type.list.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> delete(final String instituteId) throws ValidationException {
		log.info("Start process to inactive existing Institute for instituteId = {}",instituteId);
		instituteProcessor.deleteInstitute(instituteId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.deleted")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteImage(final String instituteId) throws Exception {
		log.info("Start process to fetch all InstituteImages for instituteId = {}", instituteId);
		List<StorageDto> storageDTOList = storageHandler.getStorages(instituteId, EntityTypeEnum.INSTITUTE,EntitySubTypeEnum.IMAGES);
		return new GenericResponseHandlers.Builder().setData(storageDTOList).setMessage(messageTranslator.toLocale("institute.image.list.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getTotalCourseForInstitute(final String instituteId) throws ValidationException {
		log.info("Start process to get total count of courses for institute having instituteId = {}", instituteId);
		Integer courseCount = instituteProcessor.getTotalCourseCountForInstitute(instituteId);
		return new GenericResponseHandlers.Builder().setData(courseCount).setMessage(messageTranslator.toLocale("institute.course.count.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getHistoryOfDomesticRanking(final String instituteId) throws ValidationException {
		log.info("Start process to get history of domesticRanking for institute having instituteId = {}", instituteId);
		List<InstituteDomesticRankingHistoryDto> instituteDomesticRankingHistory = instituteProcessor.getHistoryOfDomesticRanking(instituteId);
		return new GenericResponseHandlers.Builder().setData(instituteDomesticRankingHistory).setMessage(messageTranslator.toLocale("institute.domestic-ranking.history.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getHistoryOfWorldRanking(final String instituteId) throws ValidationException {
		log.info("Start process to get history of worldRanking for institute having instituteId = {}", instituteId);
		List<InstituteWorldRankingHistoryDto> instituteWorldRankingHistory = instituteProcessor.getHistoryOfWorldRanking(instituteId);
		return new GenericResponseHandlers.Builder().setData(instituteWorldRankingHistory).setMessage(messageTranslator.toLocale("institute.world-ranking.history.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getDomesticRanking(final List<String> courseIds) throws ValidationException {
		log.info("Start process to get domesticRanking for courses");
		Map<String, Integer> instituteIdDomesticRanking = instituteProcessor.getDomesticRanking(courseIds);
		return new GenericResponseHandlers.Builder().setData(instituteIdDomesticRanking).setMessage(messageTranslator.toLocale("domestic-ranking.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getNearestInstituteList(final AdvanceSearchDto courseSearchDto) throws Exception {
		log.info("Start process to fetch nearest Institutes from DB based on filters");
		NearestInstituteDTO nearestInstituteDTOs = instituteProcessor.getNearestInstituteList(courseSearchDto);
		return new GenericResponseHandlers.Builder().setData(nearestInstituteDTOs).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getDistinctInstututes(final Integer pageNumber, final Integer pageSize, final String name) throws Exception {
		log.debug("Inside getDistinctInstututes() method");
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		log.info("Getting total count of institute having instituteName = {}", name);
		int totalCount = instituteProcessor.getDistinctInstituteCount(name);
		log.info("Fetching distinct institutes from DB based on pagination for instituteName = {}", name);
		List<InstituteResponseDto> instituteList = instituteProcessor.getDistinctInstituteList(startIndex.intValue(), pageSize, name);
		log.info("Calculating pagination based on pageNumber, pageSize and totalCount");
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, pageSize, totalCount);
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setResponse(instituteList);
		paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
		paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
		paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
		paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
		paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
		return new GenericResponseHandlers.Builder().setData(paginationResponseDto).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getBoundedInstituteList(final Integer pageNumber, final Integer pageSize, 
			List<LatLongDto> latLongDto) throws ValidationException {
		log.info("Start process to fetch bounded Institute list based on passed latitude and longitude");
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		NearestInstituteDTO nearestInstituteList = instituteProcessor.getInstitutesUnderBoundRegion(startIndex.intValue(), pageSize, latLongDto);
		return new GenericResponseHandlers.Builder().setData(nearestInstituteList)
				.setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK).create();
	}
	
	@GetMapping(value = "/institute/pageNumber/{pageNumber}/pageSize/{pageSize}/{countryName}")
	public ResponseEntity<?> getInstituteByCountryName(Integer pageNumber, Integer pageSize,
			String countryName) throws NotFoundException {
		NearestInstituteDTO instituteResponse = instituteProcessor.getInstituteByCountryName(countryName, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(instituteResponse)
				.setMessage(messageTranslator.toLocale("institute.list.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteFaculties(String instituteId) throws NotFoundException {
		List<InstituteFacultyDto> instituteFaculties = instituteProcessor.getInstituteFaculties(instituteId);
		return new GenericResponseHandlers.Builder().setData(instituteFaculties)
				.setMessage(messageTranslator.toLocale("institute.faculty.list.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteCourseScholarshipAndFacultyCount(String instituteId) throws NotFoundException {
		CourseScholarshipAndFacultyCountDto data = instituteProcessor
				.getInstituteCourseScholarshipAndFacultyCount(instituteId);
		return new GenericResponseHandlers.Builder().setData(data)
				.setMessage(messageTranslator.toLocale("institute.course-scholarship-faculty.count.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstitutesByIdList(List<String> instituteIds) throws NotFoundException, InvokeException, Exception {
		if (ObjectUtils.isEmpty(instituteIds)) {
			log.error(messageTranslator.toLocale("institute.null.id"),Locale.US);
			throw new ValidationException(messageTranslator.toLocale("institute.null.id"));
			
			
		}
		List<InstituteResponseDto> instituteList = instituteProcessor.getInstitutesByIdList(instituteIds);
		return new GenericResponseHandlers.Builder().setData(instituteList).setMessage(messageTranslator.toLocale("institute.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<Object> changeStatus(String userId, String instituteId, boolean status) {
		log.info("Inside InstituteController.changeStatus method");
		instituteProcessor.changeInstituteStatus(userId, instituteId, status);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("institute.status.updated"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> instituteExistsByReadableId(String readableId)
			throws ValidationException, NotFoundException, InvokeException, Exception {
		log.info("Inside InstituteController.instituteExistsByReadableId method");
		return new GenericResponseHandlers.Builder()
				.setData(readableIdProcessor.checkIfInstituteReadableIdExists(readableId))
				.setMessage(messageTranslator.toLocale("institute.retrieved")).setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getInstituteVerificationStatus(String instituteId) {
		log.info("Inside InstituteController.getInstituteVerificationStatus method");
		return new GenericResponseHandlers.Builder()
				.setData(instituteProcessor.getInstituteVerificationStatus(instituteId))
				.setMessage(messageTranslator.toLocale("institute.verification.info.retrieved")).setStatus(HttpStatus.OK).create();
	}	
	
	@Override
	public ResponseEntity<?> getMultipleInstituteVerificationStatus(List<String> instituteIds) {
		log.info("Inside InstituteController.getMultipleInstituteVerificationStatus method");
		return new GenericResponseHandlers.Builder()
				.setData(instituteProcessor.getMultipleInstituteVerificationStatus(instituteIds))
				.setMessage(messageTranslator.toLocale("institute.verification.info.retrieved")).setStatus(HttpStatus.OK).create();
	}

	private void validateInstituteType(InstituteRequestDto instituteRequest, List<String> instituteType) {
		List<String> schoolType = new ArrayList<String>(
				Arrays.asList("PRIMARY_SCHOOL", "SECONDARY_SCHOOL", "PRE_SCHOOL"));
		List<InstituteType> universityType = new ArrayList<>(Arrays.asList(InstituteType.UNIVERSITY_COLLEGE,InstituteType.SMALL_MEDIUM_PRIVATE_SCHOOL));
		instituteType.stream().forEach(type -> {
			if (BusinessAccountType.SCHOOL.toString().equals(instituteRequest.getBusinessAccountType())) {
				
				if (!schoolType.contains(type)) {
					log.error(messageTranslator.toLocale("institute_type_school_type.valid",schoolType.toString()));
					throw new ValidationException(messageTranslator.toLocale("institute_type_school_type.valid",schoolType.toString()));
				}
				validateInstituteRequest(instituteRequest);
				}else {
					if (!universityType.toString().contains(type)) {
	
					log.error(messageTranslator.toLocale("institute_type_university_type.valid",universityType.toString()));
					throw new ValidationException(messageTranslator.toLocale("institute_type_university_type.valid",universityType.toString()));
		 }
					validateInstituteRequest(instituteRequest);
					}
	
				
				});	
	
			
	}

   private void validateInstituteRequest(InstituteRequestDto instituteRequest) {
		instituteRequest.getInstituteType().stream().forEach(e->{
			ValidationUtil.validateInstituteType(e);
		});
	 }

	@Override
	public ResponseEntity<Object> verifyInstitutes(String userId, List<String> verifiedInstituteIds, boolean verified) {
		log.info("Class InstituteController method verifyInstitutes userId : {}, verifiedInstitutes : {}, verified : {}",
				userId, verifiedInstituteIds, verified);
		
		instituteProcessor.verifyInstitutes(userId, verifiedInstituteIds, verified);
		return new GenericResponseHandlers.Builder()
				.setStatus(HttpStatus.OK)
				.setMessage("Institutes verified successfully.")
				.create();
				
	}
}
