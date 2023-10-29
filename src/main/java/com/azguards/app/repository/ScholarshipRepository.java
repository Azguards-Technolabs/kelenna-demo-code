package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Scholarship;
import com.azguards.app.dto.ScholarshipLevelCountDto;

@Repository
public interface ScholarshipRepository
		extends JpaRepository<Scholarship, String>, JpaSpecificationExecutor<Scholarship> {

	public Long countByInstituteId(String instituteId);

	@Query("SELECT new com.azguards.app.dto.ScholarshipLevelCountDto(l.id, l.code, l.name, COUNT(s.id)) "
			+ "from Scholarship s join s.levels l group by l.id")
	public List<ScholarshipLevelCountDto> getScholarshipCountGroupByLevel();
	
	public List<Scholarship> findByReadableIdIn(List<String> readableIds);

	public Scholarship findByReadableId(String readableId);
}
