package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.Service;
import com.azguards.app.dao.ServiceDao;
import com.azguards.app.repository.ServiceRepository;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class ServiceDaoImpl implements ServiceDao {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public List<Service> addUpdateServices(List<Service> services) throws ValidationException {
		try {
			return serviceRepository.saveAll(services);
		} catch (DataIntegrityViolationException ex) {
			log.error(messageTranslator.toLocale("services.already.exist",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("services.already.exist"));
		}
	}

	@Override
	public Optional<Service> getServiceById(String id) {
		return serviceRepository.findById(id);
	}

	@Override
	public Page<Service> getAllServices(Pageable pageable) {
		return serviceRepository.findAll(pageable);
	}

	@Override
	public List<Service> getAll() {
		return serviceRepository.findAll();
	}

	@Override
	public List<Service> getAllByIds(List<String> ids) {
		return serviceRepository.findAllById(ids);
	}

	@Override
	public List<Service> findByNameIgnoreCaseIn(List<String> names) {
		return serviceRepository.findByNameIgnoreCaseIn(names);
	}

}
