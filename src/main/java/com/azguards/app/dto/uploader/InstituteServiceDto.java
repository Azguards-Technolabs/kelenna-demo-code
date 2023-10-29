package com.azguards.app.dto.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstituteServiceDto {
	
	@JsonProperty("service_name")
	private String serviceName;
	
	@JsonProperty("service_csv")
	private String serviceCsv;
	
	@JsonProperty("facility_csv")
	private String facilityCsv;
	
	@JsonProperty("sport_facilities_csv")
	private String sportFacilitiesCsv;
}
