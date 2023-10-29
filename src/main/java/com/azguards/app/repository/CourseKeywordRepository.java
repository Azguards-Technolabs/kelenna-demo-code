package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseKeywords;

@Repository
public interface CourseKeywordRepository extends JpaRepository<CourseKeywords, String>{

	public List<CourseKeywords> findByKeywordContaining(String keyword);
	
}
