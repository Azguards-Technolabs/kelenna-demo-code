package com.azguards.app.processor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.CareerTestResult;
import com.azguards.app.dao.CareerTestResultDao;
import com.azguards.app.enumeration.CareerTestEntityType;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CareerTestResultProcessor {

	@Autowired
	private CareerTestResultDao careerTestResultDao;

	@Transactional
	public void saveOrUpdateResult(String userId, String entityType, List<String> entityIds) {
		log.info("Inside CareerTestResultProcessor.careerTestResultDao");
		List<CareerTestResult> records = careerTestResultDao.findByUserIdAndEntityType(userId,
				CareerTestEntityType.valueOf(entityType));
		List<CareerTestResult> toBeDeleted = records.stream().filter(e -> !Utils.contains(entityIds, e.getEntityId()))
				.toList();
		careerTestResultDao.deleteAll(toBeDeleted);
		entityIds.stream().forEach(e -> {
			CareerTestResult result = records.stream().filter(r -> r.getEntityId().equals(e)).findAny().orElse(null);
			if (ObjectUtils.isEmpty(result)) {
				result = new CareerTestResult();
				result.setCreatedBy(userId);
				result.setCreatedOn(new Date());
			}
			result.setEntityId(e);
			result.setEntityType(CareerTestEntityType.valueOf(entityType));
			result.setUserId(userId);
			result.setUpdatedBy(userId);
			result.setUpdatedOn(new Date());
			if (ObjectUtils.isEmpty(result.getId())) {
				records.add(result);
			}
		});
		careerTestResultDao.saveAll(records);
	}

	public boolean isTestCompleted(String userId) {
		boolean result = false;
		if (!CollectionUtils
				.isEmpty(careerTestResultDao.findByUserIdAndEntityType(userId, CareerTestEntityType.CAREER))) {
			result = true;
		}
		return result;
	}
}
