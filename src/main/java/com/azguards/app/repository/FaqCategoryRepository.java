package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.FaqCategory;

@Repository
public interface FaqCategoryRepository extends JpaRepository<FaqCategory, String> {
}
