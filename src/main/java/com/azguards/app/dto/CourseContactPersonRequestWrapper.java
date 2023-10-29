package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CourseContactPersonDto;

import lombok.Data;

@Data
public class CourseContactPersonRequestWrapper {
	@Valid
	@NotEmpty(message = "{course_contact_persons.is_required}")
	@JsonProperty("course_contact_persons")
	ValidList<CourseContactPersonDto> courseContactPersonDtos;

	@NotNull(message = "{linked_course_ids.is_required}")
	@JsonProperty("linked_course_ids")
	List<String> linkedCourseIds;
}
