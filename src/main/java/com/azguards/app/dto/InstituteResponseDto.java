package com.azguards.app.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.TimingDto;
import com.azguards.common.lib.dto.storage.StorageDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class InstituteResponseDto extends InstituteDto {

	@JsonProperty("location")
	private String location;

	@JsonProperty("total_count")
	private Integer totalCount;

	@JsonProperty("about_us")
	private String aboutUs;

	@JsonProperty("visa_requirement")
	private String visaRequirement;

	@JsonProperty("total_available_jobs")
	private String totalAvailableJobs;

	@JsonProperty("storage_list")
	private List<StorageDto> storageList;

	@JsonProperty("distance")
	private Double distance;

	@JsonProperty("min_price_range")
	private Double minPriceRange;

	@JsonProperty("max_price_range")
	private Double maxPriceRange;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("institute_timing")
	private TimingDto instituteTiming;
	
	public InstituteResponseDto(String id, String name, Integer worldRanking, String cityName, String countryName,
			String stateName, String website, String aboutUs,
			Double latitude, Double longitude, String phoneNumber, String whatsNo, Long totalCourses, String email, String address,
			Integer domesticRanking, String tagLine, Date createdOn) {
		super.setId(id);
		super.setName(name);
		super.setWorldRanking(worldRanking);
		super.setCityName(cityName);
		super.setCountryName(countryName);
		super.setStateName(stateName);
		super.setWebsite(website);
		this.aboutUs = aboutUs;
		super.setTotalCourses(totalCourses.intValue());
		super.setLatitude(latitude);
		super.setLongitude(longitude);
		super.setPhoneNumber(phoneNumber);
		super.setWhatsNo(whatsNo);
		super.setEmail(email);
		super.setAddress(address);
		super.setDomesticRanking(domesticRanking);
		super.setTagLine(tagLine);
		super.setCreatedOn(createdOn);
	}

}
