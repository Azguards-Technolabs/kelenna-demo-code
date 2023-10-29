package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Faq;
import com.azguards.app.constant.FaqEntityType;
import com.azguards.common.lib.dto.CountDto;

@Repository
public interface FaqRepository extends JpaRepository<Faq, String> {

	@Query("SELECT faq from Faq faq where :entityId = faq.entityId"
			+ " and (:faqCategoryId is null or :faqCategoryId = '' or :faqCategoryId = faq.faqSubCategory.faqCategory.id)"
			+ " and (:faqSubCategoryId is null or :faqSubCategoryId = '' or :faqSubCategoryId = faq.faqSubCategory.id)"
			+ " and (:searchKeyword is null or :searchKeyword = '' or (faq.title LIKE %:searchKeyword% or faq.description LIKE %:searchKeyword% "
			+ " or faq.description LIKE %:searchKeyword% or faq.faqSubCategory.name LIKE %:searchKeyword% "
			+ " or faq.faqSubCategory.faqCategory.name LIKE %:searchKeyword%))")
	Page<Faq> getFaqList(String entityId, String faqCategoryId, String faqSubCategoryId, String searchKeyword,
			Pageable pageable);
	
	@Query("SELECT new com.azguards.common.lib.dto.CountDto(f.entityId, count(f)) from Faq f WHERE f.entityType = :entityType AND f.entityId IN :entityIds group by f.entityId")
	List<CountDto> countByEntityTypeAndEntityIdIn(FaqEntityType entityType, List<String> entityIds);
	
	List<Faq> findByEntityId(String entityId);
}
