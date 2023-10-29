package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CareerJobLevel;

@Repository
public interface CareerJobLevelRepository extends JpaRepository<CareerJobLevel, String> {

}
