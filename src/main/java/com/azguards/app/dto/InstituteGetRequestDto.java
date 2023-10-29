package com.azguards.app.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteGetRequestDto {
    
	@JsonProperty("institute_id")
	private String id;
	
	@JsonProperty("country_name")
	@NotBlank(message = "{country_name.is_required}")
    private String countryName;
	
	@JsonProperty("city_name")
	@NotBlank(message = "{city_name.is_required}")
    private String cityName;
	
	@JsonProperty("institute_type")
    private List<String> instituteType;
	
	@JsonProperty("name")
	@NotBlank(message = "{name.is_required}")
    private String name;
	
	@JsonProperty("institute_youtubes")
    private List<String> instituteYoutubes;
	
	@JsonProperty("course_count")
    private Integer courseCount;
	
	@JsonProperty("latitude")
    private Double latitude;
	
	@JsonProperty("longitude")
    private Double longitude;
	
	@JsonProperty("total_student")
    private Integer totalStudent;
	
	@JsonProperty("world_ranking")
    private Integer worldRanking;
	
	@JsonProperty("accreditation")
    private String accreditation;
	
	@JsonProperty("email")
    private String email;
	
	@JsonProperty("phone_number")
    private String phoneNumber;
	
	@JsonProperty("website")
    private String website;
	
	@JsonProperty("address")
    private String address;
	
	@JsonProperty("avg_cost_of_living")
    private String avgCostOfLiving;
	
	@JsonProperty("description")
    private String description;
	
	@JsonProperty("tag_line")
	private String tagLine;
	
	@JsonProperty("verified")
	private boolean verified;

}