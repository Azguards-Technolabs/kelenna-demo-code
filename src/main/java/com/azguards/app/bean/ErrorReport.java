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
@Table(name = "error_report")
public class ErrorReport implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4896547771928499529L;

	private String id;
	private String userId;
	private ErrorReportCategory errorReportCategory;
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
	private String severity;
	private Boolean isFavourite = false;
	
	private String phoneName;
	private String phoneLocation;
	private String phoneIp;
	private Boolean isArchive = false;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(final Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_on", length = 19)
	public Date getDeletedOn() {
		return this.deletedOn;
	}

	public void setDeletedOn(final Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	@Column(name = "created_by", length = 50)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updated_by", length = 50)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the isActive
	 */
	@Column(name = "is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(final Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the userId
	 */
	@Column(name = "user_id")
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
	 * @return the errorReportCategory
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	public ErrorReportCategory getErrorReportCategory() {
		return errorReportCategory;
	}

	/**
	 * @param errorReportCategory the errorReportCategory to set
	 */
	public void setErrorReportCategory(final ErrorReportCategory errorReportCategory) {
		this.errorReportCategory = errorReportCategory;
	}

	/**
	 * @return the description
	 */
	@Column(name = "description")
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
	@Column(name = "case_number")
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
	@Column(name = "status")
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
	@Column(name = "course_article_id",length=36)
	public String getCourseArticleId() {
		return courseArticleId;
	}

	/**
	 * @param coreArticalDetail
	 */
	public void setCourseArticleId(final String courseArticleId) {
		this.courseArticleId = courseArticleId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_date", length = 19)
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 */
	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the assigneeUserId
	 */
	@Column(name = "assignee_user_id", length=36)
	public String getAssigneeUserId() {
		return assigneeUserId;
	}

	/**
	 * @param dueDate
	 */
	public void setAssigneeUserId(final String assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}

	/**
	 * @return the severity
	 */
	@Column(name = "severity")
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(final String severity) {
		this.severity = severity;
	}

	@Column(name = "is_favourite")
	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(final Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	@Column(name = "phone_name")
	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	
	@Column(name = "phone_location")
	public String getPhoneLocation() {
		return phoneLocation;
	}

	public void setPhoneLocation(String phoneLocation) {
		this.phoneLocation = phoneLocation;
	}

	@Column(name = "phone_ip")
	public String getPhoneIp() {
		return phoneIp;
	}

	public void setPhoneIp(String phoneIp) {
		this.phoneIp = phoneIp;
	}

	@Column(name = "is_archive")
	public Boolean getIsArchive() {
		return isArchive;
	}

	public void setIsArchive(Boolean isArchive) {
		this.isArchive = isArchive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assigneeUserId == null) ? 0 : assigneeUserId.hashCode());
		result = prime * result + ((caseNumber == null) ? 0 : caseNumber.hashCode());
		result = prime * result + ((courseArticleId == null) ? 0 : courseArticleId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((deletedOn == null) ? 0 : deletedOn.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((errorReportCategory == null) ? 0 : errorReportCategory.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((isArchive == null) ? 0 : isArchive.hashCode());
		result = prime * result + ((isFavourite == null) ? 0 : isFavourite.hashCode());
		result = prime * result + ((phoneIp == null) ? 0 : phoneIp.hashCode());
		result = prime * result + ((phoneLocation == null) ? 0 : phoneLocation.hashCode());
		result = prime * result + ((phoneName == null) ? 0 : phoneName.hashCode());
		result = prime * result + ((severity == null) ? 0 : severity.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((updatedOn == null) ? 0 : updatedOn.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorReport other = (ErrorReport) obj;
		if (assigneeUserId == null) {
			if (other.assigneeUserId != null)
				return false;
		} else if (!assigneeUserId.equals(other.assigneeUserId))
			return false;
		if (caseNumber == null) {
			if (other.caseNumber != null)
				return false;
		} else if (!caseNumber.equals(other.caseNumber))
			return false;
		if (courseArticleId == null) {
			if (other.courseArticleId != null)
				return false;
		} else if (!courseArticleId.equals(other.courseArticleId))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (deletedOn == null) {
			if (other.deletedOn != null)
				return false;
		} else if (!deletedOn.equals(other.deletedOn))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (errorReportCategory == null) {
			if (other.errorReportCategory != null)
				return false;
		} else if (!errorReportCategory.equals(other.errorReportCategory))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (isArchive == null) {
			if (other.isArchive != null)
				return false;
		} else if (!isArchive.equals(other.isArchive))
			return false;
		if (isFavourite == null) {
			if (other.isFavourite != null)
				return false;
		} else if (!isFavourite.equals(other.isFavourite))
			return false;
		if (phoneIp == null) {
			if (other.phoneIp != null)
				return false;
		} else if (!phoneIp.equals(other.phoneIp))
			return false;
		if (phoneLocation == null) {
			if (other.phoneLocation != null)
				return false;
		} else if (!phoneLocation.equals(other.phoneLocation))
			return false;
		if (phoneName == null) {
			if (other.phoneName != null)
				return false;
		} else if (!phoneName.equals(other.phoneName))
			return false;
		if (severity == null) {
			if (other.severity != null)
				return false;
		} else if (!severity.equals(other.severity))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (updatedOn == null) {
			if (other.updatedOn != null)
				return false;
		} else if (!updatedOn.equals(other.updatedOn))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorReport [id=").append(id).append(", userId=").append(userId)
				.append(", errorReportCategory=").append(errorReportCategory).append(", description=")
				.append(description).append(", createdOn=").append(createdOn).append(", updatedOn=").append(updatedOn)
				.append(", createdBy=").append(createdBy).append(", updatedBy=").append(updatedBy)
				.append(", deletedOn=").append(deletedOn).append(", isActive=").append(isActive).append(", caseNumber=")
				.append(caseNumber).append(", status=").append(status).append(", courseArticleId=")
				.append(courseArticleId).append(", dueDate=").append(dueDate).append(", assigneeUserId=")
				.append(assigneeUserId).append(", severity=").append(severity).append(", isFavourite=")
				.append(isFavourite).append(", phoneName=").append(phoneName).append(", phoneLocation=")
				.append(phoneLocation).append(", phoneIp=").append(phoneIp).append(", isArchive=").append(isArchive)
				.append("]");
		return builder.toString();
	}
	
	

}