package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CoursePreRequisiteDto;

import lombok.Data;

@Data
public class CoursePreRequisiteRequestWrapper {
	@Valid
	@NotEmpty(message = "{course_pre_requisites.is_required}")
	@JsonProperty("course_pre_requisites")
	ValidList<CoursePreRequisiteDto> coursePreRequisiteDtos;

	@NotNull(message = "{linked_course_ids.is_required}")
	@JsonProperty("linked_course_ids")
	List<String> linkedCourseIds;
}
