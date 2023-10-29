package com.azguards.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "career_list", uniqueConstraints = @UniqueConstraint(columnNames = { "career" }, name = "UK_CAREER_NAME"))
public class Careers implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@EqualsAndHashCode.Include
	@Column(name = "career", nullable = false)
	private String career;
	
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
	
	@OneToMany(mappedBy = "careers" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RelatedCareer> relatedCareers = new ArrayList<>();
	
	@OneToMany(mappedBy = "careers" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerJob> careerJobs = new ArrayList<>();
}
