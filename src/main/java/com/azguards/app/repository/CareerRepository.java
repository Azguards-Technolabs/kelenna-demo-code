package com.azguards.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Careers;

@Repository
public interface CareerRepository extends JpaRepository<Careers, String> {
	Page<Careers> findByCareerContainingIgnoreCase(String career, Pageable pageable);
}
