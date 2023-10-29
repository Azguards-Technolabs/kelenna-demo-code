package com.azguards.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteFacilityDto {
	
	@JsonProperty("facilities")
	private List<FacilityDto> facilities = new ArrayList<FacilityDto>();

}
