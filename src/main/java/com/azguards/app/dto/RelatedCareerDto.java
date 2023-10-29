package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatedCareerDto {

	@JsonProperty("related_career_id")
	private String id;
	
	@JsonProperty("related_career_name")
	private String name;
	
	@JsonProperty("career_id")
	private String careerId;
}
