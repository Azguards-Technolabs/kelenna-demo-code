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
@ToString(exclude = "careers")
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "related_career", uniqueConstraints = @UniqueConstraint(columnNames = { "related_career", "career_id" }, 
	 name = "UK_RC_CAREER_ID"), indexes = {@Index(name = "IDX_CAREER_ID", columnList = "career_id", unique = false) })
public class RelatedCareer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	
	@Column(name = "related_career", nullable = false, columnDefinition = "text")
	private String relatedCareer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "career_id")
	private Careers careers;
	
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
	
	public RelatedCareer(String relatedCareer, Careers careers, String createdBy, Date createdOn) {
		this.relatedCareer = relatedCareer;
		this.careers = careers;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	
}
