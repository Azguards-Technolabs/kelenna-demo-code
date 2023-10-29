package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseSearchFilterDto {

	@JsonProperty("price")
	private String price;

	@JsonProperty("duration")
	private String duration;

	@JsonProperty("location")
	private String location;

	@JsonProperty("recognition")
	private String recognition;

	@JsonProperty("latest_course")
	private String latestCourse;

	@JsonProperty("institute")
	private String institute;

	@JsonProperty("course")
	private String course;

	@JsonProperty("world_ranking")
	private String worldRanking;
}
