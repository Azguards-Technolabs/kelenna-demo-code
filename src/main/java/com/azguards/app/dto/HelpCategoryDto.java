package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HelpCategoryDto {

	@JsonProperty("help_category_id")
    private String id;
	
	@JsonProperty("name")
	@NotBlank(message = "{name.is_required}")
    private String name;

}
