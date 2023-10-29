package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaqSubCategoryResponseDto extends FaqSubCategoryDto {
	@JsonProperty(value = "faq_category")
	private FaqCategoryDto faqCategory;
}
