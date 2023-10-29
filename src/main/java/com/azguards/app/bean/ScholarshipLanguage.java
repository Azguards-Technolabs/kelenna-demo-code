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
@Table(name = "scholarship_language", uniqueConstraints = @UniqueConstraint(columnNames = { "scholarship_id",
		"name" }, name = "UK_SCHOLARSHIP_LANGUAGE_SI_NAME"), indexes = {
				@Index(name = "IDX_SCHOLARSHIP_ID", columnList = "scholarship_id", unique = false) })
public class ScholarshipLanguage implements Serializable {

	private static final long serialVersionUID = -3516597245947004154L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scholarship_id")
	private Scholarship scholarship;

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

	public void setAuditFields(String userId, ScholarshipLanguage existingScholarshipLanguage) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (existingScholarshipLanguage != null) {
			this.setCreatedBy(existingScholarshipLanguage.getCreatedBy());
			this.setCreatedOn(existingScholarshipLanguage.getCreatedOn());
		}else {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
