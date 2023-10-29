package com.azguards.app.dto.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectCsvDto {
	@JsonProperty("country_name")
	private String countryName;

	@JsonProperty("state_name")
	private String stateName;

	@JsonProperty("education_system_name")
	private String educationSystemName;

	@JsonProperty("code")
	private String code;

	@JsonProperty("name")
	private String name;
}
