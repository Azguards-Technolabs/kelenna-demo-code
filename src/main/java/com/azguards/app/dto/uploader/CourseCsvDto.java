package com.azguards.app.dto.uploader;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseCsvDto implements Serializable {

	private static final long serialVersionUID = -7711864923352896014L;
	
	@JsonProperty("course_id")
	private String id;

	@JsonProperty("course_name")
	@NotEmpty(message = "{course_name is required")
	private String name;

	@JsonProperty("world_ranking")
	private Integer worldRanking;

	@JsonProperty("stars")
	private Integer stars;

	@JsonProperty("recognition")
	private String recognition;

	@JsonProperty("recognition_type")
	private String recognitionType;

	@JsonProperty("website")
	private String website;

	@JsonProperty("language")
	private String language;

	@JsonProperty("abbreviation")
	private String abbreviation;

	@JsonProperty("rec_date")
	private Date recDate;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("description")
	private String description;

	@JsonProperty("is_active")
	private Boolean isActive;

	@JsonProperty("created_on")
	private Date createdOn;

	@JsonProperty("updated_on")
	private Date updatedOn;

	@JsonProperty("deleted_on")
	private Date deletedOn;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("is_deleted")
	private Boolean isDeleted;

	@JsonProperty("faculty_name")
	@NotEmpty(message = "{faculty_name is required")
	private String facultyName;

	@JsonProperty("institute_name")
	@NotEmpty(message = "{institute_name is required")
	private String instituteName;

	@JsonProperty("country_name")
	@NotEmpty(message = "{country_name is required")
	private String countryName;

	@JsonProperty("city_name")
	@NotEmpty(message = "{city_name is required")
	private String cityName;

	@JsonProperty("level_code")
	@NotEmpty(message = "{level_code is required")
	private String levelCode;

	@JsonProperty("availabilty")
	private String availabilty;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("currency_time")
	private String currencyTime;

	@JsonProperty("cost_range")
	private Double costRange;

	@JsonProperty("content")
	private String content;

	@JsonProperty("content_2")
	private String content2;

	@JsonProperty("content_3")
	private String content3;

	@JsonProperty("content_4")
	private String content4;

	@JsonProperty("content_5")
	private String content5;

	@JsonProperty("content_6")
	private String content6;

	@JsonProperty("content_7")
	private String content7;

	@JsonProperty("content_8")
	private String content8;

	@JsonProperty("content_9")
	private String content9;

	@JsonProperty("content_10")
	private String content10;

	@JsonProperty("content_11")
	private String content11;

	@JsonProperty("content_12")
	private String content12;

	@JsonProperty("content_13")
	private String content13;

	@JsonProperty("content_14")
	private String content14;

	@JsonProperty("content_15")
	private String content15;

	@JsonProperty("content_16")
	private String content16;

	@JsonProperty("content_17")
	private String content17;

	@JsonProperty("content_18")
	private String content18;

	@JsonProperty("content_19")
	private String content19;

	@JsonProperty("content_20")
	private String content20;

	@JsonProperty("content_21")
	private String content21;

	@JsonProperty("content_22")
	private String content22;

	@JsonProperty("content_23")
	private String content23;

	@JsonProperty("content_24")
	private String content24;

	@JsonProperty("content_25")
	private String content25;

	@JsonProperty("curriculum")
	private String curriculum;

	@JsonProperty("examination_board")
	private String examinationBoard;

	@JsonProperty("domestic_application_fee")
	private double domesticApplicationFee;

	@JsonProperty("international_application_fee")
	private double internationalApplicationFee;

	@JsonProperty("domestic_enrollment_fee")
	private double domesticEnrollmentFee;

	@JsonProperty("international_enrollment_fee")
	private double internationalEnrollmentFee;

	@JsonProperty("domestic_boarding_fee")
	private double domesticBoardingFee;

	@JsonProperty("international_boarding_fee")
	private double internationalBoardingFee;

	@JsonProperty("entrance_exam")
	private String entranceExam;

	@JsonProperty("global_gpa")
	private Double globalGpa;
	
	@JsonProperty("classroom")
	private String classroom;
	
	@JsonProperty("blended")
	private String blended;
	
	@JsonProperty("online")
	private String online;
	
	@JsonProperty("distance")
	private String distance;
	
	@JsonProperty("full_time")
	private String fullTime;
	
	@JsonProperty("part_time")
	private String partTime;
	
	@JsonProperty("duration")
	private Double duration;

	@JsonProperty("duration_time")
	private String durationTime;

	@JsonProperty("domestic_fee")
	private Double domesticFee;

	@JsonProperty("international_fee")
	private Double internationalFee;
	
    private Double ieltsReading;
    
    private Double ieltsWriting;
    
    private Double ieltsSpeaking; 
    
    private Double ieltsListening;
    
    private Double ieltsOverall;
    
    private Double toflReading;
    
    private Double toflWriting;
    
    private Double toflSpeaking;
    
    private Double toflListening;
    
    private Double toflOverall;
    
    private String coursePreRequisiteCertificate01;
    
    private String coursePreRequisiteCertificate02;
    
    private String coursePreRequisiteCertificate03;
    
    private String preRequisiteSubject01;
    
    private String preRequisiteSubject02;
    
    private String preRequisiteSubject03;
    
    private String preRequisiteSubject04;
    
    private String preRequisiteSubject05;
    
    private String preRequisiteSubject06;
    
    private String preRequisiteSubject07;
    
    private String preRequisiteSubject08;
    
    private String preRequisiteSubject09;
    
    private String preRequisiteSubject10;
    
    private String preRequisiteSubject11;
    
    private String preRequisiteSubject12;
    
    private String preRequisiteSubject13;
    
    private String preRequisiteSubject14;

    private String preRequisiteSubjectGrade01;

    private String preRequisiteSubjectGrade02;
    
    private String preRequisiteSubjectGrade03;
    
    private String preRequisiteSubjectGrade04;
    
    private String preRequisiteSubjectGrade05;
    
    private String preRequisiteSubjectGrade06;
    
    private String preRequisiteSubjectGrade07;
    
    private String preRequisiteSubjectGrade08;
    
    private String preRequisiteSubjectGrade09;
    
    private String preRequisiteSubjectGrade10;
    
    private String preRequisiteSubjectGrade11;
    
    private String preRequisiteSubjectGrade12;
    
    private String preRequisiteSubjectGrade13;
    
    private String preRequisiteSubjectGrade14;
}
