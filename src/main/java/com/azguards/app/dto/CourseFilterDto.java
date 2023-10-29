package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseFilterDto {

	@JsonProperty("country_name")
	private String countryName;
	
	@JsonProperty("institute_id")
	private String instituteId;
	
	@JsonProperty("faculty_id")
	private String facultyId;
	
	@JsonProperty("course_id")
	private String courseId;
	
	@JsonProperty("languages")
	private List<String> languages;
	
	@JsonProperty("min_ranking")
	private Integer minRanking;
	
	@JsonProperty("max_ranking")
	private Integer maxRanking;
	
	@JsonProperty("currency_code")
	private String currencyCode;

	/**
	 * This is used to get user country Id, as it will be used to determine which
	 * courses to show based on user country like courses with availability A,D,I ,
	 * the above mentioned country will be available as filter while this will be
	 * derived based on logged in user country
	 */
	
	@JsonProperty("user_country_name")
	private String userCountryName;
}
