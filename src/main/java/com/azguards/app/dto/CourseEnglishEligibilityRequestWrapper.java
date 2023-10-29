package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CourseEnglishEligibilityDto;

import lombok.Data;

@Data
public class CourseEnglishEligibilityRequestWrapper {
	@Valid
	@NotEmpty(message = "{course_englist_eligibilities.is_required}")
	@JsonProperty("course_englist_eligibilities")
	ValidList<CourseEnglishEligibilityDto> courseEnglishEligibilityDtos;

	@NotNull(message = "{linked_course_ids.is_required}")
	@JsonProperty("linked_course_ids")
	List<String> linkedCourseIds;
}
