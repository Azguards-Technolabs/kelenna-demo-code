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

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "scholarship")
@Table(name = "scholarship_eligible_nationality", uniqueConstraints = @UniqueConstraint(columnNames = { "country_name",
		"scholarship_id" }, name = "UK_SCHOLARSHIP_ELIGIBLE_NATIONALITY_CN_SI"), indexes = {
				@Index(name = "IDX_SCHOLARSHIP_ID", columnList = "scholarship_id", unique = false) })
public class ScholarshipEligibleNationality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "country_name")
	private String countryName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scholarship_id")
	private Scholarship scholarship;

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

	public void setAuditFields(String userId, ScholarshipEligibleNationality existingScholarshipNationality) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (existingScholarshipNationality != null) {
			this.setCreatedBy(existingScholarshipNationality.getCreatedBy());
			this.setCreatedOn(existingScholarshipNationality.getCreatedOn());
		}else {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
