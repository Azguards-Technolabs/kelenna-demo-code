package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HelpDto {

	@JsonProperty("help_id")
	private String id;
	
	@JsonProperty("title")
	@NotBlank(message = "{title.is_required}")
	private String title;
	
	@JsonProperty("category_id")
	@NotBlank(message = "{category_id.is_required}")
	private String categoryId;
	
	@JsonProperty("subCategory_id")
	@NotBlank(message = "{subCategory_id.is_required}")
	private String subCategoryId;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("is_questioning")
	private Boolean isQuestioning;
	
	@JsonProperty("created_user")
	private String createdUser;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("assigned_user")
	private String assignedUser;
}
