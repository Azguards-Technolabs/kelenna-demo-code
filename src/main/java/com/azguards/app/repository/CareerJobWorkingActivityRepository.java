package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CareerJobWorkingActivity;

@Repository
public interface CareerJobWorkingActivityRepository extends JpaRepository<CareerJobWorkingActivity, String>{

}
