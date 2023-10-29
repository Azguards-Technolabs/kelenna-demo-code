package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CareerTestResult;
import com.azguards.app.enumeration.CareerTestEntityType;

public interface CareerTestResultDao {
	void saveAll(List<CareerTestResult> careerTestResults);

	List<CareerTestResult> findByUserIdAndEntityType(String userId, CareerTestEntityType entityType);

	void deleteAll(List<CareerTestResult> enities);
}
