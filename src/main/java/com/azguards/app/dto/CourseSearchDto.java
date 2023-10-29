package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseSearchDto {

	@JsonProperty("search_key")
	private String searchKey;

	@JsonProperty("course_name")
	private String courseName;

	@JsonProperty("is_profile_search")
	private Boolean isProfileSearch;

	@JsonProperty("currency_id")
	private String currencyId;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("course_keys")
	private List<String> courseKeys;

	@JsonProperty("level_ids")
	private List<String> levelIds;

	@JsonProperty("faculty_ids")
	private List<String> facultyIds;

	@JsonProperty("country_names")
	private List<String> countryNames;

	@JsonProperty("service_ids")
	private List<String> serviceIds;

	@JsonProperty("city_names")
	private List<String> cityNames;

	@JsonProperty("min_cost")
	private Double minCost;

	@JsonProperty("max_cost")
	private Double maxCost;

	@JsonProperty("min_duration")
	private Integer minDuration;

	@JsonProperty("max_duration")
	private Integer maxDuration;

	@JsonProperty("max_size_per_page")
	private Integer maxSizePerPage;

	@JsonProperty("page_number")
	private Integer pageNumber;

	@JsonProperty("sorting_obj")
	private CourseSearchFilterDto sortingObj;

	@JsonProperty("institute_id")
	private String instituteId;

	@JsonProperty("sort_asscending")
	private Boolean sortAsscending;

	@JsonProperty("sort_by")
	private String sortBy;

	@JsonProperty("currency_code")
	private String currencyCode;

	@JsonProperty("date")
	private String date;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;
	
	@JsonProperty("user_country_name")
	private String userCountryName;
}
