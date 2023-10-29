package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CareerJobSubject;

@Repository
public interface CareerJobSubjectRepository extends JpaRepository<CareerJobSubject, String> {

	public Page<CareerJobSubject> findByCareerJobsIdInOrderBySubject(List<String> jobIds, Pageable pageable);
}
