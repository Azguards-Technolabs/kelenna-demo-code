package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.Scholarship;
import com.azguards.app.dto.ScholarshipLevelCountDto;

public interface ScholarshipDao {

	public Scholarship saveScholarship(Scholarship scholarship);

	public Optional<Scholarship> getScholarshipById(String id);

	public Page<Scholarship> getScholarshipList(final String countryName, final String instituteId,
			final String searchKeyword, Pageable pageable);
	
	public List<ScholarshipLevelCountDto> getScholarshipCountGroupByLevel();

	public void deleteScholarship(String scholarshipId);

	public Long getCountByInstituteId(String instituteId);

	public List<Scholarship> getScholarshipByIds(List<String> scholarshipIds);
	
	public List<Scholarship> findByReadableIdIn(List<String> readableIds);

	public Scholarship findByReadableId(String readableId);
}
