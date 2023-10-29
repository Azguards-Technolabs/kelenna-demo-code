package com.azguards.app.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.storage.StorageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstituteJoinRequestDto {

	@JsonProperty("institute_join_request_id")
	private String instituteJoinRequestId;
	
	@JsonProperty("user_id")
	private String userId;
	
	@NotBlank(message = "{institute_country.is_required}")
	@JsonProperty("institute_country")
	private String instituteCountry;
	
	@NotBlank(message = "{institute_name.is_required}")
	@JsonProperty("institute_name")
	private String instituteName;
	
	@NotBlank(message = "{institute_type.is_required}")
	@JsonProperty("institute_type")
	private String instituteType;
	
	@NotBlank(message = "{type.is_required}")
	@JsonProperty("type")
	private String type;
	
	@NotBlank(message = "{first_name.is_required}")
	@JsonProperty("first_name")
	private String firstName;
	
	@NotBlank(message = "{last_name.is_required}")
	@JsonProperty("last_name")
	private String lastName;
	
	@NotBlank(message = "{title.is_required}")
	@JsonProperty("title")
	private String title;
	
	@NotBlank(message = "{email.is_required}")
	@JsonProperty("email")
	private String email;
	
	@NotBlank(message = "{phone_number.is_required}")
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	@NotBlank(message = "{management_name.is_required}")
	@JsonProperty("management_name")
	private String managementName;
	
	@NotBlank(message = "{management_email.is_required}")
	@JsonProperty("management_email")
	private String managementEmail;
	
	@NotBlank(message = "{management_phone_number.is_required}")
	@JsonProperty("management_phone_number")
	private String managementPhoneNumber;
	
	@JsonProperty("certificates")
	List<StorageDto> certificates = new ArrayList<StorageDto>();
}
