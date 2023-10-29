package com.azguards.app.bean;

import java.io.Serializable;
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
@Table(name = "enrollment_status")
public class EnrollmentStatus implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1493487191397729023L;

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "enrollment_id", nullable = false)
	private Enrollment enrollment;
	@Column(name = "status", nullable = false)
	private String status;
	@Column(name = "dead_line", length = 19)
	private Date deadLine;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	private Date createdOn;
	@Column(name = "created_by", length = 50)
	private String createdBy;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(final Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(final Date deadLine) {
		this.deadLine = deadLine;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (createdBy == null ? 0 : createdBy.hashCode());
		result = prime * result + (createdOn == null ? 0 : createdOn.hashCode());
		result = prime * result + (deadLine == null ? 0 : deadLine.hashCode());
		result = prime * result + (enrollment == null ? 0 : enrollment.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (status == null ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EnrollmentStatus other = (EnrollmentStatus) obj;
		if (createdBy == null) {
			if (other.createdBy != null) {
				return false;
			}
		} else if (!createdBy.equals(other.createdBy)) {
			return false;
		}
		if (createdOn == null) {
			if (other.createdOn != null) {
				return false;
			}
		} else if (!createdOn.equals(other.createdOn)) {
			return false;
		}
		if (deadLine == null) {
			if (other.deadLine != null) {
				return false;
			}
		} else if (!deadLine.equals(other.deadLine)) {
			return false;
		}
		if (enrollment == null) {
			if (other.enrollment != null) {
				return false;
			}
		} else if (!enrollment.equals(other.enrollment)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EnrollmentStatus [id=").append(id).append(", enrollment=").append(enrollment).append(", status=").append(status).append(", deadLine=")
				.append(deadLine).append(", createdOn=").append(createdOn).append(", createdBy=").append(createdBy).append("]");
		return builder.toString();
	}

}
