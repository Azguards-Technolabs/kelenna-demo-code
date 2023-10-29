package com.azguards.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.OffCampusCourseDto;
import com.azguards.common.lib.dto.storage.StorageDto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseResponseDto {

	@JsonProperty("course_id")
	private String id;

	@JsonProperty("name")
	@NotBlank(message = "{name.is_required}")
	private String name;

	@JsonProperty("course_ranking")
	private Integer courseRanking;

	@JsonProperty("stars")
	private Double stars;

	@JsonProperty("language")
	private List<String> language = new ArrayList<>();

	@JsonProperty("institute_id")
	@NotBlank(message = "{institute_id.is_required}")
	private String instituteId;

	@JsonProperty("level_id")
	private String levelId;

	@JsonProperty("institute_name")
	@NotBlank(message = "{institute_name.is_required}")
	private String instituteName;

	@JsonProperty("location")
	private String location;

	@JsonProperty("totalCount")
	private Integer totalCount;

	@JsonProperty("requirements")
	private String requirements;

	@JsonProperty("country_name")
	@NotBlank(message = "{countryName.is_required}")
	private String countryName;

	@JsonProperty("city_name")
	@NotBlank(message = "{cityName.is_required}")
	private String cityName;

	@JsonProperty("is_favourite")
	private Boolean isFavourite;

	@JsonProperty("currency_code")
	@NotBlank(message = "{currency_code.is_required}")
	private String currencyCode;

	@JsonProperty("storage_list")
	private List<StorageDto> storageList = new ArrayList<>();

	@JsonProperty("is_viewed")
	private Boolean isViewed = false;

	@JsonProperty("cost")
	private String cost;

	@JsonProperty("is_active")
	private Boolean isActive;

	@JsonProperty("updated_on")
	private Date updatedOn;
	
	@JsonProperty("faculty_name")
	@NotBlank(message = "{faculty_name.is_required}")
	private String facultyName;

	@JsonProperty("faculty_id")
	@NotBlank(message = "{faculty_id.is_required}")
	private String facultyId;

	@JsonProperty("distance")
	private Double distance;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("course_delivery_modes")
	private List<CourseDeliveryModesDto> courseDeliveryModes = new ArrayList<>();
	
	@JsonProperty("course_description")
	private String courseDescription;
	
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("website")
	private String website;
	
	@JsonProperty("course_timings")
	private List<TimingRequestDto> courseTimings = new ArrayList<>();
	
	@JsonProperty("off_campus_course")
	private OffCampusCourseDto offCampusCourse;
	
	@JsonProperty("readable_id")
	private String readableId;
}
