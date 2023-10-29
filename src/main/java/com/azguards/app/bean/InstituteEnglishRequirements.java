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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institute_english_requirements",  uniqueConstraints = @UniqueConstraint(columnNames = { "institute_id", "exam_name"} , name = "UK_IER_INSTITUTE_ID_EXAN_NAME"),
		indexes = { @Index (name = "IDX_INSTITUTE_ENGLISH_REQUIREMENTS_INSTITUTE_ID", columnList="institute_id", unique = false)})
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude="institute")
@EqualsAndHashCode
public class InstituteEnglishRequirements implements Serializable {

	private static final long serialVersionUID = -6035310092856978113L;

	@Id
	@GenericGenerator(name = "GUID" , strategy = "org.hibernate.id.GUIDGenerator")
	@GeneratedValue(generator = "GUID")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "institute_id", nullable = false, length = 36)
	private String instituteId;
	
	@Column(name = "exam_name")
	private String examName;
	
	@Column(name = "reading_marks")
	private Double readingMarks;
	
	@Column(name = "listning_marks")
	private Double listningMarks;
	
	@Column(name = "writing_marks")
	private Double writingMarks;
	
	@Column(name = "oral_marks")
	private Double oralMarks;
	
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
	@JoinColumn( name = "institute_id", insertable = false, updatable = false )
	private Institute institute;

	public InstituteEnglishRequirements(String instituteId, String examName, Double readingMarks, Double listningMarks,
			Double writingMarks, Double oralMarks, Date createdOn, Date updatedOn, String createdBy, String updatedBy) {
		super();
		this.instituteId = instituteId;
		this.examName = examName;
		this.readingMarks = readingMarks;
		this.listningMarks = listningMarks;
		this.writingMarks = writingMarks;
		this.oralMarks = oralMarks;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
}
