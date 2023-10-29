package com.azguards.app.dto;

import java.util.List;
import com.azguards.common.lib.dto.storage.StorageDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class InstituteAdditionalInfoDto {

	@NotNull(message = "{student_number.is_required}")
	@JsonProperty("student_number")
	private Integer numberOfStudent;

	@NotNull(message = "{employee_number.is_required}")
	@JsonProperty("employee_number")
	private Integer numberOfEmployee;

	@NotNull(message = "{teacher_number.is_required}")
	@JsonProperty("teacher_number")
	private Integer numberOfTeacher;

	@NotNull(message = "{class_number.is_required}")
	@JsonProperty("class_number")
	private Integer numberOfClassRoom;

	@NotNull(message = "{campus_size.is_required}")
	@JsonProperty("campus_size")
	private Integer sizeOfCampus;

	@NotNull(message = "{lecture_hall_number.is_required}")
	@JsonProperty("lecture_hall_number")
	private Integer numberOfLectureHall;

	@NotNull(message = "{faculty_number.is_required}")
	@JsonProperty("faculty_number")
	private Integer numberOfFaculty;

	@NotNull(message = "{employment_rate.is_required}")
	@JsonProperty("employment_rate")
	private Integer rateOfEmployment;

	@NotBlank(message = "{about_info.is_required}")
	@JsonProperty(value = "about_info", access = Access.WRITE_ONLY)
	private String aboutInfo;

	@JsonProperty(value = "media", access = Access.READ_ONLY)
	private List<StorageDto> media;
	
	@JsonProperty("verified")
	private boolean verified;

}
