package com.azguards.app.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnLinkInsituteDto {

	@JsonProperty("course_id")
	@NotEmpty(message = "{course_id.is_required}")
	private String courseId;

	@JsonProperty("delete_course")
	private boolean deleteCourse;
}
