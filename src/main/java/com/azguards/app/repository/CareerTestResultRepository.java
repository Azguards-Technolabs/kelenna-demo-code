package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CareerTestResult;
import com.azguards.app.enumeration.CareerTestEntityType;

@Repository
public interface CareerTestResultRepository extends JpaRepository<CareerTestResult, String> {

	public List<CareerTestResult> findByUserIdAndEntityType(String userId, CareerTestEntityType entityType);
}
