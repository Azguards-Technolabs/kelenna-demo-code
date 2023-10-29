package com.azguards.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.FaqSubCategory;

@Repository
public interface FaqSubCategoryRepository extends JpaRepository<FaqSubCategory, String> {
	public Page<FaqSubCategory> findByFaqCategoryId(String faqCategoryId, Pageable pageable);
}
