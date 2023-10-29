package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipLevelCountDto {

	@JsonProperty("level_id")
	private String levelId;

	@JsonProperty("level_code")
	private String levelCode;

	@JsonProperty("level_name")
	private String levelName;

	@JsonProperty("schoarship_count")
	private Long count;
}
