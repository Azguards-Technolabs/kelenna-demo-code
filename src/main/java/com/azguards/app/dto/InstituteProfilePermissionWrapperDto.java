package com.azguards.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class InstituteProfilePermissionWrapperDto {

	private String message;
	private List<InstituteProfilePermissionDto> data;
	private String status;
}
