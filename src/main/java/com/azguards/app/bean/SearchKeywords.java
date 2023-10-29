package com.azguards.app.bean;

// Generated 7 Jun, 2019 2:45:49 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * SearchKeywords generated by hbm2java
 */
@Entity
@Table(name = "search_keywords")
public class SearchKeywords implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4770022963730803925L;
	private String id;
	private String keyword;
	private String KDesc;

	public SearchKeywords() {
	}

	public SearchKeywords(String keyword, String KDesc) {
		this.keyword = keyword;
		this.KDesc = KDesc;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "keyword", length = 250)
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "k_desc", length = 250)
	public String getKDesc() {
		return this.KDesc;
	}

	public void setKDesc(String KDesc) {
		this.KDesc = KDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((KDesc == null) ? 0 : KDesc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
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
		SearchKeywords other = (SearchKeywords) obj;
		if (KDesc == null) {
			if (other.KDesc != null)
				return false;
		} else if (!KDesc.equals(other.KDesc))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchKeywords [id=").append(id).append(", keyword=").append(keyword).append(", KDesc=")
				.append(KDesc).append("]");
		return builder.toString();
	}

}
