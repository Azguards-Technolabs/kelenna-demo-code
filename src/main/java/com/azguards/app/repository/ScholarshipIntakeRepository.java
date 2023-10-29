package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.ScholarshipIntake;

@Repository
public interface ScholarshipIntakeRepository extends JpaRepository<ScholarshipIntake, String> {

	List<ScholarshipIntake> findByScholarshipIdAndIdIn(String scholarshipId, List<String> ids);

	public void deleteByScholarshipIdAndIdIn(String scholarshipId, List<String> ids);

}