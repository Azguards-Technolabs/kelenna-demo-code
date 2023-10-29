package com.azguards.app.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseResearchProposalRequirementDto {

	@JsonProperty("id")
	private String id;

	@NotEmpty
	@JsonProperty("description")
	private String description;

}
