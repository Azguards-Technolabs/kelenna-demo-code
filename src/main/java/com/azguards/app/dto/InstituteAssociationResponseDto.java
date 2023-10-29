package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteAssociationResponseDto {

	@JsonProperty("institute_association_id")
	private String instituteAssociationId;
	
	@JsonProperty("latitude")
	private Double latitude;
	
	@JsonProperty("longitude")
	private Double longitude;
	
	@JsonProperty("campus_name")
	private String campusName;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("country")
	private String country;
	
	@JsonProperty("image_path")
	private String imagePath;
	
	@JsonProperty("association_type")
	private String associationType;
}
