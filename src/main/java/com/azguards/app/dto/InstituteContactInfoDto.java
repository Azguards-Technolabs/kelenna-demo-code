package com.azguards.app.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteContactInfoDto {

	@NotNull(message = "{latitude.is_required}")
	@JsonProperty("latitute")
	private Double latitute;
	
	@NotNull(message = "{longitude.is_required}")
	@JsonProperty("longitude")
	private Double longitude;
	
	@NotEmpty(message = "{address.is_required}")
	@JsonProperty("address")
	private String address;
	
	@NotEmpty(message = "{office_hours_from.is_required}")
	@JsonProperty("office_hours_from")
	private String officeHoursFrom;
	
	@NotEmpty(message = "{office_hours_to.is_required}")
	@JsonProperty("office_hours_to")
	private String officeHoursTo;
	
	@NotEmpty(message = "{website.is_required}")
	@JsonProperty("website")
	private String website;
	
	@NotEmpty(message = "{contact_number.is_required}")
	@JsonProperty("contact_number")
	private String contactNumber;
	
	@NotEmpty(message = "{email.is_required}")
	@JsonProperty("email")
	private String email;
	
	@NotEmpty(message = "{average_living_cost.is_required}")
	@JsonProperty("average_living_cost")
	private String averagelivingCost;
	
}
