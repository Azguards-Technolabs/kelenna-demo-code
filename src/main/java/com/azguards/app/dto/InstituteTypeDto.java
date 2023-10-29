package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class  InstituteTypeDto {
	
	@JsonProperty("institute_type_id")
	private String instituteTypeId;
	
	@JsonProperty("institute_type_name")
	@NotBlank(message = "{institute_type_name.is_required}")
	private String instituteTypeName;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("country_name")
	private String countryName;
}
