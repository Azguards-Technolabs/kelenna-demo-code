package com.azguards.app.dto;

import java.io.Serializable;

public class UserCourseView implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7602075825983258282L;

	private String courseId;
	private int count;

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(final String courseId) {
		this.courseId = courseId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + (courseId == null ? 0 : courseId.hashCode());
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
		UserCourseView other = (UserCourseView) obj;
		if (count != other.count) {
			return false;
		}
		if (courseId == null) {
			if (other.courseId != null) {
				return false;
			}
		} else if (!courseId.equals(other.courseId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserCourseView [courseId=").append(courseId).append(", count=").append(count).append("]");
		return builder.toString();
	}

}
