package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteFacultyDto {

	@JsonProperty("faculty_id")
	private String id;
	
	@JsonProperty("faculty_name")
	private String name;
	
	@JsonProperty("course_count")
	private long courseCount;
	
}
