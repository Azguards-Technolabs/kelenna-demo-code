package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.entitylistener.CourseUpdateListener;
import com.azguards.common.lib.enumeration.CourseTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(CourseUpdateListener.class)
@Table(name = "course", uniqueConstraints = @UniqueConstraint(name = "UK_COURSE_F_L_I_N_C", columnNames = { "faculty_id",
		"level_id", "institute_id", "name", "code" }), indexes = {
				@Index(name = "IDX_FACULTY_ID", columnList = "faculty_id", unique = false),
				@Index(name = "IDX_INSTITUTE_ID", columnList = "institute_id", unique = false),
				@Index(name = "IDX_LEVEL_ID", columnList = "level_id", unique = false),
				@Index(name = "IDX_COURSE_NAME", columnList = "name", unique = false),
				@Index(name = "IDX_COURSE_CURRICULUM", columnList = "curriculum_id", unique = false) })
public class Course implements Serializable {

	private static final long serialVersionUID = 8492390790670110780L;

	@Id
	@GenericGenerator(name = "CustomUUIDGenerator", strategy = "com.azguards.app.util.CustomUUIDGenerator", parameters = {})
	@GeneratedValue(generator = "CustomUUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "readable_id", nullable = false, updatable = false, unique = true)
	private String readableId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faculty_id", nullable = false)
	private Faculty faculty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institute_id", nullable = false)
	private Institute institute;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id")
	private Level level;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curriculum_id")
	private CourseCurriculum courseCurriculum;

	@EqualsAndHashCode.Include
	@Column(name = "name", nullable = false)
	private String name;

	@EqualsAndHashCode.Include
	@Column(name = "world_ranking")
	private Integer worldRanking;

	@EqualsAndHashCode.Include
	@Column(name = "stars")
	private Integer stars;

	@EqualsAndHashCode.Include
	@Column(name = "recognition")
	private String recognition;

	@EqualsAndHashCode.Include
	@Column(name = "recognition_type")
	private String recognitionType;

	@EqualsAndHashCode.Include
	@Column(name = "website", columnDefinition = "TEXT")
	private String website;

	@EqualsAndHashCode.Include
	@Column(name = "phone_number")
	private String phoneNumber;

	@EqualsAndHashCode.Include
	@Column(name = "email")
	private String email;

	@EqualsAndHashCode.Include
	@Column(name = "abbreviation", columnDefinition = "TEXT")
	private String abbreviation;

	@EqualsAndHashCode.Include
	@Column(name = "remarks", columnDefinition = "TEXT")
	private String remarks;

	@EqualsAndHashCode.Include
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@EqualsAndHashCode.Include
	@Column(name = "is_active")
	private Boolean isActive;

	@EqualsAndHashCode.Include
	@Column(name = "availabilty")
	private String availabilty;

	@EqualsAndHashCode.Include
	@Column(name = "currency", nullable = false)
	private String currency;

	@EqualsAndHashCode.Include
	@Column(name = "currency_time")
	private String currencyTime;

	@EqualsAndHashCode.Include
	@Column(name = "global_gpa")
	private Double globalGpa;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Column(name = "updated_by", length = 50)
	private String updatedBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_date", length = 19)
	private Date recDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_on", length = 19)
	private Date deletedOn;

	@EqualsAndHashCode.Include
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@EqualsAndHashCode.Include
	@Column(name = "examination_board")
	private String examinationBoard;

	@EqualsAndHashCode.Include
	@Column(name = "domestic_application_fee")
	private Double domesticApplicationFee;

	@EqualsAndHashCode.Include
	@Column(name = "international_application_fee")
	private Double internationalApplicationFee;

	@EqualsAndHashCode.Include
	@Column(name = "domestic_enrollment_fee")
	private Double domesticEnrollmentFee;

	@EqualsAndHashCode.Include
	@Column(name = "international_enrollment_fee")
	private Double internationalEnrollmentFee;

	@EqualsAndHashCode.Include
	@Column(name = "usd_domestic_application_fee")
	private Double usdDomesticApplicationFee;

	@EqualsAndHashCode.Include
	@Column(name = "usd_international_application_fee")
	private Double usdInternationalApplicationFee;

	@EqualsAndHashCode.Include
	@Column(name = "usd_domestic_enrollment_fee")
	private Double usdDomesticEnrollmentFee;

	@EqualsAndHashCode.Include
	@Column(name = "usd_international_enrollment_fee")
	private Double usdInternationalEnrollmentFee;

	@EqualsAndHashCode.Include
	@Column(name = "entrance_exam")
	private String entranceExam;
	
	@EqualsAndHashCode.Include
	@Column(name = "audience")
	private String audience;

	@Column(name = "code")
	private String code = "NORMAL COURSE";
	
	@EqualsAndHashCode.Include
	@Column(name = "international_student_procedure_id")
	private String internationalStudentProcedureId;
	
	@EqualsAndHashCode.Include
	@Column(name = "domestic_student_procedure_id")
	private String domesticStudentProcedureId;


	@EqualsAndHashCode.Include
	@Enumerated(EnumType.STRING)
	@Column(name = "course_type")
	private CourseTypeEnum courseType = CourseTypeEnum.ON_CAMPUS;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private OffCampusCourse offCampusCourse;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CoursePayment coursePayment;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseDeliveryModes> courseDeliveryModes = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseEnglishEligibility> courseEnglishEligibilities = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseIntake courseIntake;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseLanguage> courseLanguages = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseMinRequirement> courseMinRequirements = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CoursePrerequisite> coursePrerequisites = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseCareerOutcome> courseCareerOutcomes = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseSemester> courseSemesters = new ArrayList<>();

	@OneToMany(mappedBy = "course" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseFunding> courseFundings = new ArrayList<>();

	@OneToMany(mappedBy = "course" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseContactPerson> courseContactPersons = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseScholarship courseScholarship;
	
	@OneToMany(mappedBy = "course" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseProviderCode> courseProviderCodes = new ArrayList<>();
	

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseVaccineRequirement courseVaccineRequirement;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseWorkExperienceRequirement courseWorkExperienceRequirement;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseWorkPlacementRequirement courseWorkPlacementRequirement;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
	private CourseResearchProposalRequirement courseResearchProposalRequirement;

	
	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}