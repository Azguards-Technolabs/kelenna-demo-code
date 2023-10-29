package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
	@JsonProperty("job_id")
	@NotBlank(message = "{job_id.is_required}")
	private String id;
	
	@JsonProperty("job_name")
	@NotBlank(message = "{job_name.is_required}")
	private String job;
	
	@JsonProperty("job_description")
	@NotBlank(message = "{job_description.is_required}")
	private String jobDescription;

}
