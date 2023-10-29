package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {

	@JsonProperty("institute_facility_id")
	private String instituteFacilityId;

	@JsonProperty("facility_name")
	private String facilityName;

	@JsonProperty("facility_id")
	private String facilityId;

}
