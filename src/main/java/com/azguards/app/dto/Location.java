package com.azguards.app.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "latitude", "longituide" })
public class Location {

	@JsonProperty("latitude")
	private Double lat;
	
	@JsonProperty("longituide")
	private Double lng;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();
}