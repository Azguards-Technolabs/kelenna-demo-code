package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InstituteProfilePermissionDto {

	@JsonProperty("user_profile_data_permission_id")
	private String id;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("entity_id")
	private String entityId;

	@JsonProperty("entity_permission")
	private String entityPermission;

	@JsonProperty("entity_type")
	private String entityType;

}
