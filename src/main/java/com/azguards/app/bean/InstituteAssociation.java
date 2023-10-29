package com.azguards.app.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.constant.InstituteAssociationStatus;
import com.azguards.app.constant.InstituteAssociationType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institute_association" ,uniqueConstraints = @UniqueConstraint(columnNames = { "source_institute_id","destination_institute_id","association_type" } , name = "UK_INSTITUTE_ASSOCIATION_SDA"))
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InstituteAssociation implements Serializable {

	private static final long serialVersionUID = 6066399694958334275L;

	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "source_institute_id", nullable = false, length=36)
	private String sourceInstituteId;
	
	@Column(name = "destination_institute_id", nullable = false, length = 36)
	private String destinationInstituteId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "association_type", nullable = false)
	private InstituteAssociationType instituteAssociationType;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private InstituteAssociationStatus instituteAssociationStatus;
	
	@Column(name = "rejection_counter", nullable = false)
	private Integer rejectionCounter;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;

	public InstituteAssociation(String sourceInstituteId, String destinationInstituteId,
			InstituteAssociationType instituteAssociationType, InstituteAssociationStatus instituteAssociationStatus,Integer rejectionCounter,
			Date createdOn, Date updatedOn, String createdBy, String updatedBy) {
		super();
		this.sourceInstituteId = sourceInstituteId;
		this.destinationInstituteId = destinationInstituteId;
		this.instituteAssociationType = instituteAssociationType;
		this.instituteAssociationStatus = instituteAssociationStatus;
		this.rejectionCounter = rejectionCounter;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
}
