package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseIntake;

@Repository
public interface CourseIntakeRepository extends JpaRepository<CourseIntake, String> {

}
