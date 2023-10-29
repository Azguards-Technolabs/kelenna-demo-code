package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerJobWorkingActivityDto {

	@JsonProperty("working_activity_id")
	@NotBlank(message = "{working_activity_id.is_required}")
	private String id;
	
	@JsonProperty("working_activity")
	@NotBlank(message = "{working_activity.is_required}")
	private String workActivity;
	
	@JsonProperty("job_id")
	@NotBlank(message = "{job_id.is_required}")
	private String jobId;
}
