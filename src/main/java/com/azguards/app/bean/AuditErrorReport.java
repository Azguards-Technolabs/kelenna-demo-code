package com.azguards.app.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "audit_error_report")
public class AuditErrorReport implements java.io.Serializable {

	/**
	*
	*/
	private static final long serialVersionUID = -4896547771928499529L;

	private String id;
	private String note;
	private Date createdOn;
	private String createdBy;
	private ErrorReport errorReport;

	/**
	 * @return the id
	 */
	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "created_by", length = 50)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "note", length = 50)
	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "error_report_id", nullable = false)
	public ErrorReport getErrorReport() {
		return errorReport;
	}

	public void setErrorReport(final ErrorReport errorReport) {
		this.errorReport = errorReport;
	}

}
