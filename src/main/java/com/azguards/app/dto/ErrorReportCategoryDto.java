package com.azguards.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ErrorReportCategoryDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6986644293873778488L;

	@NotNull(message = "{name.is_required}")
	private String name;
	@NotNull(message = "{errorCategoryType.is_required}")
	private String errorCategoryType;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getErrorCategoryType() {
		return errorCategoryType;
	}

	public void setErrorCategoryType(final String errorCategoryType) {
		this.errorCategoryType = errorCategoryType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (errorCategoryType == null ? 0 : errorCategoryType.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
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
		ErrorReportCategoryDto other = (ErrorReportCategoryDto) obj;
		if (errorCategoryType == null) {
			if (other.errorCategoryType != null) {
				return false;
			}
		} else if (!errorCategoryType.equals(other.errorCategoryType)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorReportCategoryDto [name=").append(name).append(", errorCategoryType=").append(errorCategoryType).append("]");
		return builder.toString();
	}

}
