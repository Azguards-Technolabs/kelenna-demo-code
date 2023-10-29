package com.azguards.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.application.ProcedureDto;
import com.azguards.common.lib.dto.institute.CourseCareerOutcomeDto;
import com.azguards.common.lib.dto.institute.CourseContactPersonDto;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.CourseEnglishEligibilityDto;
import com.azguards.common.lib.dto.institute.CourseFundingDto;
import com.azguards.common.lib.dto.institute.CourseIntakeDto;
import com.azguards.common.lib.dto.institute.CourseMinRequirementDto;
import com.azguards.common.lib.dto.institute.CoursePaymentDto;
import com.azguards.common.lib.dto.institute.CoursePreRequisiteDto;
import com.azguards.common.lib.dto.institute.CourseSemesterDto;
import com.azguards.common.lib.dto.institute.FacultyDto;
import com.azguards.common.lib.dto.institute.LevelDto;
import com.azguards.common.lib.dto.institute.OffCampusCourseDto;
import com.azguards.common.lib.dto.institute.ProviderCodeDto;
import com.azguards.common.lib.dto.storage.StorageDto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Document("course")
public class CourseRequest {

	@JsonProperty("course_id")
	private String id;

	@JsonProperty("institute_id")
	private String instituteId;

	@JsonProperty("faculty_id")
	@NotBlank(message = "{faculty_id.is_required}")
	private String facultyId;

	@JsonProperty("faculty")
	private FacultyDto faculty;

	@JsonProperty("curriculum_id")
	private String curriculumId;

	@JsonProperty("name")
	@NotBlank(message = "{name.is_required}")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("language")
	private List<String> language;

	@JsonProperty("grades")
	private String grades;

	@JsonProperty("document_url")
	private String documentUrl;

	@JsonProperty("website")
	private String website;

	@JsonProperty("last_updated")
	private String lastUpdated;

	@JsonProperty("institute_name")
	private String instituteName;

	@JsonProperty("location")
	private String location;

	@JsonProperty("world_ranking")
	private String worldRanking;

	@JsonProperty("stars")
	private Double stars;

	@JsonProperty("cost")
	private String cost;

	@JsonProperty("total_count")
	private String totalCount;
	
	@JsonProperty("currency")
	private String currency;

	@JsonProperty("currency_time")
	private String currencyTime;

	@JsonProperty("level_id")
	@NotBlank(message = "{level_id.is_required}")
	private String levelId;

	@JsonProperty("level")
	private LevelDto level;

	@JsonProperty("availability")
	private String availability;

	@JsonProperty("recognition")
	private String recognition;

	@JsonProperty("recognition_type")
	private String recognitionType;

	@JsonProperty("abbreviation")
	private String abbreviation;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("applied")
	private Boolean applied;

	@JsonProperty("view_course")
	private Boolean viewCourse;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("country_name")
	private String countryName;

	@JsonProperty("city_name")
	private String cityName;

	@JsonProperty("storage_list")
	private List<StorageDto> storageList = new ArrayList<>();

	@Valid
	@JsonProperty("course_delivery_modes")
	private ValidList<CourseDeliveryModesDto> courseDeliveryModes = new ValidList<>();

	@Valid
	@JsonProperty("course_prerequisites")
	private List<CoursePreRequisiteDto> prerequisites;

	@Valid
	@JsonProperty("english_eligibility")
	private ValidList<CourseEnglishEligibilityDto> englishEligibility = new ValidList<>();

	@JsonProperty("examination_board")
	private String examinationBoard;

	@JsonProperty("domestic_application_fee")
	private Double domesticApplicationFee;

	@JsonProperty("international_application_fee")
	private Double internationalApplicationFee;

	@JsonProperty("domestic_enrollment_fee")
	private Double domesticEnrollmentFee;

	@JsonProperty("international_enrollment_fee")
	private Double internationalEnrollmentFee;

	@JsonProperty("domestic_boarding_fee")
	private Double domesticBoardingFee;

	@JsonProperty("international_boarding_fee")
	private Double internationalBoardingFee;

	@JsonProperty("entrance_exam")
	private String entranceExam;

	@JsonProperty("phone_number")
	private String phoneNumber;

	@JsonProperty("global_gpa")
	private Double globalGpa;

	@JsonProperty("content")
	private String content;

	@JsonProperty("email")
	private String email;

	@JsonProperty("rec_date")
	private Date recDate;

	@JsonProperty("has_edit_access")
	private Boolean hasEditAccess;

	@JsonProperty("favorite_course")
	private Boolean favoriteCourse;

	@JsonProperty("reviews_count")
	private Long reviewsCount;
	
	@JsonProperty("audience")
	private String audience;

	@JsonProperty("fundings_count")
	private int fundingsCount;

	@JsonProperty("off_campus_course")
	private OffCampusCourseDto offCampusCourse;

	@Valid
	@JsonProperty("course_semesters")
	@NotNull(message = "{course_semesters.is_required}")
	private ValidList<CourseSemesterDto> courseSemesters = new ValidList<>();

	@Valid
	@JsonProperty("course_timings")
	@NotNull(message = "{course_timings.is_required}")
	private ValidList<TimingRequestDto> courseTimings = new ValidList<>();

	@Valid
	@JsonProperty("course_career_outcomes")
	private ValidList<CourseCareerOutcomeDto> courseCareerOutcomes = new ValidList<>();

	@Valid
	@JsonProperty("course_fundings")
	private ValidList<CourseFundingDto> courseFundings = new ValidList<>();

	@JsonProperty("course_payment")
	private CoursePaymentDto coursePayment;

	@Valid
	@JsonProperty("institute")
	private InstituteResponseDto institute;

	@Valid
	@JsonProperty("course_contact_persons")
	@NotNull(message = "{course_contact_persons.is_required}")
	private ValidList<CourseContactPersonDto> courseContactPersons = new ValidList<>();
	
	@Valid
	@JsonProperty("provider_codes")
	private ValidList<ProviderCodeDto> providerCodes = new ValidList<>();
	
	@JsonProperty("course_intake")
	private CourseIntakeDto intake;
	
	@JsonProperty("international_student_procedure")
	private ProcedureDto internationalStudentProcedure;
	
	@JsonProperty("domestic_student_procedure")
	private ProcedureDto domesticStudentProcedure;
	
	@JsonProperty("international_student_procedure_id")
	private String internationalStudentProcedureId;
	
	@JsonProperty("domestic_student_procedure_id")
	private String domesticStudentProcedureId;
	
	@Valid 
	@NotNull(message = "{course_min_requirements.is_required}")
	@JsonProperty("course_min_requirements")
	ValidList<CourseMinRequirementDto> courseMinRequirementDtos;
}