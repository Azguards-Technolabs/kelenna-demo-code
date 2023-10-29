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
import javax.persistence.OneToOne;
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
@Table(name = "institute_additional_info", uniqueConstraints = @UniqueConstraint(columnNames = {
		"institute_id" }, name = "UK_INSTITUTE_ADDITIONAL_INFO_II"), indexes = {
				@Index(name = "IDX_INSTITUTE_ID", columnList = "institute_id", unique = true) })
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "institute")
public class InstituteAdditionalInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name ="student_number")
	private Integer numberOfStudent=0;
	
	@Column(name ="employee_number", nullable = false)
	private Integer numberOfEmployee=0;
	
	@Column(name ="teacher_number", nullable = false)
	private Integer numberOfTeacher=0;
	
	@Column(name ="class_number", nullable = false)
	private Integer numberOfClassRoom=0;
	
	@Column(name ="campus_size", nullable = false)
	private Integer sizeOfCampus=0;
	
	@Column(name ="lecture_hall_number", nullable = false)
	private Integer numberOfLectureHall=0;
	
	@Column(name ="faculty_number", nullable = false)
	private Integer numberOfFaculty=0;
	
	@Column(name ="employment_rate", nullable = false)
	private Integer rateOfEmployment=0;
	
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

	@ToString.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "institute_id")
	private Institute institute;

}
