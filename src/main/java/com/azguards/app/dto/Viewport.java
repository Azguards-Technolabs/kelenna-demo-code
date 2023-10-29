package com.azguards.app.dto;

import java.io.Serializable;
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
@JsonPropertyOrder({ "location", "southwest" })
public class Viewport implements Serializable {

	private static final long serialVersionUID = -7092845123879194575L;
	
	@JsonProperty("location")
	private Location location;
	
	@JsonProperty("southwest")
	private Southwest southwest;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

}
