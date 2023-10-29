package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteCampus;

@Repository
public interface InstituteCampusRepository extends JpaRepository<InstituteCampus, String> {

	@Query("SELECT i from InstituteCampus i where i.sourceInstitute.id = :instituteId OR i.destinationInstitute.id = :instituteId")
	public List<InstituteCampus> findInstituteCampuses(String instituteId);
}
