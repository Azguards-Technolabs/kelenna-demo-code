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
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "courseDeliveryMode")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "course_delivery_mode_funding", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "funding_name_id",
		"course_delivery_mode_id" }, name = "UK_CDMF_N_FN_DM_ID"), indexes = {
				@Index(name = "IDX_D_ID", columnList = "course_delivery_mode_id", unique = false) })
public class CourseDeliveryModeFunding implements Serializable {

	private static final long serialVersionUID = 8492390790670110780L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@EqualsAndHashCode.Include
	@Column(name = "name")
	private String name;

	@EqualsAndHashCode.Include
	@Column(name = "funding_name_id")
	private String fundingNameId;

	@EqualsAndHashCode.Include
	@Column(name = "currency")
	private String currency;

	@EqualsAndHashCode.Include
	@Column(name = "amount")
	private Double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_delivery_mode_id", nullable = false)
	private CourseDeliveryModes courseDeliveryMode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
