package com.azguards.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionReviewWrapperDto {

	private String message;
	private List<QuestionReviewDto> data;
	private String status;
}