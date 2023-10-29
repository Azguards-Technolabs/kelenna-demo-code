package com.azguards.app.dto;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Valid
public class CourseOtherRequirementDto {

	@JsonProperty("vaccine")
	private CourseVaccineRequirementDto vaccine;

	@JsonProperty("work_experience")
	private CourseWorkExperienceRequirementDto workExperience;

	@JsonProperty("work_placement")
	private CourseWorkPlacementRequirementDto workPlacement;

	@JsonProperty("research_proposal")
	private CourseResearchProposalRequirementDto researchProposal;

}
