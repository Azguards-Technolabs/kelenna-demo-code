package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.RelatedCareer;

@Repository
public interface RelatedCareerRepository extends JpaRepository<RelatedCareer, String> {
	Page<RelatedCareer> findByCareersIdInOrderByRelatedCareer(List<String> careerIds, Pageable pageable);
}
