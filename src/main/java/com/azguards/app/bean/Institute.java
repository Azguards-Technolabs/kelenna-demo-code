package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institute", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "country_name", "city_name" }, name = "UK_INSTITUTE_NA_CN_CN"), indexes = {
				@Index(name = "IDX_INSTITUTE_NAME", columnList = "name", unique = false) })
public class Institute implements Serializable {

	private static final long serialVersionUID = 8492390790670110780L;

	@Id
	@GenericGenerator(name = "CustomUUIDGenerator", strategy = "com.azguards.app.util.CustomUUIDGenerator", parameters = {})
	@GeneratedValue(generator = "CustomUUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;

	@Column(name = "readable_id", nullable = false, updatable = false, unique = true)
	private String readableId;
	
	@Column(name = "business_account_type")
	private String businessAccountType;
	
	@Column(name = "institute_affiliaction_type")
	private String instituteAffiliactionType;
	
	@Column(name = "institute_type")
	@Convert(converter = ListToStringConverter.class)
	private List <String> instituteType;

	@Column(name = "name")
	private String name;
	
	@Column(name = "is_active")
	private Boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_on")
	private Date deletedOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "world_ranking")
	private Integer worldRanking;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "website", columnDefinition = "TEXT")
	private String website;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "address", columnDefinition = "TEXT")
	private String address;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "country_name", length = 50)
	private String countryName;

	@Column(name = "city_name", length = 100)
	private String cityName;

	@Column(name = "avg_cost_of_living")
	private String avgCostOfLiving;

	@Column(name = "tution_fees_plan", columnDefinition = "TEXT")
	private String tuitionFeesPaymentPlan;

	@Column(name = "enrolment_link", columnDefinition = "TEXT")
	private String enrolmentLink;

	@Column(name = "about_us_info", columnDefinition = "TEXT")
	private String aboutInfo;

	@Column(name = "course_start")
	private String courseStart;

	@Column(name = "whatsapp_no")
	private String whatsNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institute_category_type_id")
	private InstituteCategoryType instituteCategoryType;

	@Column(name = "scholarship_financing_assistance", columnDefinition = "TEXT")
	private String scholarshipFinancingAssistance;

	@Column(name = "domestic_ranking")
	private Integer domesticRanking;

	@Column(name = "admission_email")
	private String admissionEmail;

	@Column(name = "boarding_available")
	private String boardingAvailable;

	@Column(name = "boarding")
	private String boarding;

	@Column(name = "state")
	private String state;

	@Column(name = "postal_code")
	private Integer postalCode;


	@Column(name = "english_partners")
	private String englishPartners;

	@Column(name = "img_count")
	private String imageCount;

	@Column(name = "climate")
	private String climate;

	@Column(name = "youtube_link")
	private String youtubeLink;

	@Column(name = "international_phone_number")
	private String internationalPhoneNumber;

	@Column(name = "domestic_phone_number")
	private String domesticPhoneNumber;

	@Column(name = "accreditation")
	private String accreditation;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "curriculum")
	private String curriculum;
	
	@Column(name = "domestic_boarding_fee")
	private Double domesticBoardingFee;
	
	@Column(name = "international_boarding_fee")
	private Double internationalBoardingFee;
	
	@Column(name = "tag_line")
	private String tagLine;
	
	@Column(name = "show_suggestion")
	private Boolean showSuggestion;
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteEnglishRequirements> instituteEnglishRequirements = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "institute")
	private InstituteAdditionalInfo instituteAdditionalInfo;
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteDomesticRankingHistory> instituteDomesticRankingHistories = new ArrayList<>();
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteFacility> instituteFacilities = new ArrayList<>();
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteService> instituteServices = new ArrayList<>();
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteIntake> instituteIntakes = new ArrayList<>();
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteWorldRankingHistory> instituteWorldRankingHistories = new ArrayList<>();
	
	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteFunding> instituteFundings = new ArrayList<>();

	@OneToMany(mappedBy = "institute" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstituteProviderCode> instituteProviderCodes = new ArrayList<>();
	
	@Column(name = "verified", nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean verified;
	
	@Column(name = "publicUrl")
	private String publicUrl;
		
}