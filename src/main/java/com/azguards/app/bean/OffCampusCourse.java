package com.azguards.app.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "off_campus_course", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "title" }, name = "UK_OFF_CAMPUS_TITLE"),
		@UniqueConstraint(columnNames = { "course_id" }, name = "UK_COURSE") })
public class OffCampusCourse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@EqualsAndHashCode.Include
	@Column(name = "title")
	private String title;

	@EqualsAndHashCode.Include
	@Column(name = "latitude")
	private Double latitude;

	@EqualsAndHashCode.Include
	@Column(name = "longitude")
	private Double longitude;

	@EqualsAndHashCode.Include
	@Column(name = "admin_fee")
	private Double adminFee;

	@EqualsAndHashCode.Include
	@Column(name = "material_fee")
	private Double materialFee;

	@EqualsAndHashCode.Include
	@Column(name = "address", columnDefinition = "TEXT")
	private String address;

	@EqualsAndHashCode.Include
	@Column(name = "country_name", length = 50)
	private String countryName;

	@EqualsAndHashCode.Include
	@Column(name = "city_name", length = 100)
	private String cityName;

	@EqualsAndHashCode.Include
	@Column(name = "state_name")
	private String stateName;

	@EqualsAndHashCode.Include
	@Column(name = "postal_code")
	private String postalCode;

	@EqualsAndHashCode.Include
	@Column(name = "location_help_required")
	private Boolean locationHelpRequired;

	@EqualsAndHashCode.Include
	@Column(name = "skip_location")
	private Boolean skipLocation;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "reference_course_id")
	private String reference_course_id;
	
	@OneToOne
	@JoinColumn(name = "course_id")
	private Course course;

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}