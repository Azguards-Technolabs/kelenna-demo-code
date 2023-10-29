package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azguards.app.bean.CourseFunding;

public interface CourseFundingRepository extends JpaRepository<CourseFunding, String> {
}
