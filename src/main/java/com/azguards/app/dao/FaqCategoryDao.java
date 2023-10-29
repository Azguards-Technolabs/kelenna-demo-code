package com.azguards.app.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.FaqCategory;
import com.azguards.common.lib.exception.ValidationException;

public interface FaqCategoryDao {

	Optional<FaqCategory> getById(String faqCategoryId);

	void saveOrUpdate(FaqCategory faqCategory) throws ValidationException;

	Page<FaqCategory> findAll(Pageable pageable);

	void deleteById(String faqCategoryId);
}