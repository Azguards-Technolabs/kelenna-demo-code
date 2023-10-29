package com.azguards.app.controller.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.bean.CourseKeywords;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseCountDto;
import com.azguards.app.dto.CourseDto;
import com.azguards.app.dto.CourseFilterDto;
import com.azguards.app.dto.CourseMobileDto;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.app.dto.NearestCoursesDto;
import com.azguards.app.dto.PaginationDto;
import com.azguards.app.endpoint.CourseInterface;
import com.azguards.app.message.MessageByLocaleService;
import com.azguards.app.processor.CourseKeywordProcessor;
import com.azguards.app.processor.CourseProcessor;
import com.azguards.app.processor.InstituteProcessor;
import com.azguards.app.service.UserRecommendationService;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.dto.transaction.UserMyCourseDto;
import com.azguards.common.lib.dto.user.UserInitialInfoDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.enumeration.SortingOnEnum;
import com.azguards.common.lib.enumeration.SortingTypeEnum;
import com.azguards.common.lib.enumeration.TransactionTypeEnum;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.handler.UserHandler;
import com.azguards.common.lib.handler.ViewTransactionHandler;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@RestController("courseControllerV1")
public class CourseController implements CourseInterface {
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private InstituteProcessor instituteProcessor;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private CourseKeywordProcessor courseKeywordProcessor;

	@Autowired
	private UserRecommendationService userRecommendationService;

	@Autowired
	private StorageHandler storageHandler;

	@Autowired
	private MessageByLocaleService messageByLocalService;

	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private ViewTransactionHandler viewTransactionHandler;

	public ResponseEntity<?> save(final String userId, String instituteId, final CourseRequest course)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException {
		log.info("Start process to save new course in DB");
		com.azguards.app.util.ValidationUtil.validateTimingDtoFromCourseRequest(course);
		String courseId = courseProcessor.saveOrUpdateCourse(userId, instituteId, course, null);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseId)
				.setMessage(messageTranslator.toLocale("course.added")).create();
	}

	@Override
	public ResponseEntity<?> saveBasicCourse(final String userId, String instituteId, final CourseRequest course)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException {
		log.info("Start process to save new course basic details in DB");
		String courseId = courseProcessor.saveOrUpdateBasicCourse(userId, instituteId, course, null);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseId)
				.setMessage(messageTranslator.toLocale("course.added")).create();
	}

	public ResponseEntity<?> update(final String userId, String instituteId, final CourseRequest course, final String id)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException {
		log.info("Start process to update existing course in DB");
		com.azguards.app.util.ValidationUtil.validateTimingDtoFromCourseRequest(course);
		String courseId = courseProcessor.saveOrUpdateCourse(userId, instituteId, course, id);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseId)
				.setMessage(messageTranslator.toLocale("course.updated")).create();
	}

	@Override
	public ResponseEntity<?> updateBasicCourse(final String userId, String instituteId, final CourseRequest course, final String id)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException {
		log.info("Start process to update existing basic course in DB");
		String courseId = courseProcessor.saveOrUpdateBasicCourse(userId,instituteId, course, id);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseId)
				.setMessage(messageTranslator.toLocale("course.updated")).create();
	}

	public ResponseEntity<?> getAllCourse(final Integer pageNumber, final Integer pageSize) throws Exception {
		log.info("Start process get all courses from DB based on pagination");
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseProcessor.courseFilter(pageNumber, pageSize, true, true, 
				SortingOnEnum.CREATED_ON , SortingTypeEnum.DESC, null, null, null, null, null, null))
				.setMessage(messageTranslator.toLocale("course.displayed")).create();
	}

	public ResponseEntity<?> autoSearch(final String searchKey, final Integer pageNumber, final Integer pageSize) throws Exception {
		log.info("Start process to search course based on pagination and searchkeyword");
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseProcessor.courseFilter(pageNumber, pageSize, true, true, 
				SortingOnEnum.CREATED_ON , SortingTypeEnum.DESC, null, null, null, null, null, searchKey))
				.setMessage(messageTranslator.toLocale("course.displayed")).create();
	}

	public ResponseEntity<?> delete(final String userId, final String id, final List<String> linkedCourseIds)
			throws ForbiddenException, NotFoundException {
		log.info("Start process to delete existing course from DB");
		courseProcessor.deleteCourse(userId, id, linkedCourseIds);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("course.deleted"))
				.create();
	}

	public ResponseEntity<?> searchCourse(final Integer pageNumber, final Integer pageSize, final List<String> countryIds, final String instituteId, 
			final List<String> facultyIds, final List<String> cityIds, final List<String> levelIds, final List<String> serviceIds, final Double minCost, 
			final Double maxCost, final Integer minDuration, final Integer maxDuration, final String courseName, final String currencyCode, 
			final String searchKeyword, final String sortBy, final boolean sortAsscending, final String userId, final String date) 
			throws ValidationException, InvokeException, NotFoundException {
		log.info("Start process to search course based on different passed filters");
		CourseSearchDto courseSearchDto = new CourseSearchDto();
		courseSearchDto.setCountryNames(countryIds);
		courseSearchDto.setInstituteId(instituteId);
		courseSearchDto.setFacultyIds(facultyIds);
		courseSearchDto.setCityNames(cityIds);
		courseSearchDto.setLevelIds(levelIds);
		courseSearchDto.setServiceIds(serviceIds);
		courseSearchDto.setMinCost(minCost);
		courseSearchDto.setMaxCost(maxCost);
		courseSearchDto.setMinDuration(minDuration);
		courseSearchDto.setMaxDuration(maxDuration);
		courseSearchDto.setCourseName(courseName);
		courseSearchDto.setPageNumber(pageNumber);
		courseSearchDto.setMaxSizePerPage(pageSize);
		courseSearchDto.setSortBy(sortBy);
		courseSearchDto.setCurrencyCode(currencyCode);
		courseSearchDto.setSortAsscending(sortAsscending);
		courseSearchDto.setUserId(userId);
		courseSearchDto.setDate(date);
		return courseSearch(courseSearchDto, searchKeyword);
	}

	public ResponseEntity<?> searchCourse(final String userId, final CourseSearchDto courseSearchDto) throws Exception {
		log.info("Start process to search course based on userID and passed filters");
		courseSearchDto.setUserId(userId);
		return courseSearch(courseSearchDto, null);
	}

	private ResponseEntity<?> courseSearch(final CourseSearchDto courseSearchDto, final String searchKeyword)
			throws ValidationException, InvokeException, NotFoundException {
		Long startIndex = PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage());
		
		log.info("Calling view transaction service to fetch user my course data");
		List<UserMyCourseDto> userMyCourseDtos = viewTransactionHandler.getUserMyCourseByEntityTypeAndTransactionType(courseSearchDto.getUserId(), 
				EntityTypeEnum.COURSE.name(), TransactionTypeEnum.FAVORITE.name());
		List<String> entityIds = userMyCourseDtos.stream().map(UserMyCourseDto::getEntityId).collect(Collectors.toList());
		
		List<CourseResponseDto> courseList = courseProcessor.getAllCoursesByFilter(courseSearchDto, startIndex.intValue(), courseSearchDto.getMaxSizePerPage(), 
					searchKeyword, entityIds);
		int totalCount = courseProcessor.getCountforNormalCourse(courseSearchDto, searchKeyword, entityIds);
		
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, courseSearchDto.getMaxSizePerPage(), totalCount);
		
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setResponse(courseList);
		paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
		paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
		paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
		paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
		paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).
				setMessage(messageTranslator.toLocale("course.displayed")).setData(paginationResponseDto).create();
	}

	public ResponseEntity<?> advanceSearch(final String userId, final String language, final AdvanceSearchDto courseSearchDto) throws Exception {
		log.info("Start process to advance search courses from DB");
		Long startIndex = PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(),courseSearchDto.getMaxSizePerPage());
		courseSearchDto.setUserId(userId);
		
		log.info("Calling view transaction service to fetch user my course data");
		List<UserMyCourseDto> userMyCourseDtos = viewTransactionHandler.getUserMyCourseByEntityTypeAndTransactionType(courseSearchDto.getUserId(), 
				EntityTypeEnum.COURSE.name(), TransactionTypeEnum.FAVORITE.name());
		List<String> entityIds = userMyCourseDtos.stream().map(UserMyCourseDto::getEntityId).collect(Collectors.toList());
		
		List<CourseResponseDto> courseList = courseProcessor.advanceSearch(courseSearchDto, entityIds);
		int totalCount = courseProcessor.getCountOfAdvanceSearch(courseSearchDto, entityIds);
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex,courseSearchDto.getMaxSizePerPage(), totalCount);
		
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setResponse(courseList);
		paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
		paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
		paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
		paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
		paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
		
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).
				setMessage(messageTranslator.toLocale("course.displayed")).setData(paginationResponseDto).create();
	}

	public ResponseEntity<Object> get(final String userId, final String id, final boolean isReadableId) throws Exception {
		log.info("Start process to get course from DB based on courseId");
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("course.displayed"))
				.setData(courseProcessor.getCourseById(userId, id, isReadableId)).create();
	}

	public ResponseEntity<?> getAllCourseByInstituteID(final String instituteId, final CourseSearchDto request) throws Exception {
		Map<String, Object> response = new HashMap<>();

		InstituteResponseDto instituteResponseDto = instituteProcessor.getInstituteByID(instituteId);
		if (null == instituteResponseDto) {
			throw new NotFoundException(messageTranslator.toLocale("institute.not_found.id",instituteId));
		}
		List<StorageDto> storageDTOList = storageHandler.getStorages(instituteResponseDto.getId(), EntityTypeEnum.INSTITUTE,EntitySubTypeEnum.IMAGES);
		instituteResponseDto.setStorageList(storageDTOList);

		List<CourseResponseDto> courseList = courseProcessor.getAllCoursesByInstitute(instituteId, request);

		Integer maxCount = 0, totalCount = 0;
		if (null != courseList && !courseList.isEmpty()) {
			totalCount = courseList.get(0).getTotalCount();
			maxCount = courseList.size();
		}
		boolean showMore;
		if (request.getMaxSizePerPage() == maxCount) {
			showMore = true;
		} else {
			showMore = false;
		}
		response.put("paginationObj", new PaginationDto(totalCount, showMore,instituteResponseDto));
		response.put("instituteObj", instituteResponseDto);
		response.put("courseList", courseList);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("course.displayed")).setData(response).create();
	}

	public ResponseEntity<?> searchCourseKeyword(final String keyword) throws Exception {
		log.info("Start process to search course keyword based on searcKeyword passed in request");
		List<CourseKeywords> searchkeywordList = courseKeywordProcessor.searchCourseKeyword(keyword);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("course.keyword.displayed")).setData(searchkeywordList).create();
	}

	public ResponseEntity<?> getCoursesByFacultyId(final String facultyId) throws Exception {
		log.info("Start process to get courses based on facultyID");
		List<CourseResponseDto> courseResponseDtos = courseProcessor.getCouresesByFacultyId(facultyId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("course.displayed")).setData(courseResponseDtos).create();
	}

	public ResponseEntity<?> getUserCourses(final List<String> courseIds,final String sortBy, final String sortAsscending) 
			throws ValidationException {
		log.info("Start process to get user course from DB based on pagination and userID");
		List<CourseDto> courses = courseProcessor.getUserCourse(courseIds, sortBy, sortAsscending);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courses)
				.setMessage(messageTranslator.toLocale("course.user.displayed")).create();
	}

	public ResponseEntity<?> courseFilter(final String userId, final Integer pageNumber, final Integer pageSize, final CourseFilterDto courseFilter) throws Exception {
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(courseProcessor.courseFilter(pageNumber, pageSize, true, true, SortingOnEnum.CREATED_ON , SortingTypeEnum.DESC,
				courseFilter.getInstituteId(), courseFilter.getLanguages(), courseFilter.getMinRanking(), courseFilter.getMaxRanking(), courseFilter.getCountryName(), null))
				.setMessage(messageTranslator.toLocale("course.displayed")).create();
	}

	public ResponseEntity<?> autoSearchByCharacter(final String searchKey) throws Exception {
		List<CourseRequest> courses = courseProcessor.autoSearchByCharacter(searchKey);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("course.displayed")).setData(courses).create();
	}

	// This API is used when in normal or global search if data is not available based on filter.
	public ResponseEntity<Object> getCourseNoResultRecommendation(final Integer pageNumber, final Integer pageSize, final String facultyId,
			final String countryId, final String userCountry) throws ValidationException, InvokeException, NotFoundException {
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		List<CourseResponseDto> courseResponseDtos = courseProcessor.getCourseNoResultRecommendation(userCountry,
				facultyId, countryId, startIndex.intValue(), pageSize);
		return new GenericResponseHandlers.Builder().setData(courseResponseDtos)
				.setMessage(messageTranslator.toLocale("course.list.displayed")).setStatus(HttpStatus.OK).create();
	}

	// This API is used for course Info page for related course keyword recommendation.
	public ResponseEntity<Object> getCourseKeywordRecommendation(final Integer pageNumber, final Integer pageSize, final String facultyId,
			final String countryId, final String levelId) throws ValidationException {
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		List<String> courseResponseDtos = courseProcessor.getCourseKeywordRecommendation(facultyId, countryId, levelId,
				startIndex.intValue(), pageSize);
		return new GenericResponseHandlers.Builder().setData(courseResponseDtos)
				.setMessage(messageTranslator.toLocale("course.keyword.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	// This API is used for course Info page for related course keyword recommendation.
	public ResponseEntity<Object> getCheapestCourse(final Integer pageNumber, final Integer pageSize,final String facultyId,
			final String countryId, final String levelId, final String cityId) throws ValidationException {
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		List<CourseResponseDto> courseResponseDtos = userRecommendationService.getCheapestCourse(facultyId, countryId,
				levelId, cityId, startIndex.intValue(), pageSize);
		return new GenericResponseHandlers.Builder().setData(courseResponseDtos)
				.setMessage(messageTranslator.toLocale("course.cheapest.list.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	public ResponseEntity<Object> getCourseCountByLevel() {
		Map<String, Integer> courseCount = courseProcessor.getCourseCountByLevel();
		return new GenericResponseHandlers.Builder().setData(courseCount)
				.setMessage(messageTranslator.toLocale("course.count.displayed")).setStatus(HttpStatus.OK).create();
	}
	
	public ResponseEntity<?> getDistinctCourses(final Integer pageNumber, final Integer pageSize, final String name) {
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		int totalCount = courseProcessor.getDistinctCourseCount(name);
		List<CourseResponseDto> courseList = courseProcessor.getDistinctCourseList(startIndex.intValue(), pageSize,name);
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, pageSize, totalCount);
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setResponse(courseList);
		paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
		paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
		paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
		paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
		paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
		return new GenericResponseHandlers.Builder().setData(paginationResponseDto)
				.setMessage(messageTranslator.toLocale("course.displayed")).setStatus(HttpStatus.OK).create();
		
	}
	
	public ResponseEntity<?> addCourseViaMobile(final String userId, final String instituteId, CourseMobileDto courseMobileDto) throws Exception {
		courseProcessor.addMobileCourse(userId, instituteId, courseMobileDto);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("course.mobile.added")).setStatus(HttpStatus.OK)
				.create();
	}
	
	public ResponseEntity<?> updateCourseViaMobile(final String userId, final String courseId, CourseMobileDto courseMobileDto) throws Exception {
		courseProcessor.updateMobileCourse(userId, courseId, courseMobileDto);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("course.mobile.updated")).setStatus(HttpStatus.OK)
				.create();
	}
	
	public ResponseEntity<?> getCourseViaMobile(final String userId, final String instituteId, final String facultyId, final boolean status) throws Exception {
		List<CourseMobileDto> listOfMobileCourseResponseDto = courseProcessor.getAllMobileCourseByInstituteIdAndFacultyIdAndStatus(userId, instituteId, facultyId, status);
		return new GenericResponseHandlers.Builder().setData(listOfMobileCourseResponseDto)
				.setMessage(messageTranslator.toLocale("course.mobile.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}

	public ResponseEntity<?> getCourseViaMobile(final String instituteId, final String facultyId) throws Exception {
		List<CourseMobileDto> listOfMobileCourseResponseDto = courseProcessor.getPublicMobileCourseByInstituteIdAndFacultyId(instituteId, facultyId);
		return new GenericResponseHandlers.Builder().setData(listOfMobileCourseResponseDto)
				.setMessage(messageTranslator.toLocale("course.mobile.retrieved")).setStatus(HttpStatus.OK)
				.create();
	}
	
	public ResponseEntity<?> changeStatus(final String userId, final String courseId, final boolean  status) throws Exception {
		courseProcessor.changeCourseStatus(userId, courseId, status);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageTranslator.toLocale("course.status.updated")).setStatus(HttpStatus.OK)
				.create();
	}
	
	public ResponseEntity<?> getCourseByInstituteId(Integer pageNumber, Integer pageSize, final String instituteId) throws NotFoundException {
		log.info("Start process to get courses from DB based on instituteId");
		NearestCoursesDto nearestCourseList = courseProcessor.getCourseByInstituteId(pageNumber, pageSize, instituteId);
		return new GenericResponseHandlers.Builder().setData(nearestCourseList)
				.setMessage(messageTranslator.toLocale("course.displayed")).setStatus(HttpStatus.OK)
				.create();
	}
	
	public ResponseEntity<?> getNearestCourseList(final AdvanceSearchDto courseSearchDto) throws Exception {
		log.info("Start process to get nearest course from DB");
		NearestCoursesDto courseResponseDtoList = courseProcessor.getNearestCourses(courseSearchDto);
		return new GenericResponseHandlers.Builder().setData(courseResponseDtoList).setMessage(messageTranslator.toLocale("course.displayed"))
				.setStatus(HttpStatus.OK).create();
	}
	
	public ResponseEntity<?> getCourseByCountryName(Integer pageNumber, Integer pageSize,
			String countryName) throws NotFoundException {
		NearestCoursesDto courseResponse = courseProcessor.getCourseByCountryName(countryName, pageNumber, pageSize);
		return new GenericResponseHandlers.Builder().setData(courseResponse)
				.setMessage(messageTranslator.toLocale("course.displayed")).setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> getCourseByIds(List<String> courseIds) {
		List<CourseDto> courseDtos = courseProcessor.getCourseByMultipleId(courseIds);
		return new GenericResponseHandlers.Builder().setData(courseDtos).setMessage(messageTranslator.toLocale("course.list.displayed"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getRecommendateCourses(String courseId) throws ValidationException {
		List<CourseResponseDto> recommendCourse = userRecommendationService.getCourseRecommended(courseId);
		return new GenericResponseHandlers.Builder().setData(recommendCourse).setMessage(messageTranslator.toLocale("course.recommendate.displayed"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getRelatedCourses(String courseId) throws ValidationException {
		List<CourseResponseDto> relatedCourse = userRecommendationService.getCourseRelated(courseId);
		return new GenericResponseHandlers.Builder().setData(relatedCourse).setMessage(messageTranslator.toLocale("course.related.displayed"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getCourseCountByInstituteId(String instituteId) throws ValidationException {
		CourseCountDto courseCountDto  = courseProcessor.getCourseCountByInstitute(instituteId);
		return new GenericResponseHandlers.Builder().setData(courseCountDto).setMessage(messageTranslator.toLocale("course.count.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> updateProcedureIdInCourse(List<String> courseIds,String studentType, String procedureId) {
		courseProcessor.updateProcedureIdInCourse(courseIds, studentType, procedureId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course.procedure_id.updated"))
				.setStatus(HttpStatus.OK).create();
	}
	
	@Override
	public ResponseEntity<?> updateProcedureIdInCourseByInstituteId(String instituteId,String studentType, String procedureId) {
		courseProcessor.updateProcedureIdInCourseByInstituteId(instituteId, studentType, procedureId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course.procedure_id.updated"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> publishDraftCourse(String userId, String courseId) {
		courseProcessor.publishCourse(userId, courseId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course.publish.success"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> getDraftCourses(String userId, Integer pageNumber, Integer pageSize, String name, String instituteId) {
		return new GenericResponseHandlers.Builder().setData(courseProcessor.getDraftCourses(userId, pageNumber, pageSize, name, instituteId))
				.setMessage(messageTranslator.toLocale("course.publish.list.retrieved"))
				.setStatus(HttpStatus.OK).create();
	}

	@Override
	public ResponseEntity<?> discardDraftCourse(String userId, String courseId) {
		courseProcessor.discardDraftCourse(userId, courseId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("course.discarded.success"))
				.setStatus(HttpStatus.OK).create();
	}

}