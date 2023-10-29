package com.azguards.app.dto.uploader;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class CareerCSVDto {
	
	@CsvBindByName(column = "Career List")
	private String careerList;
	
	@CsvBindByName(column = "Jobs")
	private String jobs;
	
	@CsvBindByName(column = "Levels")
	private String levels;
	
	@CsvBindByName(column = "Course Level")
	private Integer courseLevel;
	
	@CsvBindByName(column = "Course Search Keywords")
	private String courseSearchKeywords;
	
	@CsvBindByName(column = "Job Description")
	private String jobDescription;

	@CsvBindByName(column = "Job Responsibility")
	private String jobResponsibility;
	
	@CsvBindByName(column = "Work Activities")
	private String workActivities;
	
	@CsvBindByName(column = "Job Types")
	private String jobTypes;
	
	@CsvBindByName(column = "Related Careers")
	private String relatedCareers;
	
	@CsvBindByName(column = "Skills")
	private String skills;
	
	@CsvBindByName(column = "Subjects")
	private String subjects;
	
	@CsvBindByName(column = "Work Styles")
	private String workStyles;
}
