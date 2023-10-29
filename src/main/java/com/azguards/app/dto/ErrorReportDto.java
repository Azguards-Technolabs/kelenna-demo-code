package com.azguards.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ErrorReportDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8602286882307240534L;
	@NotNull(message = "{userId.is_required}")
	private String userId;
	@NotNull(message = "{errorReportCategoryId.is_required}")
	private String errorReportCategoryId;
	@NotNull(message = "{description.is_required}")
	private String description;

	private String caseNumber;
	private String status;
	private String courseArticleId;
	private String dueDate;
	private String assigneeUserId;
	private String severity;

	private String phoneName;
	private String phoneLocation;
	private String phoneIp;

	private String note;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * @return the errorReportCategoryId
	 */
	public String getErrorReportCategoryId() {
		return errorReportCategoryId;
	}

	/**
	 * @param errorReportCategoryId the errorReportCategoryId to set
	 */
	public void setErrorReportCategoryId(final String errorReportCategoryId) {
		this.errorReportCategoryId = errorReportCategoryId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the cashNumber
	 */
	public String getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @param cashNumber
	 */
	public void setCaseNumber(final String caseNumber) {
		this.caseNumber = caseNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the coreArticalDetail
	 */
	public String getCourseArticleId() {
		return courseArticleId;
	}

	/**
	 * @param coreArticalDetail
	 */
	public void setCourseArticleId(final String courseArticleId) {
		this.courseArticleId = courseArticleId;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 */
	public void setDueDate(final String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the assigneeUserId
	 */
	public String getAssigneeUserId() {
		return assigneeUserId;
	}

	/**
	 * @param assigneeUserId
	 */
	public void setAssigneeUserId(final String assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(final String severity) {
		this.severity = severity;
	}

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(final String phoneName) {
		this.phoneName = phoneName;
	}

	public String getPhoneLocation() {
		return phoneLocation;
	}

	public void setPhoneLocation(final String phoneLocation) {
		this.phoneLocation = phoneLocation;
	}

	public String getPhoneIp() {
		return phoneIp;
	}

	public void setPhoneIp(final String phoneIp) {
		this.phoneIp = phoneIp;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (assigneeUserId == null ? 0 : assigneeUserId.hashCode());
		result = prime * result + (caseNumber == null ? 0 : caseNumber.hashCode());
		result = prime * result + (courseArticleId == null ? 0 : courseArticleId.hashCode());
		result = prime * result + (description == null ? 0 : description.hashCode());
		result = prime * result + (dueDate == null ? 0 : dueDate.hashCode());
		result = prime * result + (errorReportCategoryId == null ? 0 : errorReportCategoryId.hashCode());
		result = prime * result + (note == null ? 0 : note.hashCode());
		result = prime * result + (phoneIp == null ? 0 : phoneIp.hashCode());
		result = prime * result + (phoneLocation == null ? 0 : phoneLocation.hashCode());
		result = prime * result + (phoneName == null ? 0 : phoneName.hashCode());
		result = prime * result + (severity == null ? 0 : severity.hashCode());
		result = prime * result + (status == null ? 0 : status.hashCode());
		result = prime * result + (userId == null ? 0 : userId.hashCode());
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
		ErrorReportDto other = (ErrorReportDto) obj;
		if (assigneeUserId == null) {
			if (other.assigneeUserId != null) {
				return false;
			}
		} else if (!assigneeUserId.equals(other.assigneeUserId)) {
			return false;
		}
		if (caseNumber == null) {
			if (other.caseNumber != null) {
				return false;
			}
		} else if (!caseNumber.equals(other.caseNumber)) {
			return false;
		}
		if (courseArticleId == null) {
			if (other.courseArticleId != null) {
				return false;
			}
		} else if (!courseArticleId.equals(other.courseArticleId)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (dueDate == null) {
			if (other.dueDate != null) {
				return false;
			}
		} else if (!dueDate.equals(other.dueDate)) {
			return false;
		}
		if (errorReportCategoryId == null) {
			if (other.errorReportCategoryId != null) {
				return false;
			}
		} else if (!errorReportCategoryId.equals(other.errorReportCategoryId)) {
			return false;
		}
		if (note == null) {
			if (other.note != null) {
				return false;
			}
		} else if (!note.equals(other.note)) {
			return false;
		}
		if (phoneIp == null) {
			if (other.phoneIp != null) {
				return false;
			}
		} else if (!phoneIp.equals(other.phoneIp)) {
			return false;
		}
		if (phoneLocation == null) {
			if (other.phoneLocation != null) {
				return false;
			}
		} else if (!phoneLocation.equals(other.phoneLocation)) {
			return false;
		}
		if (phoneName == null) {
			if (other.phoneName != null) {
				return false;
			}
		} else if (!phoneName.equals(other.phoneName)) {
			return false;
		}
		if (severity == null) {
			if (other.severity != null) {
				return false;
			}
		} else if (!severity.equals(other.severity)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorReportDto [userId=").append(userId).append(", errorReportCategoryId=").append(errorReportCategoryId).append(", description=")
				.append(description).append(", caseNumber=").append(caseNumber).append(", status=").append(status).append(", courseArticleId=")
				.append(courseArticleId).append(", dueDate=").append(dueDate).append(", assigneeUserId=").append(assigneeUserId).append(", severity=")
				.append(severity).append(", phoneName=").append(phoneName).append(", phoneLocation=").append(phoneLocation).append(", phoneIp=").append(phoneIp)
				.append(", note=").append(note).append("]");
		return builder.toString();
	}

}
