package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseLinkedInstituteDto {

	@JsonProperty("institute")
	private InstituteResponseDto institute;

	@JsonProperty("course_id")
	private String courseId;

	@JsonProperty("has_course_edit_access")
	private boolean hasCourseEditAccess;
}
