package com.azguards.app.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.enumeration.TimingType;
import com.azguards.common.lib.enumeration.EntityTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "timing", uniqueConstraints = @UniqueConstraint(name = "UK_TIMING_ET_TT_EI", columnNames = { "entity_type",
		"timing_type", "entity_id" }), indexes = {
				@Index(name = "IDX_ENTITY_ID", columnList = "entity_type, entity_id", unique = false) })
public class Timing {

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@EqualsAndHashCode.Include
	@Column(name = "monday")
	private String monday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "tuesday")
	private String tuesday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "wednesday")
	private String wednesday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "thursday")
	private String thursday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "friday")
	private String friday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "saturday")
	private String saturday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "sunday")
	private String sunday = "CLOSED";

	@EqualsAndHashCode.Include
	@Column(name = "entity_id")
	private String entityId;

	@EqualsAndHashCode.Include
	@Enumerated(EnumType.STRING)
	@Column(name = "timing_type")
	private TimingType timingType;

	@EqualsAndHashCode.Include
	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type")
	private EntityTypeEnum entityType;

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
