package com.azguards.app.dto.uploader;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InstituteCsvDto {
	
	@JsonProperty("institute_id")
	private String id;

	@JsonProperty("institute_type")
	private String instituteType;

	@JsonProperty("institute_name")
	@NotEmpty(message = "{institute_name is required")
	private String name;

	@JsonProperty("world_ranking")
	private Integer worldRanking;

	@JsonProperty("accredited")
	private String accredited;

	@JsonProperty("phone_number")
	private String phoneNumber;

	@JsonProperty("email")
	private String email;

	@JsonProperty("website")
	private String website;

	@JsonProperty("total_student")
	private Integer totalStudent;

	@JsonProperty("latitude")
	private String latitude;

	@JsonProperty("longitude")
	private String longitude;

	@JsonProperty("address")
	private String address;

	@JsonProperty("description")
	private String description;

	@JsonProperty("country_name")
	@NotEmpty(message = "{country_name is required")
	private String countryName;

	@JsonProperty("city_name")
	@NotEmpty(message = "{city_name is required")
	private String cityName;

	@JsonProperty("avg_cost_of_living")
	private String avgCostOfLiving;

	@JsonProperty("tuition_fees_payment_plan")
	private String tuitionFeesPaymentPlan;

	@JsonProperty("opening_from")
	private String openingFrom;

	@JsonProperty("opening_to")
	private String openingTo;

	@JsonProperty("enrolment_link")
	private String enrolmentLink;

	@JsonProperty("about_info")
	private String aboutInfo;

	@JsonProperty("course_start")
	private String courseStart;

	@JsonProperty("whats_no")
	private String whatsNo;

	@JsonProperty("scholarship_financing_assistance")
	private String scholarshipFinancingAssistance;

	@JsonProperty("domestic_ranking")
	private Integer domesticRanking;

	@JsonProperty("admission_email")
	private String admissionEmail;

	@JsonProperty("boarding_available")
	private String boardingAvailable;

	@JsonProperty("boarding")
	private String boarding;

	@JsonProperty("state")
	@NotEmpty(message = "{state is required")
	private String state;

	@JsonProperty("postal_code")
	private Integer postalCode;

	@JsonProperty("facilities")
	private String facilities;

	@JsonProperty("services")
	private String services;

	@JsonProperty("sport_facilities")
	private String sportFacilities;

	@JsonProperty("english_partners")
	private String englishPartners;

	@JsonProperty("image_count")
	private String imageCount;

	@JsonProperty("climate")
	private String climate;

	@JsonProperty("youtube_link")
	private String youtubeLink;

	@JsonProperty("international_phone_number")
	private String internationalPhoneNumber;

	@JsonProperty("domestic_phone_number")
	private String domesticPhoneNumber;

	@JsonProperty("accreditation")
	private String accreditation;

	@JsonProperty("type")
	private String type;

//	@JsonProperty("levelCode")
//	private String levelCode;
//	
//	@JsonProperty("facultyName")
//	private String facultyName;

	@JsonProperty("intake_1")
	private String intake_1;

	@JsonProperty("intake_2")
	private String intake_2;

	@JsonProperty("intake_3")
	private String intake_3;

	@JsonProperty("intake_4")
	private String intake_4;

	@JsonProperty("intake_5")
	private String intake_5;

	@JsonProperty("intake_6")
	private String intake_6;

	@JsonProperty("intake_7")
	private String intake_7;

	@JsonProperty("intake_8")
	private String intake_8;

	@JsonProperty("intake_9")
	private String intake_9;

	@JsonProperty("intake_10")
	private String intake_10;

	@JsonProperty("intake_11")
	private String intake_11;

	@JsonProperty("intake_12")
	private String intake_12;

	@JsonProperty("monday")
	private String monday;

	@JsonProperty("tuesday")
	private String tuesday;

	@JsonProperty("wednesday")
	private String wednesday;

	@JsonProperty("thursday")
	private String thursday;

	@JsonProperty("friday")
	private String friday;

	@JsonProperty("saturday")
	private String saturday;

	@JsonProperty("sunday")
	private String sunday;
	
	@JsonProperty("personalCount")
	private int personalCount;
	
	@JsonProperty("visaWorkBenefits")
	private int visaWorkBenefits;
	
	@JsonProperty("empCareerDev")
	private int empCareerDev;

	@JsonProperty("studyLibrarySupport")
	private int studyLibrarySupport;
	
	@JsonProperty("healthServices")
	private int healthServices;
	
	@JsonProperty("disabilitySupport")
	private int disabilitySupport;
	
	@JsonProperty("childcareCentre")
	private int childcareCentre;
	
	@JsonProperty("cultInclAntiracismPrg")
	private int cultInclAntiracismPrg;
	
	@JsonProperty("techServ")
	private int techServ;
	
	@JsonProperty("accommodation")
	private int accommodation;
	
	@JsonProperty("medical")
	private int medical;
	
	@JsonProperty("legService")
	private int legService;
	
	@JsonProperty("acctServ")
	private int acctServ;
	
	@JsonProperty("bus")
	private int bus;
	
	@JsonProperty("train")
	private int train;
	
	@JsonProperty("airportPickup")
	private int airportPickup;
	
	@JsonProperty("swimmingPool")
	private int swimmingPool;
	
	@JsonProperty("sportCenter")
	private int sportCenter;
	
	@JsonProperty("sprotTeam")
	private int sprotTeam;
	
	@JsonProperty("housingServices")
	private int housingServices;
	
	@JsonProperty("adminServiceFee")
	private int adminServiceFee;
	
	@JsonProperty("paymentPlan")
	private int paymentPlan;
	
	@JsonProperty("governmentLoan")
	private int governmentLoan;
	
	@JsonProperty("scholarship")
	private int scholarship;

}
