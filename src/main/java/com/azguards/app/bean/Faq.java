package com.azguards.app.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.constant.FaqEntityType;

import lombok.Data;

@Entity
@Table(name = "faq", uniqueConstraints = @UniqueConstraint(columnNames = { "title", "entity_id", "entity_type",
		"faq_sub_category_id" }, name = "UK_FAQ_T_EI_ET_SC"), indexes = {
				@Index(name = "IDX_FAQ_ENTITY_ID", columnList = "entity_id", unique = false),
				@Index(name = "IDX_FAQ_SUB_CATEGORY_ID", columnList = "faq_sub_category_id", unique = false) })
@Data
public class Faq implements Serializable {

	private static final long serialVersionUID = 5744814923342867841L;

	@Id
	@GenericGenerator(name = "GUID", strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "entity_id", nullable = false, length = 36)
	private String entityId;

	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false)
	private FaqEntityType entityType;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faq_sub_category_id")
	private FaqSubCategory faqSubCategory;
}