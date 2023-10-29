package com.azguards.app.processor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.azguards.app.bean.InstituteCategoryType;
import com.azguards.app.dao.InstituteDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class MasterDataImportProcessor {

	@Autowired
	private InstituteDao instituteDao;
	
	public void importInstituteCategoryType() {
		log.info("Importing Institute Category Type if not exists");
		List<InstituteCategoryType> categoryTypes = instituteDao.getAllCategories();
		if(CollectionUtils.isEmpty(categoryTypes)) {
			InstituteCategoryType publicType = new InstituteCategoryType();
			publicType.setName("Public");
			publicType.setCreatedBy("API");
			publicType.setCreatedOn(new Date());
			publicType.setUpdatedBy("API");
			publicType.setUpdatedOn(new Date());
			InstituteCategoryType privateType = new InstituteCategoryType();
			privateType.setName("Private");
			privateType.setCreatedBy("API");
			privateType.setCreatedOn(new Date());
			privateType.setUpdatedBy("API");
			privateType.setUpdatedOn(new Date());
			instituteDao.addInstituteCategoryTypes(Arrays.asList(publicType,privateType));
		}
	}
}
