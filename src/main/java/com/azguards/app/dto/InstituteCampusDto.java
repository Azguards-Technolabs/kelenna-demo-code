package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteCampusDto {

	@JsonProperty("institute_id")
	private String id;
	
	@JsonProperty("campus_name")
	private String campusName;
	
	@JsonProperty("latitude")
	private Double latitude;
	
	@JsonProperty("longitude")
	private Double longitude;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("website")
	private String website;
	
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("city_name")
	private String cityName;
	
	@JsonProperty("country_name")
	private String countryName;
	
	@JsonProperty("state_name")
	private String state;
	
	@JsonProperty("postal_code")
	private Integer postalCode;
	
	@JsonProperty("whatsapp_no")
	private String whatsNo;
	
	@JsonProperty("has_edit_access")
	private Boolean hasEditAccess;
	
	@JsonProperty("institute_timings")
	private List<DayTimingDto> instituteTimings;
}
