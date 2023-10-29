package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import com.azguards.app.bean.InstituteEnglishRequirements;
import com.azguards.common.lib.exception.ValidationException;

public interface InstituteEnglishRequirementsDao {

	public InstituteEnglishRequirements addUpdateInsituteEnglishRequirements(
			InstituteEnglishRequirements instituteEnglishRequirements) throws ValidationException;

	public Optional<InstituteEnglishRequirements> getInsituteEnglishRequirementsById(String englishRequirementsId);

	public List<InstituteEnglishRequirements> getInsituteEnglishRequirementsByInstituteId(String instituteId);

	public void deleteInstituteEnglishRequirementsById(String englishRequirementsId);
}
