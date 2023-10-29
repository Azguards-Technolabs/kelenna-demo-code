package com.azguards.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.GlobalFilterSearchDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.local.config.MessageTranslator;

@Service
@Transactional(rollbackFor = Throwable.class)
public class GlobalSearchFilterService implements IGlobalSearchFilterService {

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Autowired
	private CourseDao icourseDaso;

	@Autowired
	private StorageHandler storageHandler;

	@Override
	public Map<String, Object> filterByEntity(GlobalFilterSearchDto globalSearchFilterDto) throws ValidationException, NotFoundException, InvokeException {
		if (EntityTypeEnum.COURSE.equals(globalSearchFilterDto.getEntityType())) {
			AdvanceSearchDto advanceSearchDto = new AdvanceSearchDto();
			BeanUtils.copyProperties(globalSearchFilterDto, advanceSearchDto);
			return filterCoursesByParameters(globalSearchFilterDto, advanceSearchDto);
		} else {
			throw new ValidationException(messageTranslator.toLocale("global-search.invalid.entity_type"));
		}
	}

	private Map<String, Object> filterCoursesByParameters(GlobalFilterSearchDto globalSearchFilterDto, AdvanceSearchDto advacneSearchDto)
			throws ValidationException, NotFoundException, InvokeException {

		List<CourseResponseDto> courseList = icourseDaso.advanceSearch(null, advacneSearchDto, globalSearchFilterDto);
		for (CourseResponseDto courseResponseDto : courseList) {
			List<StorageDto> storageDTOList = storageHandler.getStorages(courseResponseDto.getInstituteId(), EntityTypeEnum.INSTITUTE,EntitySubTypeEnum.IMAGES);
			courseResponseDto.setStorageList(storageDTOList);
		}
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("entity", courseList);
		returnMap.put("count", courseList.get(0).getTotalCount());
		return returnMap;
	}
}
