package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteRecommendPageRequestDto {

	@JsonProperty("institute_recommend_page_request_id")
	private String instituteRecommendRequestId;
	
	@NotBlank(message = "{institute_name.is_required}")
	@JsonProperty("institute_name")
	private String instituteName;
	
	@NotNull(message = "{is_working.is_required}")
	@JsonProperty("is_working")
	private Boolean isWorking;
	
	@NotNull(message = "{knowing_someone_working.is_required}")
	@JsonProperty("knowing_someone_working")
	private Boolean knowingSomeoneWorking;
	
	@NotBlank(message = "{name.is_required}")
	@JsonProperty("name")
	private String name;
	
	@NotBlank(message = "{email.is_required}")
	@JsonProperty("email")
	private String email;
	
	@NotBlank(message = "{phone_number.is_required}")
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	@NotBlank(message = "{title.is_required}")
	@JsonProperty("title")
	private String title;
	
	@NotBlank(message = "{address_institute.is_required}")
	@JsonProperty("address_institute")
	private String addressInstitute;
	
	@NotBlank(message = "{website.is_required}")
	@JsonProperty("website")
	private String website;

}
