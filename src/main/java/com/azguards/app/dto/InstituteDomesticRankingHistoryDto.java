package com.azguards.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstituteDomesticRankingHistoryDto {

	@JsonProperty("domestic_ranking_id")
	private String id;

	@JsonProperty("domestic_ranking")
	private Integer domesticRanking;

	@JsonProperty("institute_name")
	private String instituteName;

}
