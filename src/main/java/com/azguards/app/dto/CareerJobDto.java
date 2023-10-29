package com.azguards.app.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CareerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerJobDto {

	@JsonProperty("job_id")
	@NotBlank(message = "{job_id.is_required}")
	private String id;
	
	@JsonProperty("job_name")
	@NotBlank(message = "{job_name.is_required}")
	private String job;
	
	@JsonProperty("job_description")
	@NotBlank(message = "{job_description.is_required}")
	private String jobDescription;
	
	@JsonProperty("career")
	CareerDto careers;
	
	@JsonProperty("working_styles")
	List<CareerJobWorkingStyleDto> careerJobWorkingStyles;

	@JsonProperty("working_activities")
	List<CareerJobWorkingActivityDto> careerJobWorkingActivities;
	
	@JsonProperty("job_skills")
	List<CareerJobSkillDto> careerJobSkills;

	@JsonProperty("last_selected")
	private boolean lastSelected;
}
