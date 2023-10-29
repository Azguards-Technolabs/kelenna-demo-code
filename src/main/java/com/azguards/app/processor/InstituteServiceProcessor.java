package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteService;
import com.azguards.app.bean.Service;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.InstituteServiceDao;
import com.azguards.app.dao.ServiceDao;
import com.azguards.app.dto.InstituteServiceDto;
import com.azguards.app.dto.ServiceDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class InstituteServiceProcessor {

	@Autowired
	private InstituteServiceDao instituteServiceDao;

	@Autowired
	private InstituteDao instituteDao;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private ServiceProcessor serviceProcessor;

	@Autowired
	private StorageHandler storageHandler;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageTranslator messageTranslator;


	public List<InstituteServiceDto> addInstituteService(String userId, String instituteId, List<InstituteServiceDto> instituteServiceDtos)
			throws NotFoundException, ValidationException {
		log.debug("inside addInstituteService() method");
		log.info("Getting all exsisting services");
		Institute institute = instituteDao.get(instituteId);
		if (ObjectUtils.isEmpty(institute)) {
			log.error(messageTranslator.toLocale("institute-services.invalid.id",instituteId,Locale.US));
			throw new ValidationException( messageTranslator.toLocale("institute-services.invalid.id",instituteId));
		}

		List<ServiceDto> serviceDtos = instituteServiceDtos.stream().map(e -> e.getService())
				.collect(Collectors.toList());
		//////////////////// code for adding the new services //////////////////////////

		List<ServiceDto> newServicesTobeAdded = new ArrayList<>(serviceDtos);

		newServicesTobeAdded.removeIf(e -> !StringUtils.isEmpty(e.getServiceId()));

		if (!newServicesTobeAdded.isEmpty()) {
			newServicesTobeAdded = serviceProcessor.saveAllServices(userId, newServicesTobeAdded);
			serviceDtos.removeIf(e -> StringUtils.isEmpty(e.getServiceId()));
			serviceDtos.addAll(newServicesTobeAdded);
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////

		Set<String> serviceIds = serviceDtos.stream().map(ServiceDto::getServiceId).collect(Collectors.toSet());

		List<Service> servicesFromDb = serviceDao.getAllByIds(serviceIds.stream().collect(Collectors.toList()));
		Map<String, Service> mapServicesById = servicesFromDb.stream()
				.collect(Collectors.toMap(Service::getId, e -> e));
		Map<String, Service> mapServicesByName = servicesFromDb.stream()
				.collect(Collectors.toMap(Service::getName, e -> e));

		if (serviceIds.size() != servicesFromDb.size()) {
			log.error(messageTranslator.toLocale("institute-service.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("institute-service.invalid"));
		}

		List<InstituteService> existingInstituteServices = instituteServiceDao.getAllInstituteService(instituteId);
		Set<String> existingServiceIds = existingInstituteServices.stream().map(e -> e.getService().getId())
				.collect(Collectors.toSet());

		List<InstituteService> listOfServiceToBeSaved = new ArrayList<>();
		for (InstituteServiceDto instituteServiceDto : instituteServiceDtos) {

			String serviceId = instituteServiceDto.getService().getServiceId();
			if (!StringUtils.isEmpty(instituteServiceDto.getService().getServiceName())) {
				serviceId = mapServicesByName.get(instituteServiceDto.getService().getServiceName()).getId();
			}
			if (!existingServiceIds.contains(serviceId)) {
				log.info("No service present for institute with service Id {} adding it to list", serviceId);

				Service service = mapServicesById.get(serviceId);
				if (ObjectUtils.isEmpty(service)) {
					log.error(messageTranslator.toLocale("institute-service.illegal.id" + serviceId,Locale.US));
					throw new NotFoundException(messageTranslator.toLocale("institute-service.illegal.id", serviceId));
				}
				InstituteService instituteService = new InstituteService(institute, service,
						instituteServiceDto.getDescription(), new Date(), new Date(), userId, userId);
				listOfServiceToBeSaved.add(instituteService);
			} else {
				String serviceIdCopy = serviceId;// only to make service id effective final to use in stream
				log.info("Institute service already present for institute service id {} skipping it", serviceId);
				Optional<InstituteService> optionalInstituteService = existingInstituteServices.stream()
						.filter(e -> e.getService().getId().equals(serviceIdCopy)).findFirst();
				if (optionalInstituteService.isPresent()) {
					InstituteService existingInstituteService = optionalInstituteService.get();
					existingInstituteService.setDescription(instituteServiceDto.getDescription());
					existingInstituteService.setUpdatedOn(new Date());
					existingInstituteService.setUpdatedBy(userId);
					listOfServiceToBeSaved.add(existingInstituteService);
				}
			}
		}
		log.info("Persisting resource list to DB ");
		return instituteServiceDao.saveAll(listOfServiceToBeSaved).stream()
				.map(e -> modelMapper.map(e, InstituteServiceDto.class)).collect(Collectors.toList());
	}

	@Transactional(rollbackOn = Throwable.class)
	public void deleteInstituteService(String userId, String instituteServiceId) throws NotFoundException, InvokeException {
		log.debug("inside deleteInstituteService() method");
		//TODO: need to check if user has the access to delete the instituteService 
		instituteServiceDao.delete(instituteServiceId);
		storageHandler.deleteStorageBasedOnEntityId(instituteServiceId);
	}

	public List<InstituteServiceDto> getInstituteServices(String instituteId) {
		log.debug("inside getServiceByInstituteId() method");
		log.info("Getting all services for institute id {}", instituteId);

		List<InstituteService> instituteServices = instituteServiceDao.getAllInstituteService(instituteId);
		List<InstituteServiceDto> instituteServiceResponseDtos = instituteServices.stream()
				.map(e -> modelMapper.map(e, InstituteServiceDto.class)).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(instituteServiceResponseDtos)) {
			log.info("Service from db not empty for institute id " + instituteId);
//////////////// getting logo for services /////////////////////////////////////// 
			List<String> serviceIds = instituteServiceResponseDtos.stream().map(e -> e.getService().getServiceId())
					.collect(Collectors.toList());
			List<StorageDto> serviceLogos;
			try {
				serviceLogos = storageHandler.getStorages(serviceIds, EntityTypeEnum.SERVICE, EntitySubTypeEnum.LOGO);
				Map<String, String> mapServiceIcons = serviceLogos.stream()
						.collect(Collectors.toMap(StorageDto::getEntityId, StorageDto::getFileURL));
///////////////////////////////////////////////////////////////////////////////////////////
//////////////// getting media for instiute services /////////////////////////////////////
				List<StorageDto> mediaStorages = storageHandler
						.getStorages(
								instituteServiceResponseDtos.stream().map(InstituteServiceDto::getId)
										.collect(Collectors.toList()),
								EntityTypeEnum.INSTITUTE_SERVICE, EntitySubTypeEnum.MEDIA);
////////////////////////////////////////////////////////////////////////////////////////
				instituteServiceResponseDtos.stream().forEach(e -> {
					List<StorageDto> serviceMediaStorages = mediaStorages.stream()
							.filter(m -> m.getEntityId().equals(e.getId())).collect(Collectors.toList());
					e.setMedia(serviceMediaStorages);
					e.getService().setIcon(mapServiceIcons.get(e.getService().getServiceId()));
				});
			} catch (NotFoundException | InvokeException e1) {
				log.error("error invoking storage service");
			}
		}
		return instituteServiceResponseDtos;
	}
}
