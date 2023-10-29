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
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "help", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "category_id", "sub_category_id" }, 
name = "UK_HELP_HID_CID_SCID_TITLE"), indexes = {@Index(name = "IDX_USER_ID", columnList = "user_id", unique = false),
		   @Index(name = "IDX_CATEGORY_ID", columnList = "category_id", unique = false),
		   @Index(name = "IDX_SUB_CATEGORY_ID", columnList = "sub_category_id", unique = false)})
public class Help implements Serializable {

	private static final long serialVersionUID = 6922844940897956622L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private HelpCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_category_id", nullable = false)
	private HelpSubCategory subCategory;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description")
	private String description;

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

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_questioning")
	private Boolean isQuestioning;

	@Column(name = "user_id", nullable = false, length = 36)
	private String userId;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "assigned_user_id", nullable = false, length = 36)
	private String assignedUserId;

	@Column(name = "is_favourite")
	private Boolean isFavourite = false;

	@Column(name = "is_archive")
	private Boolean isArchive = false;
}
