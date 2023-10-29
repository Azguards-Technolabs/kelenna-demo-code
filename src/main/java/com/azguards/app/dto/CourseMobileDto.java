package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseMobileDto {
	
	@JsonProperty("course_id")
	private String courseId;

	@NotBlank(message = "{course_name.is_required}")
	@JsonProperty("course_name")
	private String courseName;
	
	@NotBlank(message = "{course_description.is_required}")
	@JsonProperty("course_description")
	private String courseDescription;
	
	@NotBlank(message = "{faculty_id.is_required}")
	@JsonProperty("faculty_id")
	private String facultyId;
	
	@JsonProperty("faculty_name")
	private String facultyName;
	
	@NotNull(message = "{gpa_required.is_required}")
	@JsonProperty("gpa_required")
	private Double gpaRequired;
	
	@NotNull(message = "{usd_domestic_fee.is_required}")
	@JsonProperty("usd_domestic_fee")
	private Double usdDomesticFee;
	
	@NotNull(message = "{usd_international_fee.is_required}")
	@JsonProperty("usd_international_fee")
	private Double usdInternationalFee;
	
	@NotNull(message = "{duration.is_required}")
	@JsonProperty("duration")
	private Double duration;
	
	@NotBlank(message = "{duration_unit.is_required}")
	@JsonProperty("duration_unit")
	private String durationUnit;
}
