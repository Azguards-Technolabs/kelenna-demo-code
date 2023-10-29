package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseCurriculum;

@Repository
public interface CourseCurriculumRepository extends JpaRepository<CourseCurriculum, String> {

}
