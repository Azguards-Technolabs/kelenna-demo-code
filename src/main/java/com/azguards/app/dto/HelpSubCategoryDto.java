package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HelpSubCategoryDto {

	@JsonProperty("help_sub_category_id")
    private String id;
	
	@JsonProperty("name")
    private String name;
	
	@JsonProperty("category_id")
    private String categoryId;
	
	@JsonProperty("help_count")
    private Integer helpCount;
}
