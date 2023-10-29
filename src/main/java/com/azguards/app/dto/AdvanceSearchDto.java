package com.azguards.app.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AdvanceSearchDto {

	@JsonProperty("faculties")
	private List<String> faculties;

	@JsonProperty("level_ids")
	private List<String> levelIds;

	@JsonProperty("service_ids")
	private List<String> serviceIds;

	@JsonProperty("country_names")
	private List<String> countryNames;

	@JsonProperty("course_keys")
	private List<String> courseKeys;

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

	@JsonProperty("sort_asscending")
	private boolean sortAsscending;

	@JsonProperty("sort_by")
	private String sortBy;

	@JsonProperty("max_size_per_page")
	@NotNull(message = "{max_size_per_page.is_required}")
	private Integer maxSizePerPage;

	@JsonProperty("page_number")
	@NotNull(message = "{page_number.is_required}")
	private Integer pageNumber;

	@JsonProperty("currency_code")
	@NotBlank(message = "{currency_code.is_required}")
	private String currencyCode;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("user_country_name")
	private String userCountryName;

	@JsonProperty("names")
	private List<String> names;

	@JsonProperty("search_keyword")
	private String searchKeyword;

	@JsonProperty("study_modes")
	private List<String> studyModes;

	@JsonProperty("delivery_methods")
	private List<String> deliveryMethods;

	@JsonProperty("institute_id")
	private String instituteId;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("initial_radius")
	private Integer initialRadius;
}
