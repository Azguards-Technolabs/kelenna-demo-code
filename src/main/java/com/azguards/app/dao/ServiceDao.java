package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.Service;
import com.azguards.common.lib.exception.ValidationException;

public interface ServiceDao {

	public List<Service> addUpdateServices(List<Service> service) throws ValidationException;

	public Optional<Service> getServiceById(String id);

	public Page<Service> getAllServices(Pageable pageable);

	public List<Service> getAll();

	public List<Service> getAllByIds(List<String> id);
	
	public List<Service> findByNameIgnoreCaseIn(List<String> names);
}
