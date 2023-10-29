package com.azguards.app.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.Subject;

public interface SubjectDao {

	void saveAll(List<Subject> subjects);

	Page<Subject> findByNameContainingIgnoreCaseAndEducationSystemId(String name, String educationSystemId, Pageable pageable);

	Subject findByNameAndEducationSystemId(String name, String educationSystemId);
}
