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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "institute_funding", uniqueConstraints = @UniqueConstraint(columnNames = { "institute_id",
		"funding_name_id" }, name = "UK_IFUNDING_INSTITUTE_ID_FUNDING_ID"), indexes = {
				@Index(name = "IDX_FUNDING_INSTITUTE_ID", columnList = "institute_id", unique = false) })
@Data
@NoArgsConstructor
@ToString(exclude = "institute")
@EqualsAndHashCode
public class InstituteFunding implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "GUID", strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institute_id", nullable = false)
	private Institute institute;

	@Column(name = "funding_name_id", nullable = false)
	private String fundingNameId;
	
	public void setAuditFields(String userId, InstituteFunding existingInstituteFunding) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (existingInstituteFunding == null) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
