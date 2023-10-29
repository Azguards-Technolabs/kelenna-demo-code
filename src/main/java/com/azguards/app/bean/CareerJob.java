package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@ToString(exclude = "careers")
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "jobs", uniqueConstraints = @UniqueConstraint(columnNames = { "job", "career_id" }, 
	 name = "UK_JOB_CAREER_ID"), indexes = {@Index(name = "IDX_CAREER_ID", columnList = "career_id", unique = false)})
public class CareerJob implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "job", nullable = false)
	private String job;
	
	@Column(name = "job_description", nullable = false, columnDefinition = "text")
	private String jobDescription;

	@Column(name = "responsibility", columnDefinition = "text")
	private String responsibility;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "career_id")
	private Careers careers;
	
	@Column(name = "course_level", nullable = false)
	private Integer courseLevel;
	
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
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobCourseSearchKeyword> careerJobCourseSearchKeywords = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobLevel> careerJobLevels = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobSkill> careerJobSkills = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobSubject> careerJobSubjects = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobType> careerJobTypes = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobWorkingActivity> careerJobWorkingActivities = new ArrayList<>();
	
	@OneToMany(mappedBy = "careerJobs" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJobWorkingStyle> careerJobWorkingStyles = new ArrayList<>();
	
	public CareerJob(String jobs, String jobDescription, String responsibility, Careers careers, Integer courseLevel, Date createdOn, String createdBy) {
		this.job = jobs;
		this.jobDescription = jobDescription;
		this.responsibility = responsibility;
		this.careers = careers;
		this.courseLevel = courseLevel;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}
}
