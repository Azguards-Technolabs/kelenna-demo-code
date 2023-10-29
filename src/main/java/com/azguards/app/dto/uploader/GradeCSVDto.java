package com.azguards.app.dto.uploader;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GradeCSVDto {
    
	@JsonProperty("country_name")
	@NotBlank(message = "{country_name should not be blank")
    private String country;
	
	@JsonProperty("education_system")
	@NotBlank(message = "{education_system should not be blank")
    private String educationSystem;
	
	@JsonProperty("state_name")
	@NotBlank(message = "{state_name should not be blank")
    private String stateName;
    
	@JsonProperty("grade_1")
    private String grade1;
	
	@JsonProperty("grade_2")
    private String grade2;
	
	@JsonProperty("grade_3")
    private String grade3;
	
	@JsonProperty("grade_4")
    private String grade4;
	
	@JsonProperty("grade_5")
    private String grade5;
	
	@JsonProperty("grade_6")
    private String grade6;
	
	@JsonProperty("grade_7")
    private String grade7;
	
	@JsonProperty("grade_8")
    private String grade8;
	
	@JsonProperty("grade_9")
    private String grade9;
	
	@JsonProperty("grade_10")
    private String grade10;
	
	@JsonProperty("grade_11")
    private String grade11;
	
	@JsonProperty("grade_12")
    private String grade12;
	
	@JsonProperty("grade_13")
    private String grade13;
	
	@JsonProperty("grade_14")
    private String grade14;
	
	@JsonProperty("grade_15")
    private String grade15;
	
	@JsonProperty("grade_16")
    private String grade16;
	
	@JsonProperty("gpa_grades_1")
    private String gpaGrades1;
	
	@JsonProperty("gpa_grades_2")
    private String gPaGrades2;
	
	@JsonProperty("gpa_grades_3")
    private String gPaGrades3;
	
	@JsonProperty("gpa_grades_4")
    private String gPaGrades4;
	
	@JsonProperty("gpa_grades_5")
    private String gPaGrades5;
	
	@JsonProperty("gpa_grades_6")
    private String gPaGrades6;
	
	@JsonProperty("gpa_grades_7")
    private String gPaGrades7;
	
	@JsonProperty("gpa_grades_8")
    private String gPaGrades8;
	
	@JsonProperty("gpa_grades_9")
    private String gPaGrades9;
	
	@JsonProperty("gpa_grades_10")
    private String gPaGrades10;
	
	@JsonProperty("gpa_grades_11")
    private String gPaGrades11;
	
	@JsonProperty("gpa_grades_12")
    private String gPaGrades12;
	
	@JsonProperty("gpa_grades_13")
    private String gPaGrades13;
	
	@JsonProperty("gpa_grades_14")
    private String gPaGrades14;
	
	@JsonProperty("gpa_grades_15")
    private String gPaGrades15;
	
	@JsonProperty("gpa_grades_16")
    private String gPaGrades16;
}
