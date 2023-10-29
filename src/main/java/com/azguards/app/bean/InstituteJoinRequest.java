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

import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.constant.InstituteJoinStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institute_join_request",  uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "institute_name" } , name = "UK_IJOIN_REQUEST_INSTITUTE_NAME_USER_ID"),
          indexes = { @Index (name = "IDX_JR_INSTITUTE_NAME_USER_ID", columnList="institute_name,user_id", unique = true),
        		  @Index (name = "IDX_JR_STATUS", columnList="status", unique = false)})
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InstituteJoinRequest {

	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "user_id", nullable = false, length=36)
	private String userId;
	
	@Column(name = "institute_name", nullable = false)
	private String instituteName;
	
	@Column(name = "institute_country", nullable = false)
	private String instituteCountry;
	
	@Column(name = "institute_type", nullable = false)
	private String typeOfInstitute;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name ="first_name", nullable = false)
	private String firstName;
	
	@Column(name ="last_name", nullable = false)
	private String lastName;
	
	@Column(name ="title", nullable = false)
	private String title;
	
	@Column(name ="work_email", nullable = false)
	private String workEmail;
	
	@Column(name ="work_phone_number", nullable = false)
	private String workPhoneNumber;
	
	@Column(name ="management_name", nullable = false)
	private String managementName;
	
	@Column(name ="management_email", nullable = false)
	private String managementEmail;
	
	@Column(name ="management_phone_number", nullable = false)
	private String managementPhoneNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private InstituteJoinStatus instituteJoinStatus;
	
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

	public InstituteJoinRequest(String userId, String instituteName, String instituteCountry, String typeOfInstitute,
			String type, String firstName, String lastName, String title, String workEmail, String workPhoneNumber,
			String managementName, String managementEmail, String managementPhoneNumber,
			InstituteJoinStatus instituteJoinStatus, Date createdOn, Date updatedOn, String createdBy,
			String updatedBy) {
		super();
		this.userId = userId;
		this.instituteName = instituteName;
		this.instituteCountry = instituteCountry;
		this.typeOfInstitute = typeOfInstitute;
		this.type = type;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.workEmail = workEmail;
		this.workPhoneNumber = workPhoneNumber;
		this.managementName = managementName;
		this.managementEmail = managementEmail;
		this.managementPhoneNumber = managementPhoneNumber;
		this.instituteJoinStatus = instituteJoinStatus;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
}
