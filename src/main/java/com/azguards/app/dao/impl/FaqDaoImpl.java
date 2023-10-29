package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Faq;
import com.azguards.app.constant.FaqEntityType;
import com.azguards.app.dao.FaqDao;
import com.azguards.app.repository.FaqRepository;
import com.azguards.common.lib.dto.CountDto;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FaqDaoImpl implements FaqDao {

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private FaqRepository faqRepository;

	@Override
	public void saveOrUpdate(Faq faq) throws ValidationException {
		try {
			faqRepository.save(faq);
		} catch (DataIntegrityViolationException ex) {
			log.error(messageTranslator.toLocale("faq-daolmpl.already.category_exist",Locale.US));
			throw new ValidationException(
					messageTranslator.toLocale("faq-daolmpl.already.category_exist"));
		}
	}

	@Override
	public Optional<Faq> getById(String id) {
		return faqRepository.findById(id);
	}

	@Override
	public Page<Faq> getFaqList(String entityId, String faqCategoryId, String faqSubCategoryId, String searchKeyword,
			Pageable pageable) {
		return faqRepository.getFaqList(entityId, faqCategoryId, faqSubCategoryId, searchKeyword, pageable);
	}

	@Override
	public void deleteFaqById(String faqId) {
		try {
			faqRepository.deleteById(faqId);
		} catch (EmptyResultDataAccessException ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	public List<CountDto> countByEntityTypeAndEntityIdIn(FaqEntityType entityType, List<String> entityIds) {
		return faqRepository.countByEntityTypeAndEntityIdIn(entityType, entityIds);
	}
}
