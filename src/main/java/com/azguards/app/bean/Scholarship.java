package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "scholarship", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "institute_id",
		"faculty_id" }, name = "UK_SCHOLARSHIP_N_FI_II"), indexes = {
				@Index(name = "IDX_INSTITUTE_ID", columnList = "institute_id", unique = false),
				@Index(name = "IDX_FACULTY_ID", columnList = "faculty_id", unique = false) })
public class Scholarship implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "readable_id", nullable = false, updatable = false, unique = true)
	private String readableId;
	
	@Column(name = "description")
	private String description;

	@Column(name = "scholarship_award")
	private String scholarshipAward;

	@ManyToMany
	@JoinTable(name = "scholarship_level", joinColumns = @JoinColumn(name = "level_id"), inverseJoinColumns = @JoinColumn(name = "scholarship_id"))
	private Set<Level> levels = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faculty_id", nullable = false)
	private Faculty faculty;

	@Column(name = "number_of_avaliability")
	private Integer numberOfAvaliability;

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "is_percentage_amount", nullable = false)
	private Boolean isPercentageAmount = false;

	@Column(name = "validity", nullable = false)
	private String validity;

	@Column(name = "how_to_apply")
	private String howToApply;

	@Column(name = "gender")
	private String gender;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	private Date updatedOn;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Column(name = "updated_by", length = 50)
	private String updatedBy;

	@Column(name = "website")
	private String website;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institute_id", nullable = false)
	private Institute institute;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "benefits", columnDefinition = "text")
	private String benefits;

	@Column(name = "requirements", columnDefinition = "text")
	private String requirements;

	@Column(name = "conditions", columnDefinition = "text")
	private String conditions;

	@Column(name = "successful_canidates", columnDefinition = "text")
	private String successfulCanidates;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = false;
	
	@OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScholarshipIntake> scholarshipIntakes = new ArrayList<>();

	@OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScholarshipLanguage> scholarshipLanguages = new ArrayList<>();

	@OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScholarshipCountry> scholarshipCountries = new ArrayList<>();

	@OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScholarshipEligibleNationality> scholarshipEligibleNationalities = new ArrayList<>();

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
