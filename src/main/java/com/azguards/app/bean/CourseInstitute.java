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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = { "sourceCourse", "sourceInstitute", "destinationCourse", "destinationInstitute" })
@EqualsAndHashCode
@Table(name = "course_institute", uniqueConstraints = @UniqueConstraint(columnNames = { "source_course_id",
		"source_institute_id", "destination_course_id",
		"destination_institute_id" }, name = "UK_COURSE_SC_SI_DC_DI"), indexes = {
				@Index(name = "IDX_SRC_COURSE_ID", columnList = "source_course_id", unique = false),
				@Index(name = "IDX_DES_COURSE_ID", columnList = "source_course_id", unique = false) })
public class CourseInstitute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5639931588147850985L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_course_id", nullable = false)
	private Course sourceCourse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_institute_id", nullable = false)
	private Institute sourceInstitute;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_course_id", nullable = false)
	private Course destinationCourse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_institute_id", nullable = false)
	private Institute destinationInstitute;

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

	public void setAuditFields(String userId) {
		this.setUpdatedBy(userId);
		this.setUpdatedOn(new Date());
		if (StringUtils.isEmpty(id)) {
			this.setCreatedBy(userId);
			this.setCreatedOn(new Date());
		}
	}
}
