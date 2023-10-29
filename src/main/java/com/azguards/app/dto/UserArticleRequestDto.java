package com.azguards.app.dto;

import java.io.Serializable;

public class UserArticleRequestDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5780245895720098894L;
	private String userId;
	private String articleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(final String articleId) {
		this.articleId = articleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (articleId == null ? 0 : articleId.hashCode());
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
		UserArticleRequestDto other = (UserArticleRequestDto) obj;
		if (articleId == null) {
			if (other.articleId != null) {
				return false;
			}
		} else if (!articleId.equals(other.articleId)) {
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
		builder.append("UserArticleRequestDto [userId=").append(userId).append(", articleId=").append(articleId).append("]");
		return builder.toString();
	}

}
