package com.azguards.app.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "accredited_detail",  uniqueConstraints = @UniqueConstraint(columnNames = { "entity_id" , "accrediated_name", "entity_type" } , name = "UK_Accredited_Detail_NAME_EI_ET"),
indexes = { @Index (name = "IDX_ENTITY_ID", columnList="entity_id", unique = false)})
public class AccrediatedDetail {

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;

	@Column(name = "accrediated_name")
	private String accrediatedName;

	@Column(name = "accrediated_website")
	private String accrediatedWebsite;

	@Column(name = "entity_id", length=36)
	private String entityId;

	@Column(name = "entity_type")
	private String entityType;
	
	@Column(name = "description")
	private String description;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private String updateddBy;

	@Column(name = "updated_on")
	private Date updatedOn;
}
