package com.azguards.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteFilterDto {

	@JsonProperty("city_name")
	private String cityName;

	@JsonProperty("country_name")
	private String countryName;

	@JsonProperty("institute_id")
	private String instituteId;

	@JsonProperty("institute_type_id")
	private String instituteTypeId;

	@JsonProperty("duration_date")
	private Date durationDate;

	@JsonProperty("max_size_per_page")
	private Integer maxSizePerPage;

	@JsonProperty("page_number")
	private Integer pageNumber;

	@JsonProperty("world_ranking")
	private Integer worldRanking;

	@JsonProperty("date_posted")
	private String datePosted;
}
