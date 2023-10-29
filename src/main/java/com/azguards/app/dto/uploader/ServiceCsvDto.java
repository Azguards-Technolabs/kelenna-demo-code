package com.azguards.app.dto.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCsvDto {
	@JsonProperty("service_name")
	private String serviceName;
	
	@JsonProperty("description")
	private String description;
}
