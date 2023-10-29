package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseScholarshipAndFacultyCountDto {

	@JsonProperty("course_count")
	long courseCount;

	@JsonProperty("scholarship_count")
	long scholarshipCount;

	@JsonProperty("faculty_count")
	long facultyCount;

}
