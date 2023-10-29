package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.ScholarshipIntake;
import com.azguards.common.lib.exception.ValidationException;

public interface ScholarshipIntakeDao {

	List<ScholarshipIntake> saveAll(List<ScholarshipIntake> scholarshipIntakes) throws ValidationException;

	void deleteByScholarshipIdAndIdIn(String scholarshipId, List<String> ids);

	List<ScholarshipIntake> findByScholarshipIdAndIdIn(String scholarshipId, List<String> ids);

}
