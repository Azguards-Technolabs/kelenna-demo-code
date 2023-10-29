package com.azguards.app.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "location", "viewport" })
public class Geometry implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -493588468724965285L;
	@JsonProperty("location")
	private Location location;
	@JsonProperty("viewport")
	private Viewport viewport;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("location")
	public Location getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(final Location location) {
		this.location = location;
	}

	@JsonProperty("viewport")
	public Viewport getViewport() {
		return viewport;
	}

	@JsonProperty("viewport")
	public void setViewport(final Viewport viewport) {
		this.viewport = viewport;
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
	public String toString() {
		return new ToStringBuilder(this).append("location", location).append("viewport", viewport).append("additionalProperties", additionalProperties)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(viewport).append(location).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof Geometry == false) {
			return false;
		}
		Geometry rhs = (Geometry) other;
		return new EqualsBuilder().append(viewport, rhs.viewport).append(location, rhs.location).append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}
