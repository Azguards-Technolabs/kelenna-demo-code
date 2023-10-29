package com.azguards.app.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "formatted_address", "geometry", "name", "rating", "user_ratings_total" })
public class Candidate implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4384766884385400137L;
	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("geometry")
	private Geometry geometry;
	@JsonProperty("name")
	private String name;
	@JsonProperty("rating")
	private Double rating;
	@JsonProperty("user_ratings_total")
	private Integer userRatingsTotal;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("formatted_address")
	public String getFormattedAddress() {
		return formattedAddress;
	}

	@JsonProperty("formatted_address")
	public void setFormattedAddress(final String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	@JsonProperty("geometry")
	public Geometry getGeometry() {
		return geometry;
	}

	@JsonProperty("geometry")
	public void setGeometry(final Geometry geometry) {
		this.geometry = geometry;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(final String name) {
		this.name = name;
	}

	@JsonProperty("rating")
	public Double getRating() {
		return rating;
	}

	@JsonProperty("rating")
	public void setRating(final Double rating) {
		this.rating = rating;
	}

	@JsonProperty("user_ratings_total")
	public Integer getUserRatingsTotal() {
		return userRatingsTotal;
	}

	@JsonProperty("user_ratings_total")
	public void setUserRatingsTotal(final Integer userRatingsTotal) {
		this.userRatingsTotal = userRatingsTotal;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (additionalProperties == null ? 0 : additionalProperties.hashCode());
		result = prime * result + (formattedAddress == null ? 0 : formattedAddress.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (rating == null ? 0 : rating.hashCode());
		result = prime * result + (userRatingsTotal == null ? 0 : userRatingsTotal.hashCode());
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
		Candidate other = (Candidate) obj;
		if (additionalProperties == null) {
			if (other.additionalProperties != null) {
				return false;
			}
		} else if (!additionalProperties.equals(other.additionalProperties)) {
			return false;
		}
		if (formattedAddress == null) {
			if (other.formattedAddress != null) {
				return false;
			}
		} else if (!formattedAddress.equals(other.formattedAddress)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (rating == null) {
			if (other.rating != null) {
				return false;
			}
		} else if (!rating.equals(other.rating)) {
			return false;
		}
		if (userRatingsTotal == null) {
			if (other.userRatingsTotal != null) {
				return false;
			}
		} else if (!userRatingsTotal.equals(other.userRatingsTotal)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Candidate [formattedAddress=").append(formattedAddress).append(", name=").append(name).append(", rating=").append(rating)
				.append(", userRatingsTotal=").append(userRatingsTotal).append(", additionalProperties=").append(additionalProperties).append("]");
		return builder.toString();
	}

}