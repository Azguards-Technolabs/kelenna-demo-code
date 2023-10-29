package com.azguards.app.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.common.VaccinationDto;

import lombok.Data;

@Data
public class CourseVaccineRequirementDto {

	@JsonProperty("id")
	private String id;

	@NotEmpty
	@JsonProperty("description")
	private String description;

	@NotNull
	@JsonProperty("vaccination")
	private Set<VaccinationDto> vaccination;

}
