package com.azguards.app.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "course")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "course_english_eligibility", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id",
		"english_type" }, name = "UK_COURSE_ID_ENGLISH_TYPE"), indexes = {
				@Index(name = "IDX_COURSE_ID", columnList = "course_id", unique = false) })
public class CourseEnglishEligibility implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@EqualsAndHashCode.Include
	@Column(name = "english_type", nullable = false)
	private String englishType;

	@EqualsAndHashCode.Include
	@Column(name = "reading", precision = 10)
	private Double reading;

	@EqualsAndHashCode.Include
	@Column(name = "writing", precision = 10)
	private Double writing;

	@EqualsAndHashCode.Include
	@Column(name = "speaking", precision = 10)
	private Double speaking;

	@EqualsAndHashCode.Include
	@Column(name = "listening", precision = 10)
	private Double listening;

	@EqualsAndHashCode.Include
	@Column(name = "overall", precision = 10)
	private Double overall;

	@EqualsAndHashCode.Include
	@Column(name = "is_active")
	private Boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_on", length = 19)
	private Date deletedOn;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Column(name = "updated_by", length = 50)
	private String updatedBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	public CourseEnglishEligibility(String englishType, Double reading, Double writing, Double speaking,
			Double listening, Double overall, Boolean isActive, Date createdOn, Date updatedOn, Date deletedOn,
			String createdBy, String updatedBy, Boolean isDeleted) {
		super();
		this.englishType = englishType;
		this.reading = reading;
		this.writing = writing;
		this.speaking = speaking;
		this.listening = listening;
		this.overall = overall;
		this.isActive = isActive;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.deletedOn = deletedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.isDeleted = isDeleted;
	}

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
