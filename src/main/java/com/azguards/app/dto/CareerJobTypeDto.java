package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CareerJobTypeDto {

	@JsonProperty("job_type_id")
	private String id;
	
	@JsonProperty("job_type")
	private String type;
	
	@JsonProperty("job_ids")
	private List<String> jobIds;

	@JsonProperty("last_selected")
	private boolean lastSelected;
}
