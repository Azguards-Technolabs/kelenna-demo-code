package com.azguards.app.dao.impl;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.FaqSubCategory;
import com.azguards.app.dao.FaqSubCategoryDao;
import com.azguards.app.repository.FaqSubCategoryRepository;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FaqSubCategoryDaoImpl implements FaqSubCategoryDao {

	@Autowired
	private FaqSubCategoryRepository faqSubCategoryRepository;

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public Optional<FaqSubCategory> getById(String faqSubCategoryId) {
		return faqSubCategoryRepository.findById(faqSubCategoryId);
	}
	
	@Override
	public void saveOrUpdate(FaqSubCategory faqSubCategory) throws ValidationException {
		try {
			faqSubCategoryRepository.save(faqSubCategory);
		} catch (Exception ex) {
			log.error(messageTranslator.toLocale("auth-provision.scope.created", faqSubCategory.getName(),Locale.US));
			throw new ValidationException(messageTranslator.toLocale("auth-provision.scope.created", faqSubCategory.getName()));
		}
	}

	@Override
	public Page<FaqSubCategory> findByCategoryId(String faqCategoryId, Pageable pageable) {
		return faqSubCategoryRepository.findByFaqCategoryId(faqCategoryId, pageable);
	}

	@Override
	public void deleteById(String faqSubCategoryId) {
		try {
			faqSubCategoryRepository.deleteById(faqSubCategoryId);
		} catch (EmptyResultDataAccessException ex) {
			log.error(ex.getMessage());
		}
	}
}
