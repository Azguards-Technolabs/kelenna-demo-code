package com.azguards.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

	@JsonProperty("course_id")
    private String id;
    
	@JsonProperty("level_id")
	private String levelId;
	
	@JsonProperty("name")
    private String name;
	
	@JsonProperty("world_ranking")
    private String worldRanking;
	
	@JsonProperty("stars")
    private String stars;
	
	@JsonProperty("faculty_name")
    private String facultyName;
	
	@JsonProperty("level_name")
    private String levelName;
	
	@JsonProperty("cost_of_living")
    private String costOfLiving;
	
	@JsonProperty("description")
    private String description;
	
	@JsonProperty("remarks")
    private String remarks;
	
	@JsonProperty("institute_name")
	private String instituteName;
	
	@JsonProperty("course_delivery_modes")
	private List<CourseDeliveryModesDto> courseDeliveryModes;
	
	@JsonProperty("international_student_procedure_id")
	private String internationalStudentProcedureId;
	
	@JsonProperty("domestic_student_procedure_id")
	private String domesticStudentProcedureId;
}
