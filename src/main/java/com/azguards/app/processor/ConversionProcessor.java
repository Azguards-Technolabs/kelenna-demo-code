package com.azguards.app.processor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteCategoryType;
import com.azguards.app.bean.InstituteEnglishRequirements;
import com.azguards.app.bean.InstituteFacility;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.bean.InstituteService;
import com.azguards.app.bean.Scholarship;
import com.azguards.app.dao.TimingDao;
import com.azguards.common.lib.dto.ValidList;
import com.azguards.common.lib.dto.institute.CourseDeliveryModeFundingDto;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.CourseFeesDto;
import com.azguards.common.lib.dto.institute.CourseIntakeDto;
import com.azguards.common.lib.dto.institute.CoursePaymentDto;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.dto.institute.InstituteEnglishRequirementsElasticDto;
import com.azguards.common.lib.dto.institute.InstituteSyncDTO;
import com.azguards.common.lib.dto.institute.ProviderCodeDto;
import com.azguards.common.lib.dto.institute.TimingDto;
import com.azguards.common.lib.dto.scholarship.ScholarshipSyncDto;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.model.elastic.ProviderCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConversionProcessor {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TimingDao timingDao;

	@Autowired
	private CourseMinRequirementProcessor minRequirementProcessor;

	@PostConstruct
	private void postConstrut() {
		
		modelMapper.getConfiguration().setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
		
		Converter<List<InstituteEnglishRequirements>, List<InstituteEnglishRequirementsElasticDto>> instituteEnglishRequirementElasticDtoConverter = ctx -> ctx
				.getSource() == null
						? null
						: ctx.getSource().stream()
								.map(instituteEnglishRequirement -> new InstituteEnglishRequirementsElasticDto(
										instituteEnglishRequirement.getId(), instituteEnglishRequirement.getExamName(),
										instituteEnglishRequirement.getReadingMarks(),
										instituteEnglishRequirement.getWritingMarks(),
										instituteEnglishRequirement.getOralMarks(),
										instituteEnglishRequirement.getListningMarks()))
								.collect(Collectors.toList());

		Converter<List<InstituteFacility>, List<String>> instituteFacilityConverter = ctx -> ctx.getSource() == null
				? null
				: ctx.getSource().stream().map(instituteFacility -> instituteFacility.getService().getName())
						.collect(Collectors.toList());

		Converter<List<InstituteService>, List<String>> instituteServiceConverter = ctx -> ctx.getSource() == null
				? null
				: ctx.getSource().stream().map(instituteService -> instituteService.getService().getName())
						.collect(Collectors.toList());

		Converter<List<InstituteIntake>, List<String>> instituteIntakeConverter = ctx -> ctx.getSource() == null ? null
				: ctx.getSource().stream().map(instituteIntake -> instituteIntake.getIntake())
						.collect(Collectors.toList());

		Converter<InstituteCategoryType, String> instituteTypeConverter = ctx -> ctx.getSource() == null ? null
				: ctx.getSource().getName();

		modelMapper.typeMap(Institute.class, InstituteSyncDTO.class)
				.addMappings(mapper -> mapper.using(instituteEnglishRequirementElasticDtoConverter).map(
						Institute::getInstituteEnglishRequirements,
						InstituteSyncDTO::setInstituteEnglishRequirements));

		modelMapper.typeMap(Institute.class, InstituteSyncDTO.class)
				.addMappings(mapper -> mapper.using(instituteFacilityConverter).map(Institute::getInstituteFacilities,
						InstituteSyncDTO::setInstituteFacilities));

		modelMapper.typeMap(Institute.class, InstituteSyncDTO.class)
				.addMappings(mapper -> mapper.using(instituteServiceConverter).map(Institute::getInstituteServices,
						InstituteSyncDTO::setInstituteServices));

		modelMapper.typeMap(Institute.class, InstituteSyncDTO.class)
				.addMappings(mapper -> mapper.using(instituteIntakeConverter).map(Institute::getInstituteIntakes,
						InstituteSyncDTO::setInstituteIntakes));
	}

	public CourseSyncDTO convertToCourseSyncDTOSyncDataEntity(Course course) {
		log.info("inside DTOUtils.convertToCourseSyncDTOSyncDataEntity ");
		CourseSyncDTO syncCourse = modelMapper.map(course, CourseSyncDTO.class);

		syncCourse.setLanguages(
				course.getCourseLanguages().stream().map(CourseLanguage::getLanguage).collect(Collectors.toList()));
		syncCourse.setCourseType(course.getCourseType().name());
		
		if(ObjectUtils.isEmpty(course.getCoursePayment())) {
			syncCourse.setCoursePayment(new CoursePaymentDto());
		}
		if(ObjectUtils.isEmpty(course.getCourseIntake())) {
			syncCourse.setCourseIntake(new CourseIntakeDto());
		}else {
			syncCourse.setCourseIntake(new CourseIntakeDto(course.getCourseIntake().getType().name(),course.getCourseIntake().getDates()));
		}
		
		syncCourse.setCourseDeliveryModes(course.getCourseDeliveryModes().stream()
				.map(delMode -> new CourseDeliveryModesDto(delMode.getId(), delMode.getDeliveryType(),
						delMode.getDuration(), delMode.getDurationTime(), delMode.getStudyMode(),
						delMode.getCourse().getId(), delMode.getAccessibility(), delMode.getIsGovernmentEligible(),
						new ValidList<>(delMode.getFundings().stream()
								.map(funding -> new CourseDeliveryModeFundingDto(funding.getId(),
										funding.getFundingNameId(), funding.getName(), funding.getCurrency(),
										funding.getAmount()))
								.collect(Collectors.toList())),
						new ValidList<>(delMode.getFees().stream().map(fees -> new CourseFeesDto(fees.getId(),
								fees.getName(), fees.getCurrency(), fees.getAmount())).collect(Collectors.toList()))))
				.toList());
		syncCourse.setProviderCodes(course.getCourseProviderCodes().stream().map(e->new ProviderCode(e.getId(), e.getName(), e.getValue())).toList());
		syncCourse.setCourseMinRequirements(course.getCourseMinRequirements().stream().map(e->minRequirementProcessor.modelToDto(e)).toList());
		syncCourse.setCourseTimings(timingDao.findByEntityTypeAndEntityIdIn(EntityTypeEnum.COURSE, Arrays.asList(course.getId())).stream()
				.map(e -> modelMapper.map(e, TimingDto.class)).collect(Collectors.toList()));
		return syncCourse;
	}

	public InstituteSyncDTO convertToInstituteInstituteSyncDTOSynDataEntity(Institute institute) {
		log.info("inside DTOUtils.convertToInstituteInstituteSyncDTOSynDataEntity");
		InstituteSyncDTO dto = modelMapper.map(institute, InstituteSyncDTO.class);
		dto.setInstituteProviderCodes(institute.getInstituteProviderCodes().stream()
				.map(instituteProviderCode -> new ProviderCodeDto(instituteProviderCode.getId(),
						instituteProviderCode.getName(), instituteProviderCode.getValue()))
				.collect(Collectors.toList()));
		return dto;
	}

	public ScholarshipSyncDto convertToScholarshipSyncDtoSyncDataEntity(Scholarship scholarship) {
		log.info("inside DTOUtils.convertToScholarshipSyncDtoSyncDataEntity");
		ScholarshipSyncDto scholarshipElasticDto = modelMapper.map(scholarship, ScholarshipSyncDto.class);
//		scholarshipElasticDto.setLanguages(scholarship.getScholarshipLanguages().stream()
//				.map(ScholarshipLanguage::getName).collect(Collectors.toList()));
//		scholarshipElasticDto.setEligibleNationalities(scholarship.getScholarshipEligibleNationalities().stream()
//				.map(ScholarshipEligibleNationality::getCountryName).collect(Collectors.toList()));
//		scholarshipElasticDto.setCountryNames(scholarship.getScholarshipCountries().stream()
//				.map(ScholarshipCountry::getCountryName).collect(Collectors.toList()));
		return scholarshipElasticDto;
	}

}
