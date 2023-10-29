package com.azguards.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.cache.annotation.Cacheable;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteCategoryType;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.bean.InstituteService;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.InstituteFacultyDto;
import com.azguards.app.dto.InstituteFilterDto;
import com.azguards.app.dto.InstituteGetRequestDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.common.lib.exception.NotFoundException;

public interface InstituteDao {

	public Institute addUpdateInstitute(Institute obj);

	public Institute get(String id);

	public List<InstituteResponseDto> getAllInstitutesByFilter(CourseSearchDto filterObj, String sortByField,
			String sortByType, String searchKeyword, Integer startIndex, String cityId, List<String> instituteTypeId,
			Boolean isActive, Date updatedOn, Integer fromWorldRanking, Integer toWorldRanking);

	public InstituteResponseDto getInstituteById(String instituteId);

	public List<Institute> searchInstitute(String sqlQuery);

	public int findTotalCount();

	public List<InstituteGetRequestDto> getAll(Integer pageNumber, Integer pageSize);

	public List<Institute> instituteFilter(int startIndex, Integer maxSizePerPage,
			InstituteFilterDto instituteFilterDto);

	public int findTotalCountFilterInstitute(InstituteFilterDto instituteFilterDto);

	public List<InstituteGetRequestDto> autoSearch(int startIndex, Integer pageSize, String searchKey);

	public int findTotalCountForInstituteAutosearch(String searchKey);

	public InstituteCategoryType getInstituteCategoryType(String instituteCategoryTypeId);

	public List<Institute> getSecondayCampus(String countryId, String cityId, String name);

	public void saveInstituteserviceDetails(InstituteService instituteServiceDetails);

	public void deleteInstituteService(String id);

	public void saveInstituteIntake(InstituteIntake instituteIntake);

	public void deleteInstituteIntakeById(String id);

	public List<String> getIntakesById(@Valid String id);

	public List<InstituteCategoryType> getAllCategories();

	public List<InstituteCategoryType> addInstituteCategoryTypes(List<InstituteCategoryType> instituteCategoryTypes);

	public List<Institute> ratingWiseInstituteListByCountry(String countryName);

	public List<String> getInstituteIdsBasedOnGlobalRanking(Long startIndex, Long pageSize);

	public List<String> getRandomInstituteByCountry(List<String> countryIdList);

	public int getCountOfInstitute(CourseSearchDto courseSearchDto, String searchKeyword, String cityId,
			List<String> instituteTypeId, Boolean isActive, Date updatedOn, Integer fromWorldRanking, Integer toWorldRanking);

	public Map<String, Integer> getDomesticRanking(List<String> instituteIdList);

	public List<InstituteResponseDto> getNearestInstituteListForAdvanceSearch(AdvanceSearchDto courseSearchDto);

	public List<String> getUserSearchInstituteRecommendation(Integer startIndex, Integer pageSize,
			String searchKeyword);

	public List<InstituteResponseDto> getInstitutesByInstituteName(Integer startIndex, Integer pageSize,
			String institueName);

	public int getDistinctInstituteCountByName(String skillName);

	public Optional<Institute> getInstituteByInstituteId(String instituteId);

	public List<InstituteResponseDto> getNearestInstituteList(Integer pageNumber, Integer pageSize, Double latitutde,
			Double longitude, Integer initialRadius);

	public Integer getTotalCountOfNearestInstitutes(Double latitude, Double longitude, Integer initialRadius);

	public List<Institute> getInstituteCampuses(String instituteId, String instituteName) throws NotFoundException;

	public List<InstituteFacultyDto> getInstituteFaculties(String instituteId) throws NotFoundException;

	public List<InstituteResponseDto> findByIds(List<String> instituteIds);

	public List<Institute> findAllById(List<String> instituteIds);

	public List<Institute> findByReadableIdIn(List<String> readableIds);

	public Institute findByReadableId(String readableId);
	
	@Cacheable(value = "cacheInstituteMap", unless = "#result == null")
	public Map<String, String> getAllInstituteMap();

	@Cacheable(value = "cacheInstitute", key = "#instituteId", unless = "#result == null", condition="#instituteId!=null")
	public Institute getInstitute(String instituteId);

	public List<Institute> saveAll(List<Institute> institutesFromDb);
}
