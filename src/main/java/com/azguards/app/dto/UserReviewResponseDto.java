package com.azguards.app.dto;

import java.util.Map;

import lombok.Data;

@Data
public class UserReviewResponseDto {
	private Map<String,Object> data;
	private String message;
	private String status;
}
