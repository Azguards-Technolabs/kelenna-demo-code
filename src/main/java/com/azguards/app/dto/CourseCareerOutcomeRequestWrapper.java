package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CourseCareerOutcomeDto;

import lombok.Data;

@Data
public class CourseCareerOutcomeRequestWrapper {
	@Valid
	@NotEmpty(message = "{course_career_outcomes.is_required}")
	@JsonProperty("course_career_outcomes")
	ValidList<CourseCareerOutcomeDto> courseCareerOutcomeDtos;

	@NotNull(message = "{linked_course_ids.is_required}")
	@JsonProperty("linked_course_ids")
	List<String> linkedCourseIds;
}
