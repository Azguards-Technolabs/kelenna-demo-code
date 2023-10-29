package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseDeliveryModes;
import com.azguards.common.lib.dto.eligibility.FundingResponseDto;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.dto.transaction.ViewTransactionDto;
import com.azguards.common.lib.dto.user.UserInitialInfoDto;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.handler.EligibilityHandler;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.common.lib.handler.UserHandler;
import com.azguards.common.lib.handler.ViewTransactionHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonProcessor {
	
	@Autowired
	@Lazy
	private InstituteProcessor instituteProcessor;

	@Autowired
	@Lazy
	private CourseProcessor courseProcessor;

	@Autowired
	private EligibilityHandler eligibilityHandler;

	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;
	
	@Autowired
	private ViewTransactionHandler viewTransactionHandler;
	
	@Autowired
	private NotificationProcessor notificationProcessor;

	@Autowired
	private ConversionProcessor conversionProcessor;

	public void notifyCourseUpdates(String notificationType, List<Course> courses) {
		courses.stream().forEach(c -> {
			if (!ObjectUtils.isEmpty(c)) {
				log.info("Notify course information changed");
				try {
					List<ViewTransactionDto> favoriteTransactionList = viewTransactionHandler.getAllUsersWhoMarkCourseFavorite(c.getUpdatedBy(), c.getId());
					notificationProcessor.notifyCourseChanged(c, favoriteTransactionList, notificationType);
				} catch (InvokeException e) {
					log.error("error retriving users who marked course as favorite",e);
				}
			}				
		});
	}
	
	public boolean checkIfPriceChanged(List<CourseDeliveryModes> beforeUpdate, List<CourseDeliveryModes> afterUpdate) {
		Map<String, CourseDeliveryModes> afterUpdateMap = afterUpdate.stream().collect(Collectors.toMap(CourseDeliveryModes::getId, e -> e));
//		return beforeUpdate.stream().anyMatch(e -> afterUpdateMap.containsKey(e.getId()) && 
//				!(e.getDomesticFee().equals(afterUpdateMap.get(e.getId()).getDomesticFee()) 
//						&& e.getInternationalFee().equals(afterUpdateMap.get(e.getId()).getInternationalFee())));
		return false;
	}
	
	public List<StorageDto> getEntityGallery(String entityType, String entityId)
			throws NotFoundException, InternalServerException {
		if (EntityTypeEnum.COURSE.name().equals(entityType)) {
			return courseProcessor.getCourseGallery(entityId);
		} else if (EntityTypeEnum.INSTITUTE.name().equals(entityType)) {
			return instituteProcessor.getInstituteGallery(entityId);
		}
		return new ArrayList<>();
	}

	public Map<String, FundingResponseDto> getFundingsByFundingNameIds(List<String> fundingNameIds, Boolean validateIds)
			throws NotFoundException, InvokeException {
		log.info("inside getFundingMapByFundingNameIds");
		Map<String, FundingResponseDto> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(fundingNameIds)) {
			List<FundingResponseDto> dtos = eligibilityHandler.getFundingByFundingNameId(fundingNameIds);
			if (!CollectionUtils.isEmpty(dtos)) {
				map = dtos.stream().collect(Collectors.toMap(FundingResponseDto::getFundingNameId, e -> e));
			}
			if (validateIds && map.size() != fundingNameIds.size()) {
				log.error(messageTranslator.toLocale("common.name.notfound",Locale.US));
				throw new NotFoundException(messageTranslator.toLocale("common.name.notfound"));
			}
		}
		return map;
	}

	public Map<String, UserInitialInfoDto> validateAndGetUsersByUserIds(String loginUserID, List<String> userIds)
			throws NotFoundException, InvokeException {
		log.info("inside getFundingMapByFundingNameIds");
		Map<String, UserInitialInfoDto> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(userIds)) {
			List<UserInitialInfoDto> dtos = userHandler.getUserByIds(userIds);
			if (!CollectionUtils.isEmpty(dtos)) {
				map = dtos.stream().collect(Collectors.toMap(UserInitialInfoDto::getUserId, e -> e));
			}
			if (map.size() != userIds.size()) {
				log.error(messageTranslator.toLocale("common.user.notfound",Locale.US));
				throw new NotFoundException(messageTranslator.toLocale("common.user.notfound"));
			}
		}
		return map;
	}

	@Transactional
	public void saveElasticCourses(List<Course> courses) {
		log.info("Calling elastic service to save/update courses on elastic index ");
		if (!CollectionUtils.isEmpty(courses)) {
			List<CourseSyncDTO> courseElasticDtos = courses.stream()
					.map(e -> conversionProcessor.convertToCourseSyncDTOSyncDataEntity(e)).collect(Collectors.toList());
			publishSystemEventHandler.syncCourses(courseElasticDtos);
		}
	}
}
