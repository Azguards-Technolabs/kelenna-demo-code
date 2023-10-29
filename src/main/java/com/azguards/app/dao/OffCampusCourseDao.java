package com.azguards.app.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.OffCampusCourse;

public interface OffCampusCourseDao {
	public OffCampusCourse saveOrUpdate(OffCampusCourse offCampusCourse);

	public Optional<OffCampusCourse> getById(String offCampusCourseId);

	public Page<OffCampusCourse> getOffCampusCoursesByInstituteId(String instituteId, Pageable pageable);
}
