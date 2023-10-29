package com.azguards.app.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.FaqSubCategory;
import com.azguards.common.lib.exception.ValidationException;

@Repository
public interface FaqSubCategoryDao {

	Optional<FaqSubCategory> getById(String faqSubCategoryId);

	void saveOrUpdate(FaqSubCategory faqSubCategory) throws ValidationException;

	Page<FaqSubCategory> findByCategoryId(String faqCategoryId, Pageable pageable);

	void deleteById(String faqSubCategoryId);
}
