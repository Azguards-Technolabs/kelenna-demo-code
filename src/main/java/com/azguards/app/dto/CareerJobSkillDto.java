package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerJobSkillDto {

	@JsonProperty("career_job_skill_id")
	@NotBlank(message = "{career_job_skill_id.is_required}")
	private String id;
	
	@JsonProperty("skill")
	@NotBlank(message = "{skill.is_required}")
	private String skill;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("job_id")
	@NotBlank(message = "{job_id.is_required}")
	private String jobId;
	
	@JsonProperty("last_selected")
	private boolean lastSelected;
	
}
