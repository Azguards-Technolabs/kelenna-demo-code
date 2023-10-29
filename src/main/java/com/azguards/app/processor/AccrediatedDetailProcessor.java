package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.AccrediatedDetail;
import com.azguards.app.dao.AccrediatedDetailDao;
import com.azguards.app.dto.AccrediatedDetailDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class AccrediatedDetailProcessor {

	@Autowired
	private AccrediatedDetailDao accrediatedDetailDao;
	
	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private StorageHandler storageHandler;

	public AccrediatedDetailDto addAccrediatedDetail(AccrediatedDetailDto accrediatedDetailDto) {
		log.info("Checking accrediation is already exists or not" + accrediatedDetailDto.getEntityId());
		AccrediatedDetailDto detailDto = new AccrediatedDetailDto();
		AccrediatedDetail accrediatedDetailFromDB = accrediatedDetailDao.getAccrediationBiNameAndEntityId(accrediatedDetailDto.getAccrediatedName(), 
				accrediatedDetailDto.getEntityId());
		if(ObjectUtils.isEmpty(accrediatedDetailFromDB)) {
			log.info("Accrediation is not present in DB hence adding accrediation for entityId " + accrediatedDetailDto.getEntityId());
			AccrediatedDetail accrediatedDetail = new AccrediatedDetail();
			accrediatedDetail.setAccrediatedName(accrediatedDetailDto.getAccrediatedName());
			accrediatedDetail.setAccrediatedWebsite(accrediatedDetailDto.getAccrediatedWebsite());
			accrediatedDetail.setEntityId(accrediatedDetailDto.getEntityId());
			accrediatedDetail.setEntityType(accrediatedDetailDto.getEntityType());
			accrediatedDetail.setDescription(accrediatedDetailDto.getDescription());
			accrediatedDetail.setCreatedBy("API");
			accrediatedDetail.setCreatedOn(new Date());
			log.info("going to save accrediated details in DB for entityId " + accrediatedDetailDto.getEntityId());
			AccrediatedDetail savedAccrediatedDetail = accrediatedDetailDao.addAccrediatedDetail(accrediatedDetail);
			BeanUtils.copyProperties(savedAccrediatedDetail, detailDto);
			return detailDto;
		} else {
			log.info("Accrediation is already present for given entityId "+accrediatedDetailDto.getEntityId()+ " and for accrediatedName "
					 +accrediatedDetailDto.getAccrediatedName());
			BeanUtils.copyProperties(accrediatedDetailFromDB, detailDto);
			return detailDto;
		}
	}
	
	public List<AccrediatedDetailDto> getAccrediationDetailByEntityId(String entityId) throws NotFoundException {
		List<AccrediatedDetailDto> accrediatedDetailDtoList = new ArrayList<>();
		log.info("Get accrediation for entityId " + entityId);
		List<AccrediatedDetail> accrediatedDetails = accrediatedDetailDao.getAccrediationDetailByEntityId(entityId);
		log.info("Checking if accrediatedDetail is present in DB or not for entityId " + entityId);
		if(!CollectionUtils.isEmpty(accrediatedDetails)) {
			log.info("AccrediatedDetail is present in DB start fetching value");
			accrediatedDetails.stream().forEach(accrediatedDetail -> {
				AccrediatedDetailDto accrediatedDetailDto = new AccrediatedDetailDto();
				accrediatedDetailDto.setId(accrediatedDetail.getId());
				accrediatedDetailDto.setEntityId(accrediatedDetail.getEntityId());
				accrediatedDetailDto.setEntityType(accrediatedDetail.getEntityType());
				accrediatedDetailDto.setAccrediatedName(accrediatedDetail.getAccrediatedName());
				accrediatedDetailDto.setAccrediatedWebsite(accrediatedDetail.getAccrediatedWebsite());
				accrediatedDetailDto.setDescription(accrediatedDetail.getDescription());
				try {
					List<StorageDto> storageDTOList = storageHandler.getStorages(accrediatedDetail.getId(),
							EntityTypeEnum.valueOf(accrediatedDetail.getEntityType()), EntitySubTypeEnum.ACCREDIATED);
					accrediatedDetailDto.setStorage(storageDTOList);
				} catch (Exception e) {
					log.error("Error invoking storage service for accrediatedId "+accrediatedDetail.getId());
				}
				accrediatedDetailDtoList.add(accrediatedDetailDto);
			});
		} else {
			log.warn("AccrediatedDetail is not found for entityId "+entityId);
		}
		return accrediatedDetailDtoList;
	}
	
	
	public void deleteAccrediationDetailByEntityId(String entityId) throws NotFoundException, InvokeException {
		log.info("Delete accrediation for entityId " + entityId);
		accrediatedDetailDao.deleteAccrediationDetailByEntityId(entityId);
		log.info("Delete accrediation from storage service for entityId " + entityId);
		storageHandler.deleteStorageBasedOnEntityId(entityId);
	}
	
	public AccrediatedDetailDto updateAccrediatedDetails(String id, AccrediatedDetailDto accrediatedDetailDto) throws NotFoundException {
		AccrediatedDetailDto detailDto = new AccrediatedDetailDto();
		log.info("Get accrediation for id " + id);
		Optional<AccrediatedDetail> optAccrediatedDetail = accrediatedDetailDao.getAccrediatedDetailById(id);
		if(optAccrediatedDetail.isPresent()) {
			log.info("if accrediation is present set values in Obj");
			optAccrediatedDetail.get().setAccrediatedName(accrediatedDetailDto.getAccrediatedName());
			optAccrediatedDetail.get().setAccrediatedWebsite(accrediatedDetailDto.getAccrediatedWebsite());
			optAccrediatedDetail.get().setEntityType(accrediatedDetailDto.getEntityType());
			optAccrediatedDetail.get().setEntityId(accrediatedDetailDto.getEntityId());
			optAccrediatedDetail.get().setDescription(accrediatedDetailDto.getDescription());
			optAccrediatedDetail.get().setUpdateddBy("API");
			optAccrediatedDetail.get().setUpdatedOn(new Date());
			log.info("Save accrediation details in DB for id "+id);
			AccrediatedDetail saveAccrediatedDetail = accrediatedDetailDao.addAccrediatedDetail(optAccrediatedDetail.get());
			BeanUtils.copyProperties(saveAccrediatedDetail, detailDto);
		} else {
			log.error(messageTranslator.toLocale("accrediated_detail.id.notfound",id,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("accrediated_detail.id.notfound",id));
		}
		return detailDto;
	}
	
	public AccrediatedDetailDto getAccrediatedById(String id) throws NotFoundException, InvokeException  {
		log.info("Get accrediation details for id " + id);
		AccrediatedDetailDto accrediatedDetailDto = new AccrediatedDetailDto();
		log.info("fetching accrediated details by accrediated id " + id);
		Optional<AccrediatedDetail> optAccrediatedDetail = accrediatedDetailDao.getAccrediatedDetailById(id);
		if(optAccrediatedDetail.isPresent()) {
			log.info("if accrediated details are present then copying values into DTO");
			BeanUtils.copyProperties(optAccrediatedDetail.get(), accrediatedDetailDto);
			log.info("going to fetch storage details fro accrediatedId "+optAccrediatedDetail.get().getId());
			List<StorageDto> storageDTOList = storageHandler.getStorages(optAccrediatedDetail.get().getId(), EntityTypeEnum.valueOf(optAccrediatedDetail.get().getEntityType()), EntitySubTypeEnum.ACCREDIATED);
			accrediatedDetailDto.setStorage(storageDTOList);
		} else {
			log.error(messageTranslator.toLocale("accrediated_detail.id.notfound",id,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("accrediated_detail.id.notfound",id));
		}
		return accrediatedDetailDto;
	}
	
	public void deleteAccrediationDetailById(String id) throws NotFoundException, InvokeException {
		log.info("Delete accrediation for id " + id);
		accrediatedDetailDao.deleteAccrediationDetailById(id);
		log.info("Delete accrediation from storage service for id " + id);
		storageHandler.deleteStorageBasedOnEntityId(id);
	}
	
	public List<AccrediatedDetailDto> getAllAccrediationDetails() {
		log.info("getting all accrediation details");
		List<AccrediatedDetailDto> accrediatedDetailDtos = new ArrayList<>();
		log.info("going to fetch accrediated details from DB");
		List<AccrediatedDetail> accrediatedDetails = accrediatedDetailDao.getAllAccrediationDetails();
		if(!CollectionUtils.isEmpty(accrediatedDetails)) {
			log.info("accrediated details are present and start iterating");
			accrediatedDetails.stream().forEach(accrediatedDetail -> {
				AccrediatedDetailDto accrediatedDetailDto = new AccrediatedDetailDto();
				accrediatedDetailDto.setId(accrediatedDetail.getId());
				accrediatedDetailDto.setAccrediatedName(accrediatedDetail.getAccrediatedName());
				accrediatedDetailDto.setAccrediatedWebsite(accrediatedDetail.getAccrediatedWebsite());
				accrediatedDetailDto.setEntityId(accrediatedDetail.getEntityId());
				accrediatedDetailDto.setEntityType(accrediatedDetail.getEntityType());
				accrediatedDetailDto.setDescription(accrediatedDetail.getDescription());
				try {
					log.info("going to call storage service for id "+accrediatedDetail.getId());
					List<StorageDto> storageDTOList = storageHandler.getStorages(accrediatedDetail.getId(), EntityTypeEnum.valueOf(accrediatedDetail.getEntityType()), EntitySubTypeEnum.ACCREDIATED);
					accrediatedDetailDto.setStorage(storageDTOList);
				} catch (Exception e) {
					log.error("Error invoking storage service for accrediatedId "+accrediatedDetail.getId());
				}
				accrediatedDetailDtos.add(accrediatedDetailDto);
			});
		}
		return accrediatedDetailDtos;
	}
}
 