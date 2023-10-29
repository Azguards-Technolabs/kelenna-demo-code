package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;

import com.azguards.app.bean.CourseCurriculum;

public interface CourseCurriculumDao {
	public Optional<CourseCurriculum> getById(String id);
	
	@Cacheable(value = "cacheCourseCurriculumList", unless = "#result == null")
	public List<CourseCurriculum> getAll();
}
