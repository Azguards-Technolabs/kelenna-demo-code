package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteWorldRankingHistoryDto {
	
	@JsonProperty("world_ranking_history_id")
	private String id;
	
	@JsonProperty("world_ranking")
	private Integer worldRanking;
	
	@JsonProperty("institute_name")
	private String instituteName;
}
