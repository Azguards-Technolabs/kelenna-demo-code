package com.azguards.app.dao.impl;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.FaqCategory;
import com.azguards.app.dao.FaqCategoryDao;
import com.azguards.app.repository.FaqCategoryRepository;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FaqCategoryDaoImpl implements FaqCategoryDao {

	@Autowired
	private FaqCategoryRepository faqCategoryRepository;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public Optional<FaqCategory> getById(String faqCategoryId) {
		return faqCategoryRepository.findById(faqCategoryId);
	}

	@Override
	public void saveOrUpdate(FaqCategory faqCategory) throws ValidationException {
		try {
			faqCategoryRepository.save(faqCategory);
		} catch (DataIntegrityViolationException ex) {
			log.error(messageTranslator.toLocale("faq.already.category", faqCategory.getName(),Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq.already.category", faqCategory.getName()));
		}
	}

	@Override
	public Page<FaqCategory> findAll(Pageable pageable) {
		return faqCategoryRepository.findAll(pageable);
	}

	@Override
	public void deleteById(String faqCategoryId) {
		try {
			faqCategoryRepository.deleteById(faqCategoryId);
		} catch (EmptyResultDataAccessException ex) {
			log.error(ex.getMessage());
		}
	}

}
