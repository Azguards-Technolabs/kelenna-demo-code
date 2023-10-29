package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azguards.app.bean.Timing;
import com.azguards.app.dao.TimingDao;
import com.azguards.app.repository.TimingRepository;
import com.azguards.common.lib.enumeration.EntityTypeEnum;

@Service
public class TimingDaoImpl implements TimingDao {

	@Autowired
	private TimingRepository timingRepository;

	@Override
	public List<Timing> saveAll(List<Timing> timings) {
		return timingRepository.saveAll(timings);
	}

	@Override
	public List<Timing> findByEntityTypeAndEntityIdIn(EntityTypeEnum entityType, List<String> entityIds) {
		return timingRepository.findByEntityTypeAndEntityIdIn(entityType, entityIds);
	}

	@Override
	public void deleteAll(List<Timing> timings) {
		timingRepository.deleteAll(timings);
	}

	@Override
	public void deleteByEntityTypeAndEntityId(EntityTypeEnum entityType, String entityId) {
		timingRepository.deleteByEntityTypeAndEntityId(entityType, entityId);
	}

	@Override
	public Optional<Timing> findById(String id) {
		return timingRepository.findById(id);
	}

	@Override
	public void deleteByEntityTypeAndEntityIdAndId(EntityTypeEnum entityType, String entityId, String id) {
		timingRepository.deleteByEntityTypeAndEntityIdAndId(entityType, entityId, id);
	}

	@Override
	public Timing findByEntityTypeAndEntityIdAndId(EntityTypeEnum entityType, String entityId, String id) {
		return timingRepository.findByEntityTypeAndEntityIdAndId(entityType, entityId, id);
	}

	@Override
	public void deleteByEntityTypeAndEntityIdIn(EntityTypeEnum entityType, List<String> entityIds) {
		timingRepository.deleteByEntityTypeAndEntityIdIn(entityType, entityIds);
	}

}
