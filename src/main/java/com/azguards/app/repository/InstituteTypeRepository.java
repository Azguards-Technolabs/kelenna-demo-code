package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteType;

@Repository
public interface InstituteTypeRepository extends JpaRepository<InstituteType, String>{

	public List<InstituteType> findByCountryName(String countryName);

	public InstituteType findByNameAndCountryName(String name, String countryName);
}
