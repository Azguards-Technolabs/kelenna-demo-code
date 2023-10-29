package com.azguards.app.bean;

import java.io.Serializable;

// Generated 7 Jun, 2019 2:45:49 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "level", uniqueConstraints = @UniqueConstraint(columnNames = { "name",
		"code", "sequence_no" }, name = "UK_NA_CO_SN"), indexes = { @Index(name = "IDX_NAME", columnList = "name", unique = false) })
public class Level implements Serializable {

	private static final long serialVersionUID = 9149617652748065109L;

	@Id
	@GenericGenerator(name = "CustomUUIDGenerator", strategy = "com.azguards.app.util.CustomUUIDGenerator", parameters = {})
	@GeneratedValue(generator = "CustomUUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "sequence_no", nullable = false)
	private Integer sequenceNo;

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

	public Level(String id, String name, String code, String description, Integer sequenceNo, Boolean isActive,
			Date createdOn, Date updatedOn, Date deletedOn, String createdBy, String updatedBy, Boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.sequenceNo = sequenceNo;
		this.isActive = isActive;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.deletedOn = deletedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.isDeleted = isDeleted;
	}
}
