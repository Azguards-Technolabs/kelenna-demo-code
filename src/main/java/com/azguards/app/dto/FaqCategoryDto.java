package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class FaqCategoryDto {
	@JsonProperty(value = "faq_category_id", access = Access.READ_ONLY)
	private String id;

	@JsonProperty("name")
	@NotBlank(message = "{name.is_required}")
	private String name;
}
