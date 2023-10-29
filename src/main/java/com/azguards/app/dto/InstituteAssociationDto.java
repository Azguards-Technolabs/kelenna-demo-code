package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteAssociationDto {

	@NotBlank(message = "{source_institute_id.is_required}")
	@JsonProperty("source_institute_id")
	private String sourceInstituteId;
	
	@NotBlank(message = "{destination_institute_id.is_required}")
	@JsonProperty("destination_institute_id")
	private String destinationInstituteId;
	
	@NotBlank(message = "{association_type.is_required}")
	@JsonProperty("association_type")
	private String associationType;
}
