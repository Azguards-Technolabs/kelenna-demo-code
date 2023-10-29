package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteService;
import com.azguards.common.lib.dto.CountDto;

@Repository
public interface InstituteServiceRepository extends JpaRepository<InstituteService, String> {

	public List<InstituteService> findByInstituteId(String instituteId);

	public InstituteService findByInstituteIdAndServiceId(String instituteId, String serviceId);

	@Query("SELECT new com.azguards.common.lib.dto.CountDto(s.institute.id, count(s)) from InstituteService s JOIN s.institute i WHERE i.id IN :instituteIds group by i.id")
	public List<CountDto> countByInstituteIdsIn(List<String> instituteIds);
}
