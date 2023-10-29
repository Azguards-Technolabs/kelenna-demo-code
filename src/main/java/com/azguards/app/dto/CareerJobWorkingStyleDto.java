package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerJobWorkingStyleDto {

	@JsonProperty("working_style_id")
	@NotBlank(message = "{working_style_id.is_required}")
	private String id;
	
	@JsonProperty("working_style")
	@NotBlank(message = "{working_style.is_required}")
	private String workStyle;
	
	@JsonProperty("job_id")
	@NotBlank(message = "{job_id.is_required}")
	private String jobId;
	
	@JsonProperty("last_selected")
	private boolean lastSelected;
}
