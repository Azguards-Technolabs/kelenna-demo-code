package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.azguards.app.bean.Scholarship;
import com.azguards.app.dao.ScholarshipDao;
import com.azguards.app.dto.ScholarshipLevelCountDto;
import com.azguards.app.repository.ScholarshipRepository;
import com.azguards.app.specification.ScholarshipSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScholarshipDaoImpl implements ScholarshipDao {

	@Autowired
	private ScholarshipRepository scholarshipRepository;

	@Override
	public Scholarship saveScholarship(final Scholarship scholarship) {
		return scholarshipRepository.save(scholarship);
	}

	@Override
	public Optional<Scholarship> getScholarshipById(final String id) {
		return scholarshipRepository.findById(id);
	}

	@Override
	public Page<Scholarship> getScholarshipList(final String countryName, final String instituteId,
			final String searchKeyword, Pageable pageable) {
		return scholarshipRepository.findAll(
				ScholarshipSpecification.getScholarshipsBasedOnFilters(countryName, instituteId, searchKeyword),
				pageable);
	}

	@Override
	public List<ScholarshipLevelCountDto> getScholarshipCountGroupByLevel() {
		return scholarshipRepository.getScholarshipCountGroupByLevel();
	}

	@Override
	public void deleteScholarship(String scholarshipId) {
		try {
			scholarshipRepository.deleteById(scholarshipId);
		} catch (EmptyResultDataAccessException ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	public Long getCountByInstituteId(String instituteId) {
		return scholarshipRepository.countByInstituteId(instituteId);
	}

	@Override
	public List<Scholarship> getScholarshipByIds(List<String> scholarshipIds) {
		return scholarshipRepository.findAllById(scholarshipIds);
	}
	
	@Override
	public List<Scholarship> findByReadableIdIn(List<String> readableIds) {
		return scholarshipRepository.findByReadableIdIn(readableIds);
	}

	@Override
	public Scholarship findByReadableId(String readableId) {
		return scholarshipRepository.findByReadableId(readableId);
	}
}
