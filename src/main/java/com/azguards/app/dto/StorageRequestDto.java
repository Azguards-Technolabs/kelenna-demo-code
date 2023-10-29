package com.azguards.app.dto;

import java.io.Serializable;
import java.util.List;

public class StorageRequestDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2247205976858854489L;

	private List<String> entityIds;
	private String entityType;
	private String type;

	public List<String> getEntityIds() {
		return entityIds;
	}

	public void setEntityIds(final List<String> entityIds) {
		this.entityIds = entityIds;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(final String entityType) {
		this.entityType = entityType;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (entityIds == null ? 0 : entityIds.hashCode());
		result = prime * result + (entityType == null ? 0 : entityType.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
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
		StorageRequestDto other = (StorageRequestDto) obj;
		if (entityIds == null) {
			if (other.entityIds != null) {
				return false;
			}
		} else if (!entityIds.equals(other.entityIds)) {
			return false;
		}
		if (entityType == null) {
			if (other.entityType != null) {
				return false;
			}
		} else if (!entityType.equals(other.entityType)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StorageRequestDto [entityIds=").append(entityIds).append(", entityType=").append(entityType).append(", type=").append(type).append("]");
		return builder.toString();
	}

}
