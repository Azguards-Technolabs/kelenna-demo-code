package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.storage.StorageDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstituteServiceResponseDto extends InstituteServiceDto {
	@JsonProperty("media")
	private List<StorageDto> media;
}
