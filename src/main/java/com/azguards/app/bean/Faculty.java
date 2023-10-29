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
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculty", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }, name = "UK_FACULY_NA"), 
	indexes = {@Index(name = "IDX_FACULTY_NAME", columnList = "name", unique = true) })
public class Faculty implements Serializable {

	private static final long serialVersionUID = -5502957778916515394L;

	@Id
	@GenericGenerator(name = "CustomUUIDGenerator", strategy = "com.azguards.app.util.CustomUUIDGenerator", parameters = {})
	@GeneratedValue(generator = "CustomUUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", length = 500)
	private String description;

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
}
