package com.azguards.app.dto;

import java.io.Serializable;
import java.util.Date;

public class ErrorReportResponseDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1390809834979298427L;
	private String id;
	private String userId;
	private String userName;
	private String userEmail;
	private String errorReportCategoryId;
	private String errorReportCategoryName;
	private String description;
	private Date createdOn;
	private Date updatedOn;
	private String createdBy;
	private String updatedBy;
	private Date deletedOn;
	private Boolean isActive;
	private String caseNumber;
	private String status;
	private String courseArticleId;
	private Date dueDate;
	private String assigneeUserId;
	private String assigneeUserName;
	private String assigneeUserImageUrl;
	private String severity;
	private Boolean isFavourite;
	private String phoneName;
	private String phoneLocation;
	private String phoneIp;
	private Boolean isArchive;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(final String userEmail) {
		this.userEmail = userEmail;
	}

	public String getErrorReportCategoryId() {
		return errorReportCategoryId;
	}

	public void setErrorReportCategoryId(final String errorReportCategoryId) {
		this.errorReportCategoryId = errorReportCategoryId;
	}

	public String getErrorReportCategoryName() {
		return errorReportCategoryName;
	}

	public void setErrorReportCategoryName(final String errorReportCategoryName) {
		this.errorReportCategoryName = errorReportCategoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(final Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(final Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(final String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getCourseArticleId() {
		return courseArticleId;
	}

	public void setCourseArticleId(final String courseArticleId) {
		this.courseArticleId = courseArticleId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getAssigneeUserId() {
		return assigneeUserId;
	}

	public void setAssigneeUserId(final String assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}

	public String getAssigneeUserName() {
		return assigneeUserName;
	}

	public void setAssigneeUserName(final String assigneeUserName) {
		this.assigneeUserName = assigneeUserName;
	}

	public String getAssigneeUserImageUrl() {
		return assigneeUserImageUrl;
	}

	public void setAssigneeUserImageUrl(final String assigneeUserImageUrl) {
		this.assigneeUserImageUrl = assigneeUserImageUrl;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(final String severity) {
		this.severity = severity;
	}

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(final Boolean isFavourite) {
		this.isFavourite = isFavourite;
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

	public Boolean getIsArchive() {
		return isArchive;
	}

	public void setIsArchive(final Boolean isArchive) {
		this.isArchive = isArchive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (assigneeUserId == null ? 0 : assigneeUserId.hashCode());
		result = prime * result + (assigneeUserImageUrl == null ? 0 : assigneeUserImageUrl.hashCode());
		result = prime * result + (assigneeUserName == null ? 0 : assigneeUserName.hashCode());
		result = prime * result + (caseNumber == null ? 0 : caseNumber.hashCode());
		result = prime * result + (courseArticleId == null ? 0 : courseArticleId.hashCode());
		result = prime * result + (createdBy == null ? 0 : createdBy.hashCode());
		result = prime * result + (createdOn == null ? 0 : createdOn.hashCode());
		result = prime * result + (deletedOn == null ? 0 : deletedOn.hashCode());
		result = prime * result + (description == null ? 0 : description.hashCode());
		result = prime * result + (dueDate == null ? 0 : dueDate.hashCode());
		result = prime * result + (errorReportCategoryId == null ? 0 : errorReportCategoryId.hashCode());
		result = prime * result + (errorReportCategoryName == null ? 0 : errorReportCategoryName.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (isActive == null ? 0 : isActive.hashCode());
		result = prime * result + (isArchive == null ? 0 : isArchive.hashCode());
		result = prime * result + (isFavourite == null ? 0 : isFavourite.hashCode());
		result = prime * result + (phoneIp == null ? 0 : phoneIp.hashCode());
		result = prime * result + (phoneLocation == null ? 0 : phoneLocation.hashCode());
		result = prime * result + (phoneName == null ? 0 : phoneName.hashCode());
		result = prime * result + (severity == null ? 0 : severity.hashCode());
		result = prime * result + (status == null ? 0 : status.hashCode());
		result = prime * result + (updatedBy == null ? 0 : updatedBy.hashCode());
		result = prime * result + (updatedOn == null ? 0 : updatedOn.hashCode());
		result = prime * result + (userEmail == null ? 0 : userEmail.hashCode());
		result = prime * result + (userId == null ? 0 : userId.hashCode());
		result = prime * result + (userName == null ? 0 : userName.hashCode());
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
		ErrorReportResponseDto other = (ErrorReportResponseDto) obj;
		if (assigneeUserId == null) {
			if (other.assigneeUserId != null) {
				return false;
			}
		} else if (!assigneeUserId.equals(other.assigneeUserId)) {
			return false;
		}
		if (assigneeUserImageUrl == null) {
			if (other.assigneeUserImageUrl != null) {
				return false;
			}
		} else if (!assigneeUserImageUrl.equals(other.assigneeUserImageUrl)) {
			return false;
		}
		if (assigneeUserName == null) {
			if (other.assigneeUserName != null) {
				return false;
			}
		} else if (!assigneeUserName.equals(other.assigneeUserName)) {
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
		if (deletedOn == null) {
			if (other.deletedOn != null) {
				return false;
			}
		} else if (!deletedOn.equals(other.deletedOn)) {
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
		if (errorReportCategoryName == null) {
			if (other.errorReportCategoryName != null) {
				return false;
			}
		} else if (!errorReportCategoryName.equals(other.errorReportCategoryName)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isActive == null) {
			if (other.isActive != null) {
				return false;
			}
		} else if (!isActive.equals(other.isActive)) {
			return false;
		}
		if (isArchive == null) {
			if (other.isArchive != null) {
				return false;
			}
		} else if (!isArchive.equals(other.isArchive)) {
			return false;
		}
		if (isFavourite == null) {
			if (other.isFavourite != null) {
				return false;
			}
		} else if (!isFavourite.equals(other.isFavourite)) {
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
		if (updatedBy == null) {
			if (other.updatedBy != null) {
				return false;
			}
		} else if (!updatedBy.equals(other.updatedBy)) {
			return false;
		}
		if (updatedOn == null) {
			if (other.updatedOn != null) {
				return false;
			}
		} else if (!updatedOn.equals(other.updatedOn)) {
			return false;
		}
		if (userEmail == null) {
			if (other.userEmail != null) {
				return false;
			}
		} else if (!userEmail.equals(other.userEmail)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ErrorReportResponseDto [id=" + id + ", userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", errorReportCategoryId="
				+ errorReportCategoryId + ", errorReportCategoryName=" + errorReportCategoryName + ", description=" + description + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", deletedOn=" + deletedOn + ", isActive=" + isActive
				+ ", caseNumber=" + caseNumber + ", status=" + status + ", courseArticleId=" + courseArticleId + ", dueDate=" + dueDate + ", assigneeUserId="
				+ assigneeUserId + ", assigneeUserName=" + assigneeUserName + ", assigneeUserImageUrl=" + assigneeUserImageUrl + ", severity=" + severity
				+ ", isFavourite=" + isFavourite + ", phoneName=" + phoneName + ", phoneLocation=" + phoneLocation + ", phoneIp=" + phoneIp + ", isArchive="
				+ isArchive + "]";
	}

}
