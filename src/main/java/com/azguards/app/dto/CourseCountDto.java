package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseCountDto {

	@JsonProperty("course_count")
	private long courseCount;
}
