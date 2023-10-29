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

@Data
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "course_prerequisite_subjects", uniqueConstraints = @UniqueConstraint(columnNames = { "course_prerequisite_id", "subject_name" }, 
	   	 name = "UK_COURSE_PREREQUISITE_SUBJECT_NA_CN_CN"), indexes = {@Index(name = "IDX_COURSE_PREREQUISITE_ID", columnList = "course_prerequisite_id", unique = false)})
public class CoursePrerequisiteSubjects implements Serializable {

	private static final long serialVersionUID = 8492390790670110780L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_prerequisite_id", nullable = false)
	private CoursePrerequisite coursePrerequisite;
	
	@Column(name = "subject_name", nullable = false)
	private String subjectName;
	
	@Column(name = "prerequisite_grade", nullable = false)
	private Double prerequisiteGrade;
	
	@Column(name = "subject_type")
	private String type;
	
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
	
	public CoursePrerequisiteSubjects(CoursePrerequisite coursePrerequisite, String subjectName, Double prerequisiteGrade, String type, Date createdOn,
			String createdBy) {
		this.coursePrerequisite = coursePrerequisite;
		this.subjectName = subjectName;
		this.prerequisiteGrade = prerequisiteGrade;
		this.type = type;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}
}
