package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class FaqDto {

	@JsonProperty(value = "faq_id", access = Access.READ_ONLY)
	private String id;

	@NotBlank(message = "{title.is_required}")
	@JsonProperty("title")
	private String title;

	@NotBlank(message = "{description.is_required}")
	@JsonProperty("description")
	private String description;

	@NotBlank(message = "{entity_id.is_required}")
	@JsonProperty("entity_id")
	private String entityId;

	@NotBlank(message = "{entity_type.is_required}")
	@JsonProperty("entity_type")
	private String entityType;

}
