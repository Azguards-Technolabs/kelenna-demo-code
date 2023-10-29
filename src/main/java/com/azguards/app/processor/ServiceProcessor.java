package com.azguards.app.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.Service;
import com.azguards.app.dao.ServiceDao;
import com.azguards.app.dto.ServiceDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class ServiceProcessor {

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StorageHandler storageHandler;

	@Autowired
	@Qualifier("importServiceJob")
	private Job job;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	public List<ServiceDto> saveAllServices(String userId, List<ServiceDto> serviceDtos) throws ValidationException {
		List<String> allNames = serviceDtos.stream().map(ServiceDto::getServiceName).collect(Collectors.toList());
		List<Service> existingServices = serviceDao.findByNameIgnoreCaseIn(allNames);
		List<String> existingNames = existingServices.stream().map(Service::getName).collect(Collectors.toList());
		serviceDtos.removeIf(e -> existingNames.contains(e.getServiceName()));
		List<Service> services = new ArrayList<>();
		if (!serviceDtos.isEmpty()) {
			services = serviceDtos.stream().map(e -> modelMapper.map(e, Service.class)).collect(Collectors.toList());
		}
		services.addAll(existingServices);
		services.stream().forEach(e -> {
			if (ObjectUtils.isEmpty(e.getCreatedOn())) {
				e.setCreatedBy(userId);
				e.setCreatedOn(new Date());
			}
			e.setUpdatedBy(userId);
			e.setUpdatedOn(new Date());
		});
		return serviceDao.addUpdateServices(services).stream().map(e -> modelMapper.map(e, ServiceDto.class))
				.collect(Collectors.toList());
	}

	public PaginationResponseDto getAllServices(final Integer pageNumber, final Integer pageSize)
			throws NotFoundException, InvokeException {
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<Service> servicesPage = serviceDao.getAllServices(pageable);
		List<ServiceDto> serviceDtos = servicesPage.getContent().stream().map(e -> modelMapper.map(e, ServiceDto.class))
				.collect(Collectors.toList());
		if (!serviceDtos.isEmpty()) {
			List<String> serviceIds = serviceDtos.stream().map(ServiceDto::getServiceId).collect(Collectors.toList());
			List<StorageDto> storages = storageHandler.getStorages(serviceIds, EntityTypeEnum.SERVICE,
					EntitySubTypeEnum.LOGO);
			Map<String, String> serviceLogoMap = storages.stream()
					.collect(Collectors.toMap(StorageDto::getEntityId, StorageDto::getFileURL));
			serviceDtos.stream().forEach(e -> e.setIcon(serviceLogoMap.get(e.getServiceId())));
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(PaginationUtil.getStartIndex(pageNumber, pageSize),
				pageSize, ((Long) servicesPage.getTotalElements()).intValue(), serviceDtos);
	}
	
	public void importServices(final MultipartFile multipartFile) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.debug("Inside importServices() method");
		log.info("Calling methiod to save service data");
		
		File f = File.createTempFile("services", ".csv");
		multipartFile.transferTo(f);
		
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("csv-file", f.getAbsolutePath());
		jobParametersBuilder.addString("execution-id", "ServiceUploader-"+UUID.randomUUID().toString());
		jobLauncher.run(job, jobParametersBuilder.toJobParameters());
	}
}
