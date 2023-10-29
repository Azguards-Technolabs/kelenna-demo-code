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

import com.azguards.app.constant.RecommendRequestStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institute_recommend_request",  uniqueConstraints = @UniqueConstraint(columnNames = { "institute_name", "user_id","address_institute" } , name = "UK_RR_INSTITUTE_NAME_USER_ID_ADDRESS"),
          indexes = { @Index (name = "IDX_RR_INSTITUTE_NAME", columnList="institute_name", unique = false),
        		  @Index (name = "IDX_RR_STATUS", columnList="status", unique = false)})
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InstituteRecommendRequest {
	
	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "user_id", nullable = false, length = 36)
	private String userId;
	
	@Column(name = "institute_name", nullable = false)
	private String instituteName;
	
	@Column(name = "is_working", nullable = false)
	private Boolean isWorking;
	
	@Column(name = "knowing_someone_working", nullable = false)
	private Boolean knowingSomeoneWorking;
	
	@Column(name ="name", nullable = false)
	private String name;
	
	@Column(name ="title", nullable = false)
	private String title;
	
	@Column(name ="email", nullable = false)
	private String email;
	
	@Column(name ="phone_number", nullable = false)
	private String phoneNumber;
	
	@Column(name ="address_institute", nullable = false)
	private String addressInstitute;
	
	@Column(name ="website_link", nullable = false)
	private String websiteLink;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private RecommendRequestStatus recommendRequestStatus;
		
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

	public InstituteRecommendRequest(String userId, String instituteName, Boolean isWorking,
			Boolean knowingSomeoneWorking, String name, String title, String email, String phoneNumber,
			String addressInstitute, String websiteLink, RecommendRequestStatus recommendRequestStatus, Date createdOn,
			Date updatedOn, String createdBy, String updatedBy) {
		super();
		this.userId = userId;
		this.instituteName = instituteName;
		this.isWorking = isWorking;
		this.knowingSomeoneWorking = knowingSomeoneWorking;
		this.name = name;
		this.title = title;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.addressInstitute = addressInstitute;
		this.websiteLink = websiteLink;
		this.recommendRequestStatus = recommendRequestStatus;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
}
