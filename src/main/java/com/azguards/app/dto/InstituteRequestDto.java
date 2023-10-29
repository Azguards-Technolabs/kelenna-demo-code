package com.azguards.app.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azguards.common.lib.dto.institute.ProviderCodeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class InstituteRequestDto extends InstituteDto {
	
	@JsonProperty("business_account_type")
	private String businessAccountType;
	
	@JsonProperty("institute_affiliaction_type")
	private String instituteAffiliactionType;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("avg_cost_of_living")
	private String avgCostOfLiving;
	
	@JsonProperty("enrolment_link")
	private String enrolmentLink;
	
	@JsonProperty("tuition_fess_payment_plan")
	private String tuitionFessPaymentPlan;
	
	@JsonProperty("scholarship_financing_assistance")
	private String scholarshipFinancingAssistance;
	
	@JsonProperty("institute_category_type_id")
	private String instituteCategoryTypeId;
	
	@JsonProperty("accreditation")
	private List<String> accreditation;
	
	@JsonProperty("accreditation_details")
	private List<AccrediatedDetailDto> accreditationDetails;
	
	@JsonProperty("institute_timings")
	private List<DayTimingDto> instituteTimings; 
	
	/**
	 * There is no use of below fields in Admin panel.
	 */
	@JsonProperty("about_info")
	private String aboutInfo;
	
	@JsonProperty("course_start")
	private String courseStart;
	
	@JsonProperty("link")
	private String link;
	
	@JsonProperty("contact")
	private String contact;
	
	@JsonProperty("curriculum")
	private String curriculum;
	
	@JsonProperty("domestic_boarding_fee")
	private Double domesticBoardingFee;
	
	@JsonProperty("international_boarding_fee")
	private Double internationalBoardingFee;
	
	@JsonProperty("followers_count")
	private long followersCount;

	@JsonProperty("postal_code")
	private Integer postalCode;
	
	@JsonProperty("has_edit_access")
	private boolean editAccess;

	@JsonProperty("is_followed")
	private boolean followed;
	
	@Valid
	@JsonProperty("institute_fundings")
	@NotNull(message = "{institute_fundings.is_required}")
	private ValidList<InstituteFundingDto> instituteFundings = new ValidList<>();
	
	@Valid
	@JsonProperty("provider_codes")
	@NotNull(message = "{provider_codes.is_required}")
	private ValidList<ProviderCodeDto> instituteProviderCodes = new ValidList<>();
	
	@JsonProperty("verified")
	private boolean verified;
}