package com.azguards.app.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseIntake;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.bean.Scholarship;
import com.azguards.app.bean.ScholarshipCountry;
import com.azguards.app.bean.ScholarshipEligibleNationality;
import com.azguards.app.bean.ScholarshipLanguage;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.ValidList;
import com.yuzee.common.lib.dto.PaginationResponseDto;
import com.yuzee.common.lib.dto.institute.CourseDeliveryModesDto;
import com.yuzee.common.lib.dto.institute.CourseEnglishEligibilityDto;
import com.yuzee.common.lib.dto.institute.CourseIntakeDto;
import com.yuzee.common.lib.dto.institute.CourseSyncDTO;
import com.yuzee.common.lib.dto.institute.InstituteSyncDTO;
import com.yuzee.common.lib.dto.institute.ScholarshipSyncDto;
import com.yuzee.common.lib.dto.storage.StorageDto;
import com.yuzee.common.lib.enumeration.EntitySubTypeEnum;
import com.yuzee.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DTOUtils {
	private DTOUtils() {
	}

	public static InstituteSyncDTO convertToInstituteElasticSearchDTOEntity(Institute institute) {
		log.info("inside DTOUtils.convertToInstituteElasticSearchDTOEntity");
		ModelMapper modelMapper = new ModelMapper();
		InstituteSyncDTO instituteElaticDto = modelMapper.map(institute, InstituteSyncDTO.class);
		instituteElaticDto.setIntakes(
				institute.getInstituteIntakes().stream().map(InstituteIntake::getIntake).collect(Collectors.toList()));
		return instituteElaticDto;
	}

	public static CourseSyncDTO convertToCourseDTOElasticSearchEntity(Course course) {
		log.info("inside DTOUtils.convertToCourseDTOElasticSearchEntity");
		ModelMapper modelMapper = new ModelMapper();

		Converter<List<CourseLanguage>, List<String>> courseLanguageConverter = ctx -> ctx.getSource() == null ? null
				: ctx.getSource().stream().map(courseLanguage -> courseLanguage.getLanguage())
						.collect(Collectors.toList());

		modelMapper.typeMap(Course.class, CourseSyncDTO.class).addMappings(mapper -> mapper
				.using(courseLanguageConverter).map(Course::getCourseLanguages, CourseSyncDTO::setLanguages));

		CourseSyncDTO courseElasticDto = modelMapper.map(course, CourseSyncDTO.class);

		courseElasticDto.setInstitute(convertToInstituteElasticSearchDTOEntity(course.getInstitute()));
		return courseElasticDto;

	}

	public static ScholarshipSyncDto convertScholarshipToScholarshipDTOElasticSearchEntity(Scholarship scholarship) {
		log.info("inside DTOUtils.convertToCourseDTOElasticSearchEntity");
		ModelMapper modelMapper = new ModelMapper();

		ScholarshipSyncDto scholarshipElasticDto = modelMapper.map(scholarship, ScholarshipSyncDto.class);
		scholarshipElasticDto.setLanguages(scholarship.getScholarshipLanguages().stream()
				.map(ScholarshipLanguage::getName).collect(Collectors.toList()));
		scholarshipElasticDto.setEligibleNationalities(scholarship.getScholarshipEligibleNationalities().stream()
				.map(ScholarshipEligibleNationality::getCountryName).collect(Collectors.toList()));
		scholarshipElasticDto.setCountryNames(scholarship.getScholarshipCountries().stream()
				.map(ScholarshipCountry::getCountryName).collect(Collectors.toList()));
		scholarshipElasticDto.setInstitute(convertToInstituteElasticSearchDTOEntity(scholarship.getInstitute()));
		return scholarshipElasticDto;
	}

	public static List<CourseDeliveryModesDto> createCourseDeliveryModesDtoListFromCourse(Course courseFromDb){
		List<CourseDeliveryModesDto> courseDeliveryModesResponse = new ArrayList<>();
		if(!CollectionUtils.isEmpty(courseFromDb.getCourseDeliveryModes())) {
			
			courseDeliveryModesResponse = courseFromDb.getCourseDeliveryModes().stream().map(courseDeliveryMode -> {
				CourseDeliveryModesDto courseDeliveryModesDto = new CourseDeliveryModesDto();
				log.info("Copying Bean class values to DTO class using beanUtils");
				BeanUtils.copyProperties(courseDeliveryMode, courseDeliveryModesDto);
				courseDeliveryModesDto.setCourseId(courseFromDb.getId());
				return courseDeliveryModesDto;
			}).collect(Collectors.toList());

		}
		return courseDeliveryModesResponse;
	}
	
	public static List<CourseEnglishEligibilityDto> createCourseEnglishEligibilityDtoListFromCourse(Course courseFromDb){
		List<CourseEnglishEligibilityDto> courseEnglishEligibilityDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(courseFromDb.getCourseEnglishEligibilities())) {

			courseEnglishEligibilityDtos = courseFromDb.getCourseEnglishEligibilities().stream().map(courseEnglishEligibility -> {
				CourseEnglishEligibilityDto courseEnglishEligibilityDto = new CourseEnglishEligibilityDto();
				BeanUtils.copyProperties(courseEnglishEligibility, courseEnglishEligibilityDto);
				return courseEnglishEligibilityDto;
			}).collect(Collectors.toList());
		}
		return courseEnglishEligibilityDtos;
	}
	
	public static List<String> createCourseLanguageListFromCourse(Course courseFromDb){
		List<String> languages = new ArrayList<>();
		if(!CollectionUtils.isEmpty(courseFromDb.getCourseLanguages())) {
			languages = courseFromDb.getCourseLanguages().stream().map(courseLanguage -> {
				return courseLanguage.getLanguage();
			}).collect(Collectors.toList());
		}
		return languages;
	}
	private static CourseIntakeDto createCourseIntakeDtoFromCourseDb(CourseIntake courseIntakeFromDb) {
		CourseIntakeDto courseIntakeDto = new CourseIntakeDto();
		if(!ObjectUtils.isEmpty(courseIntakeFromDb)) {
			courseIntakeDto.setType(courseIntakeFromDb.getType().name());;		
			courseIntakeDto.setDates(courseIntakeFromDb.getDates());
		}
		return courseIntakeDto;
	}
	public static PaginationResponseDto<List<CourseRequest>> createPageCourseRequestFromCourse(Page<Course> coursePage,
			Map<String, Map<EntitySubTypeEnum, List<StorageDto>>> mapOfStorages) {
		List<CourseRequest> courseRequestList = new ArrayList<> ();
		if (!CollectionUtils.isEmpty(coursePage.getContent())) {
			courseRequestList = coursePage.getContent().stream().map(course -> {
				
				CourseRequest courseRequest =  new CourseRequest();
				
				courseRequest.setId(course.getId());
				courseRequest.setName(course.getName());
				courseRequest.setDescription(course.getDescription());
				courseRequest.setWebsite(course.getWebsite());
				if(!CollectionUtils.isEmpty(course.getCourseLanguages())) {
					courseRequest.setLanguage(createCourseLanguageListFromCourse(course));		
				}
				if(!ObjectUtils.isEmpty(course.getWorldRanking())) {
					courseRequest.setWorldRanking(Integer.toString(course.getWorldRanking()));
				}
				courseRequest.setRemarks(course.getRemarks());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
				courseRequest.setLastUpdated(formatter.format(course.getUpdatedOn()));
				
				if(!ObjectUtils.isEmpty(course.getLevel())) {
					courseRequest.setLevelId(course.getLevel().getId());
				}
				if(!ObjectUtils.isEmpty(course.getInstitute())) {
					courseRequest.setInstituteId(course.getInstitute().getId());	
					courseRequest.setInstituteName(course.getInstitute().getName());
					courseRequest.setCost(course.getInstitute().getAvgCostOfLiving());	
					courseRequest.setLocation(course.getInstitute().getCityName() + ", " + course.getInstitute().getCountryName());
					courseRequest.setCityName(course.getInstitute().getCityName());
					courseRequest.setCountryName(course.getInstitute().getCountryName());
					if (!MapUtils.isEmpty(mapOfStorages) && mapOfStorages.containsKey(course.getInstitute().getId())) {
						Map < EntitySubTypeEnum, List < StorageDto >> storageMap = null;
						storageMap = mapOfStorages.get(course.getInstitute().getId());
						if (!MapUtils.isEmpty(storageMap) && storageMap.containsKey(EntitySubTypeEnum.IMAGES)) {
							courseRequest.setStorageList(storageMap.get(EntitySubTypeEnum.IMAGES));
						}
					}
				}
				if(!ObjectUtils.isEmpty(course.getFaculty())) {
					courseRequest.setFacultyId(course.getFaculty().getId());
				}
				if(!CollectionUtils.isEmpty(course.getCourseDeliveryModes())) {
					courseRequest.setCourseDeliveryModes(new ValidList<>(createCourseDeliveryModesDtoListFromCourse(course)));
				}
				if(!CollectionUtils.isEmpty(course.getCourseEnglishEligibilities())) {
					courseRequest.setEnglishEligibility(new ValidList<>(createCourseEnglishEligibilityDtoListFromCourse(course)));
				}
				if(!ObjectUtils.isEmpty(course.getCourseIntake())) {
					courseRequest.setIntake(createCourseIntakeDtoFromCourseDb(course.getCourseIntake()));
				}
				return courseRequest;
			}).collect(Collectors.toList());
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(coursePage, courseRequestList);
	}

}