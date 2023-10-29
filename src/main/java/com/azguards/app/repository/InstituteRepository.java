package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Institute;
import com.azguards.app.dto.InstituteFacultyDto;
import com.azguards.app.dto.InstituteResponseDto;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, String> {

	public Page<Institute> findByCountryName(String countryName, @PageableDefault Pageable pageable);

	@Query("SELECT COUNT(*) FROM Institute i where i.countryName = :countryName")
	public Integer getTotalCountOfInstituteByCountryName(String countryName);

	public List<Institute> findByCityName(String cityName);

	@Query("SELECT i from Institute i where i.id != :id and i.name = :name and (i.isDeleted is null or i.isDeleted = false  )")
	public List<Institute> findByIdNotAndNameAndIsDeletedFalse(String id, String name);

	@Query("SELECT new com.azguards.app.dto.InstituteFacultyDto(f.id, f.name, count(*) as courseCount) "
			+ "from Course c "
			+ "join c.faculty f  "
			+ "where  c.institute.id = :instituteId "
			+ "group by c.faculty "
			+ "order by f.name")
	public List<InstituteFacultyDto> findFacultyWithCourseCountById(String instituteId);

	@Query("SELECT new com.azguards.app.dto.InstituteResponseDto(i.id,i.name, i.worldRanking, i.cityName, i.countryName,i.state, "
			+ "i.website, i.aboutInfo, i.latitude, i.longitude, i.phoneNumber,i.whatsNo, "
			+ "(select count(c.id) from Course c where c.institute.id = i.id ), i.email, i.address, i.domesticRanking, "
			+ "i.tagLine, i.createdOn) from Institute i  where i.id in :instituteIds")
	public List<InstituteResponseDto> findByIdIn(List<String> instituteIds);

	public List<Institute> findByReadableIdIn(List<String> readableIds);

	public Institute findByReadableId(String readableId);
}
