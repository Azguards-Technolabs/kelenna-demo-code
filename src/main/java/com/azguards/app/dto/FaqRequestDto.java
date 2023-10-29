package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaqRequestDto extends FaqDto {

	@NotBlank(message = "{faq_sub_category_id.is_required}")
	@JsonProperty("faq_sub_category_id")
	private String faqSubCategoryId;
}
