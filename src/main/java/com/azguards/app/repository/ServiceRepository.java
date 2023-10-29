package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
	public Service findByNameIgnoreCase(String name);
	public List<Service> findByNameIgnoreCaseIn(List<String> names);
}
