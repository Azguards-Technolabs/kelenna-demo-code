package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "course_delivery_modes", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "study_mode",
		"delivery_type", "is_government_eligible", "accessibility", "duration", "duration_time" }, name = "UK_CDM_CI_SM_DT_GE_AC_DU_DT"), indexes = {
				@Index(name = "IDX_COURSE_ID", columnList = "course_id", unique = false) })
public class CourseDeliveryModes implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;

	@EqualsAndHashCode.Include
	@Column(name = "delivery_type", nullable = false)
	private String deliveryType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@EqualsAndHashCode.Include
	@Column(name = "duration", nullable = false)
	private Double duration;

	@EqualsAndHashCode.Include
	@Column(name = "duration_time", nullable = false)
	private String durationTime;

	@EqualsAndHashCode.Include
	@Column(name = "study_mode", nullable = false)
	private String studyMode;

	@Column(name = "is_government_eligible", nullable = false)
	private Boolean isGovernmentEligible;

	@Column(name = "accessibility", nullable = false)
	private String accessibility;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	private Date createdOn;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	private Date updatedOn;

	@Column(name = "updated_by", length = 50)
	private String updatedBy;

	@EqualsAndHashCode.Include
	@OneToMany(mappedBy = "courseDeliveryMode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseFees> fees = new ArrayList<>();

	@EqualsAndHashCode.Include
	@OneToMany(mappedBy = "courseDeliveryMode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseDeliveryModeFunding> fundings = new ArrayList<>();

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
