package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CareerJobType;
import com.azguards.app.dto.JobIdProjection;

@Repository
public interface CareerJobTypeRepository extends JpaRepository<CareerJobType, String> {
	
	public Page<CareerJobType> findByCareerJobsIdInOrderByJobType(List<String> jobId, Pageable pageable);

	@Query("select j.id as jobId from CareerJobType jt join jt.careerJobs j where jt.id = :id")
	public List<JobIdProjection> findJobIdsById(String id);
}
