package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "course")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "course_min_requirement", uniqueConstraints = @UniqueConstraint(columnNames = { "country_name",
		"state_name", "education_system_id", "course_id" }, name = "UK_COURSE_CN_SN_ESI_C"), indexes = {
				@Index(name = "IDX_COURSE_ID", columnList = "course_id", unique = false) })
public class CourseMinRequirement implements Serializable {

	private static final long serialVersionUID = 6903674843134844883L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@EqualsAndHashCode.Include
	@Column(name = "country_name", nullable = false)
	private String countryName;

	@EqualsAndHashCode.Include
	@Column(name = "state_name", nullable = false)
	private String stateName;

	@EqualsAndHashCode.Include
	@Column(name = "grade_point", nullable = false)
	private Double gradePoint;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "education_system_id", nullable = false)
	private EducationSystem educationSystem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

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

	@EqualsAndHashCode.Include
	@OneToMany(mappedBy = "courseMinRequirement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseMinRequirementSubject> courseMinRequirementSubjects = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> studyLanguages = new HashSet<>();
	
	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
