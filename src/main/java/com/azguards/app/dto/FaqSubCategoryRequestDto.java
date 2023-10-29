package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaqSubCategoryRequestDto extends FaqSubCategoryDto {
	@JsonProperty(value = "faq_category_id")
	@NotBlank(message = "{faq_category_id.is_required}")
	private String faqCategoryId;
}
