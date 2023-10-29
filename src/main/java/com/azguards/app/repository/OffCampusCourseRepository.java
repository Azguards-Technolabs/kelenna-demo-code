package com.azguards.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.OffCampusCourse;

@Repository
public interface OffCampusCourseRepository extends JpaRepository<OffCampusCourse, String> {
	public Page<OffCampusCourse> findByCourseInstituteId(String instituteId, Pageable pageable);
}