package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteVerficationDto {
	@JsonProperty("institute_id")
	private String instituteId;
	
	@JsonProperty("about_us")
	private boolean aboutUs;

	@JsonProperty("courses")
	private boolean courses;

	@JsonProperty("teachers")
	private boolean teachers;

	@JsonProperty("reviews")
	private boolean reviews;

	@JsonProperty("services")
	private boolean services;

	@JsonProperty("gallery")
	private boolean gallery;

	@JsonProperty("faq")
	private boolean faq;

	@JsonProperty("application_procedure")
	private boolean applicationProcedure;
}
