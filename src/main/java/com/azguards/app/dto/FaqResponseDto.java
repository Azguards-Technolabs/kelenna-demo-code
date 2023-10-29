package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FaqResponseDto extends FaqDto {

	@JsonProperty(value = "faq_sub_category")
	private FaqSubCategoryResponseDto faqSubCategory;
}
