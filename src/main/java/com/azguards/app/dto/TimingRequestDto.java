package com.azguards.app.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TimingRequestDto {

	@JsonProperty("timing_id")
	private String id;

	@JsonProperty("timings")
	@NotEmpty(message = "{timings.is_required}")
	private List<DayTimingDto> timings;

	@NotEmpty(message = "{timing_type.is_required}")
	@JsonProperty("timing_type")
	private String timingType;

	@NotEmpty(message = "{entity_type.is_required}")
	@JsonProperty("entity_type")
	private String entityType;

//	we cant put this validation as while saving/updating course we cant put the entity_id inthe object 
//	but while using the timing controller we need it so we need to do it manually 
//	@NotEmpty(message = "{entity_id.is_required}")
	@JsonProperty("entity_id")
	private String entityId;

}
