package com.azguards.app.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteFundingDto {

	@JsonProperty("institute_funding_id")
	private String id;

	@JsonProperty(value = "funding_name_id")
	@NotEmpty(message = "{funding_name_id.is_required}")
	private String fundingNameId;
}
