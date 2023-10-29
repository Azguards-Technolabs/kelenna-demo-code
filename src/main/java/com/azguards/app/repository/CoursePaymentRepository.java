package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CoursePayment;

@Repository
public interface CoursePaymentRepository extends JpaRepository<CoursePayment, String> {

}
