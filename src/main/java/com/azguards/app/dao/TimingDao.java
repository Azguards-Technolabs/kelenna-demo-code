package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import com.azguards.app.bean.Timing;
import com.azguards.common.lib.enumeration.EntityTypeEnum;

public interface TimingDao {

	List<Timing> saveAll(List<Timing> timings);

	List<Timing> findByEntityTypeAndEntityIdIn(EntityTypeEnum entityType, List<String> entityId);

	void deleteAll(List<Timing> timings);

	void deleteByEntityTypeAndEntityId(EntityTypeEnum entityType, String entityId);

	Optional<Timing> findById(String id);

	Timing findByEntityTypeAndEntityIdAndId(EntityTypeEnum entityType, String entityId, String id);

	void deleteByEntityTypeAndEntityIdAndId(EntityTypeEnum entityType, String entityId, String id);

	void deleteByEntityTypeAndEntityIdIn(EntityTypeEnum entityType, List<String> entityIds);
}
