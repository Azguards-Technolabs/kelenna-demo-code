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

import com.azguards.app.constant.PageRequestStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institute_page_request",  uniqueConstraints = @UniqueConstraint(columnNames = { "institute_id", "user_id" } , name = "UK_PR_INSTITUTE_ID_USER_ID"),
          indexes = { @Index (name = "IDX_PR_INSTITUTE_ID_USER_ID", columnList="institute_id,user_id", unique = true),
        		  @Index (name = "IDX_PR_INSTITUTE_ID", columnList="institute_id", unique = false)})
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InstitutePageRequest {
	
	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "institute_id", nullable = false, length = 36)
	private String instituteId;
	
	@Column(name = "user_id", nullable = false, length = 36)
	private String userId;
	
	@Column(name ="first_name", nullable = false)
	private String firstName;
	
	@Column(name ="last_name", nullable = false)
	private String lastName;
	
	@Column(name ="title", nullable = false)
	private String title;
	
	@Column(name ="email", nullable = false)
	private String email;
	
	@Column(name ="phone_number", nullable = false)
	private String phoneNumber;
	
	@Column(name ="management_name", nullable = false)
	private String managementName;
	
	@Column(name ="management_email", nullable = false)
	private String managementEmail;
	
	@Column(name ="management_phone_number", nullable = false)
	private String managementPhoneNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private PageRequestStatus pageRequestStatus;
	
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

	public InstitutePageRequest(String instituteId, String userId, String firstName, String lastName, String title,
			String email, String phoneNumber, String managementName, String managementEmail,
			String managementPhoneNumber, PageRequestStatus pageRequestStatus, Date createdOn, Date updatedOn,
			String createdBy, String updatedBy) {
		super();
		this.instituteId = instituteId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.managementName = managementName;
		this.managementEmail = managementEmail;
		this.managementPhoneNumber = managementPhoneNumber;
		this.pageRequestStatus = pageRequestStatus;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
}
