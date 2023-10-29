package com.azguards.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseIntake;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.Faculty;
import com.azguards.app.bean.Institute;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseDto;
import com.azguards.app.dto.CourseFilterDto;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.UserDto;
import com.azguards.common.lib.dto.common.CurrencyRateDto;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.enumeration.SortingOnEnum;
import com.azguards.common.lib.enumeration.SortingTypeEnum;
import com.azguards.common.lib.exception.ValidationException;

public interface CourseDao {

	public Course addUpdateCourse(Course obj) throws ValidationException;

	public List<Course> saveAll(List<Course> courses) throws ValidationException;

	public Course get(String id);

	public List<CourseResponseDto> getAllCoursesByFilter(CourseSearchDto filterObj, String searchKeyword, List<String> courseIds, Integer startIndex,
			boolean uniqueCourseName, List<String> entityIds);
	
	public List<CourseResponseDto> getAllCoursesByInstitute(String instituteId, CourseSearchDto filterObj);

	public List<CourseResponseDto> getCouresesByFacultyId(String facultyId);
	
	public List<Course> getAllCourseByInstituteIdAndFacultyIdAndStatus (String instituteId,String facultyId, boolean isActive);
	
	public List<Course> getAllCourseByInstituteIdAndFacultyId (String instituteId,String facultyId);

	public int findTotalCount();
	
	public Page<Course> findCourseByFilters(Pageable pageable, Boolean isNotDeleted, Boolean isActive, SortingOnEnum sortingOnEnum, SortingTypeEnum sortingTypeEnum,
			String instituteId,  List<String> languages, Integer minRanking, Integer maxRanking, String countryName, String searchKey);

	public List<CourseDto> getUserCourse(List<String> courseIds, String sortBy, boolean sortType) throws ValidationException;

	public int findTotalCountByUserId(String userId);

	public Course getCourseData(String id);

	public List<CourseResponseDto> advanceSearch(List<String> entityIds,Object... values);

	public Long autoSearchTotalCount(String searchKey);

	public List<Course> facultyWiseCourseForTopInstitute(List<Faculty> facultyList, Institute institute);

	public long getCourseCountForCountry(String countryName);
	
	public long getCourseCountByInstituteId (String instituted);
	
	public List<Course> getAllCoursesUsingId(List<String> listOfRecommendedCourseIds);

	public Long getCountOfDistinctInstitutesOfferingCoursesForCountry(UserDto userDto, String countryName);

	public List<String> getDistinctCountryBasedOnCourses(List<String> topSearchedCourseIds);

	public List<String> getCourseListForCourseBasedOnParameters(String courseId, String instituteId, String facultyId, String countryId,
			String cityId);

	public List<String> getCourseIdsForCountry(final String countryName);

	public List<String> getAllCoursesForCountry(List<String> otherCountryIds);

	public int updateCourseForCurrency(CurrencyRateDto currencyRate);

	public int getCountforNormalCourse(CourseSearchDto courseSearchDto, String searchKeyword, List<String> entityIds);

	public int getCountOfAdvanceSearch(List<String> entityIds, Object... values);

	public Integer getTotalCourseCountForInstitute(String instituteId);

	public List<CourseSyncDTO> getUpdatedCourses(Date date, Integer startIndex, Integer limit);

	public Integer getCountOfTotalUpdatedCourses(Date utCdatetimeAsOnlyDate);

	public List<CourseSyncDTO> getCoursesToBeRetriedForElasticSearch(List<String> courseIds, Integer startIndex, Integer limit);

	public List<CourseIntake> getCourseIntakeBasedOnCourseId(String courseId);

	public void deleteCourseDeliveryMethod(String courseId);

	public void saveCourseLanguage(CourseLanguage courseLanguage);

	public List<CourseLanguage> getCourseLanguageBasedOnCourseId(String courseId);

	public List<String> getUserSearchCourseRecommendation(Integer startIndex, Integer pageSize, String searchKeyword);
	
	public Integer getCoursesCountBylevelId(String levelId);

	public int getDistinctCourseCountbyName(String courseName);

	public List<CourseResponseDto> getDistinctCourseListByName(Integer startIndex, Integer pageSize, String courseName);
	
	public List<CourseResponseDto> getNearestCourseForAdvanceSearch(AdvanceSearchDto courseSearchDto);
	
	public List<CourseResponseDto> getCourseByCountryName(Integer startIndex, Integer pageSize, String countryName);
	
	public Integer getTotalCountOfNearestCourses(Double latitude, Double longitude, Integer initialRadius);
	
	public List<CourseResponseDto> getRelatedCourseBasedOnCareerTest(List<String> searchKeyword, Integer startIndex, Integer pageSize);
	
	public Integer getRelatedCourseBasedOnCareerTestCount(List<String> searchKeyword);
	
	public void deleteCourse(String id);

	public List<Course> findByInstituteId(String instituteId);

	public List<Course> findAllById(List<String> ids);

	void deleteAll(List<Course> courses);

	public List<Course> findAll();

	public List<Course> findByReadableIdIn(List<String> readableIds);

	public Course findByReadableId(String readableId);
	
	public CourseRequest saveDocument(CourseRequest courseRequest);

	public Page<CourseRequest> filterDocuments(String name, String instituteId, Pageable pageable);

	public Optional<CourseRequest> findDocumentById(String id);

	public void deleteDocumentById(String id);
	
	public boolean existsById(String id);

	public boolean documentExistsById(String id);
}
