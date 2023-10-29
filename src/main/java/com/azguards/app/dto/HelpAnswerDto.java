package com.azguards.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HelpAnswerDto {

	@JsonProperty("user_id")
	@NotBlank(message = "{user_id.is_required}")
    private String userId;
	
	@JsonProperty("help_id")
	@NotBlank(message = "{help_id.is_required}")	
    private String helpId;
	
	@JsonProperty("answer")
	@NotBlank(message = "{answer.is_required}")
    private String answer;
	
	@JsonProperty("file_url")
    private String fileUrl;
}
