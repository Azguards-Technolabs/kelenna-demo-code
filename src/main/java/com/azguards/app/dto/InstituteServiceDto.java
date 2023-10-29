package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.azguards.common.lib.dto.storage.StorageDto;
import lombok.Data;

@Data
@Valid
public class InstituteServiceDto {

	@JsonProperty("institute_service_id")
	private String id;

	@NotNull(message = "{service.is_required}")
	@JsonProperty("service")
	private ServiceDto service;

	@JsonProperty("description")
	private String description;

	@JsonProperty(value = "media", access = Access.READ_ONLY)
	private List<StorageDto> media;
}
