package com.azguards.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserDto {

	private String id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private Date dob;
	private String countryOrgin;
	private String citizenship;
	private String email;
	private String username;
	private String mobileNo;
	private String skypeId;
	private String userEduInfo;
	private String dobStr;
	private String signUpType;
	private String socialAccountId;
	private String userType;
	private String whattsappNo;
	private String city;
	private String state;
	private String postalCode;
	private String address;
	private String imageName;
	private String imageURL;
	private String aboutMe;
	private String currencyCode;

	UserEducationProfileResponseDto userEducationProfileResponseDto =  null;

	UserAchivementsProfileResponseDto userAchivementsProfileResponseDto = null;

	private List<SkillProfileResponseDto> skills = new ArrayList<SkillProfileResponseDto>();

	List<UserEnglishQualificationProfileResponseDto> listOfUserEnglishQualificationProfileResponseDto = null;
	
	public UserDto() {

	}
}
