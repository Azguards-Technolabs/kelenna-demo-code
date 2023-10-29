package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionReviewDto {

	@JsonProperty("review_question_id")
	private String reviewQuestionId;
	
	@JsonProperty("rating_id")
	private Double rating;
}