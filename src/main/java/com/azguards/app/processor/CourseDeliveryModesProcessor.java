package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseDeliveryModeFunding;
import com.azguards.app.bean.CourseDeliveryModes;
import com.azguards.app.bean.CourseFees;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseDeliveryModesDao;
import com.azguards.app.dto.CourseDeliveryModeRequestWrapper;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.ValidList;
import com.azguards.common.lib.dto.institute.CourseDeliveryModeFundingDto;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.CourseFeesDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeNotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CourseDeliveryModesProcessor {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	@Lazy
	private CourseProcessor courseProcessor;

	@Autowired
	private MessageTranslator messageTranslator;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CourseDeliveryModesDao courseDeliveryModesDao;

	@Autowired
	private CommonProcessor commonProcessor;

	public List<CourseDeliveryModesDto> getCourseDeliveryModesByCourseId(String courseId) {
		log.debug("Inside getCourseDeliveryModesByCourseId() method");
		List<CourseDeliveryModesDto> courseDeliveryModesResponse = new ArrayList<>();
		log.info("Fetching courseDeliveryModes from DB for courseId = " + courseId);
		List<CourseDeliveryModes> courseDeliveryModesFromDB = courseDeliveryModesDao
				.getCourseDeliveryModesByCourseId(courseId);
		if (!CollectionUtils.isEmpty(courseDeliveryModesFromDB)) {
			log.info("Additional Info is not null, then start iterating list");
			courseDeliveryModesFromDB.stream().forEach(courseDeliveryMode -> {
				CourseDeliveryModesDto courseDeliveryModesDto = new CourseDeliveryModesDto();
				log.info("Copying Bean class values to DTO class using beanUtils");
				BeanUtils.copyProperties(courseDeliveryMode, courseDeliveryModesDto);
				courseDeliveryModesDto.setCourseId(courseId);
				courseDeliveryModesResponse.add(courseDeliveryModesDto);
			});
		}
		return courseDeliveryModesResponse;
	}

	@Transactional
	public void saveUpdateCourseDeliveryModes(String userId, String courseId,
			@Valid CourseDeliveryModeRequestWrapper request)
			throws NotFoundException, ValidationException, InternalServerException {
		log.info("inside CourseDeliveryModesProcessor.saveUpdateCourseDeliveryModes");
		List<CourseDeliveryModesDto> courseDeliveryModeDtos = request.getCourseDelieveryModeDtos();
		if (courseDao.existsById(courseId)) {
			Course course = courseDao.get(courseId);
			List<CourseDeliveryModes> courseDeliveryModesBeforeUpdate = course.getCourseDeliveryModes().stream()
					.map(deliveryMode -> {
						CourseDeliveryModes clone = new CourseDeliveryModes();
						BeanUtils.copyProperties(deliveryMode, clone);
						return clone;
					}).collect(Collectors.toList());

			log.info("preparing map of exsiting course delivery modes");
			Map<String, CourseDeliveryModes> existingCourseDeliveryModesMap = course.getCourseDeliveryModes().stream()
					.collect(Collectors.toMap(CourseDeliveryModes::getId, e -> e));
			List<CourseDeliveryModes> courseDeliveryModes = course.getCourseDeliveryModes();

			log.info("loop the requested list to collect the entitities to be saved/updated");
			courseDeliveryModeDtos.stream().forEach(e -> {
				CourseDeliveryModes courseDeliveryMode = new CourseDeliveryModes();
				if (StringUtils.hasText(e.getId())) {
					log.info(
							"entityId is present so going to see if it is present in db if yes then we have to update it");
					courseDeliveryMode = existingCourseDeliveryModesMap.get(e.getId());
					if (ObjectUtils.isEmpty(courseDeliveryMode)) {
						log.error("invalid course delivery mode id : {}", e.getId());
						throw new RuntimeNotFoundException("invalid course delivery mode id : " + e.getId());
					}
				}else {
					courseDeliveryModes.add(courseDeliveryMode);
				}
				courseDeliveryMode.setCourse(course);
				log.info("Adding additional infos like deliveryType, studyMode etc");
				courseDeliveryMode.setDeliveryType(e.getDeliveryType());
				courseDeliveryMode.setStudyMode(e.getStudyMode());
				courseDeliveryMode.setDuration(e.getDuration());
				courseDeliveryMode.setDurationTime(e.getDurationTime());
				courseDeliveryMode.setAccessibility(e.getAccessibility());
				courseDeliveryMode.setIsGovernmentEligible(e.getIsGovernmentEligible());
				courseDeliveryMode.setCourse(course);
				courseDeliveryMode.setAuditFields(userId);
				saveUpdateCourseFees(userId, courseDeliveryMode, e.getFees());
				saveUpdateCourseDeliveryModeFunding(userId, courseDeliveryMode, e.getFundings());
				
			});
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			if (!CollectionUtils.isEmpty(request.getLinkedCourseIds())) {
				List<CourseDeliveryModesDto> dtosToReplicate = courseDeliveryModes.stream()
						.map(e -> modelMapper.map(e, CourseDeliveryModesDto.class)).collect(Collectors.toList());
				coursesToBeSavedOrUpdated
						.addAll(replicateCourseDeliveryModes(userId, request.getLinkedCourseIds(), dtosToReplicate));
			}

			courseDao.saveAll(coursesToBeSavedOrUpdated);

			if (!courseDeliveryModesBeforeUpdate.equals(courseDeliveryModes)) {
				log.info("Notify course information changed");
				final String notificationType = commonProcessor.checkIfPriceChanged(courseDeliveryModesBeforeUpdate,
						courseDeliveryModes) ? "COURSE_PRICE_CHANGED" : "COURSE_CONTENT_UPDATED";
				commonProcessor.notifyCourseUpdates(notificationType, coursesToBeSavedOrUpdated);
			}

			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else if (courseDao.documentExistsById(courseId)) {
			CourseRequest course = courseDao.findDocumentById(courseId).get();
			courseDeliveryModeDtos.stream().forEach(dto -> {
				if (!StringUtils.hasText(dto.getId())) {
					dto.setId(Utils.generateUUID());
				}
				dto.getFundings().stream().forEach(e -> {
					if (!StringUtils.hasText(e.getId())) {
						e.setId(Utils.generateUUID());
					}
				});

				dto.getFees().stream().forEach(e -> {
					if (!StringUtils.hasText(e.getId())) {
						e.setId(Utils.generateUUID());
					}
				});
			});
			course.setCourseDeliveryModes(new ValidList<>(courseDeliveryModeDtos));
			courseDao.saveDocument(course);
		}else {
			log.error(messageTranslator.toLocale("course.id.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course.id.invalid"));
		}
	}

	@Transactional
	public void deleteByCourseDeliveryModeIds(String userId, String courseId, List<String> deliveryModeIds,
			List<String> linkedCourseIds) throws NotFoundException, ValidationException {
		log.info("inside CourseDeliveryModesProcessor.deleteByCourseDeliveryModeIds");
		if(courseDao.existsById(courseId)) {
			Course course = courseDao.get(courseId);
			List<CourseDeliveryModes> courseDeliveryModes = course.getCourseDeliveryModes();
			if (courseDeliveryModes.stream().map(CourseDeliveryModes::getId).collect(Collectors.toSet())
					.containsAll(deliveryModeIds)) {
				if (courseDeliveryModes.stream().anyMatch(e -> !e.getCreatedBy().equals(userId))) {
					log.error("no access to delete one more course_delivery_modes");
					throw new ForbiddenException("no access to delete one more course_delivery_modes");
				}
				courseDeliveryModes.removeIf(e -> Utils.contains(deliveryModeIds, e.getId()));
				List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
				coursesToBeSavedOrUpdated.add(course);
				if (!CollectionUtils.isEmpty(linkedCourseIds)) {
					List<CourseDeliveryModesDto> dtosToReplicate = courseDeliveryModes.stream()
							.map(e -> modelMapper.map(e, CourseDeliveryModesDto.class)).collect(Collectors.toList());
					coursesToBeSavedOrUpdated
							.addAll(replicateCourseDeliveryModes(userId, linkedCourseIds, dtosToReplicate));
				}
	
				courseDao.saveAll(coursesToBeSavedOrUpdated);
	
				log.info("Notify course information changed");
				commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);
	
				commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
			} else {
				log.error("one or more invalid course_delivery_mode_ids");
				throw new NotFoundException("one or more invalid course_delivery_mode_ids");
			}
		} else if (courseDao.documentExistsById(courseId)) {
			CourseRequest course = courseDao.findDocumentById(courseId).get();
			if(!CollectionUtils.isEmpty(course.getCourseDeliveryModes())){
				course.getCourseDeliveryModes().removeIf(e->Utils.contains(deliveryModeIds, e.getId()));
				courseDao.saveDocument(course);
			}
		} else {
			log.error(messageTranslator.toLocale("course.id.invalid",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course.id.invalid"));
		}
	}

	private List<Course> replicateCourseDeliveryModes(String userId, List<String> courseIds,
			List<CourseDeliveryModesDto> courseDeliveryModeDtos) throws ValidationException, NotFoundException {
		log.info("inside courseProcessor.replicateCourseDeliveryModes");
		if (!CollectionUtils.isEmpty(courseIds)) {
			List<Course> courses = courseProcessor.validateAndGetCourseByIds(courseIds);
			courses.stream().forEach(course -> {
				List<CourseDeliveryModes> courseDeliveryModes = course.getCourseDeliveryModes();
				if (CollectionUtils.isEmpty(courseDeliveryModeDtos)) {
					courseDeliveryModes.clear();
				} else {
					courseDeliveryModes.removeIf(e -> !contains(courseDeliveryModeDtos, e));
					courseDeliveryModeDtos.stream().forEach(dto -> {
						Optional<CourseDeliveryModes> existingCourseDeliveryModeOp = courseDeliveryModes.stream()
								.filter(e -> e.getDeliveryType().equalsIgnoreCase(dto.getDeliveryType())
										&& e.getStudyMode().equalsIgnoreCase(dto.getStudyMode()))
								.findAny();
						CourseDeliveryModes courseDeliveryMode = new CourseDeliveryModes();
						if (existingCourseDeliveryModeOp.isPresent()) {
							courseDeliveryMode = existingCourseDeliveryModeOp.get();
						}
						courseDeliveryMode.setCourse(course);
						log.info("Adding additional infos like deliveryType, studyMode etc");

						courseDeliveryMode.setDeliveryType(dto.getDeliveryType());
						courseDeliveryMode.setStudyMode(dto.getStudyMode());
						courseDeliveryMode.setDuration(dto.getDuration());
						courseDeliveryMode.setDurationTime(dto.getDurationTime());
						courseDeliveryMode.setCourse(course);
						if (!StringUtils.hasText(courseDeliveryMode.getId())) {
							courseDeliveryModes.add(courseDeliveryMode);
						}
						courseDeliveryMode.setAuditFields(userId);
					});
				}
			});
			return courses;
		}
		return new ArrayList<>();
	}

	public static boolean contains(List<CourseDeliveryModesDto> lst, CourseDeliveryModes target) {
		return lst.stream().anyMatch(t -> t.getDeliveryType().equalsIgnoreCase(target.getDeliveryType())
				&& t.getStudyMode().equalsIgnoreCase(target.getStudyMode())
				&& t.getIsGovernmentEligible().equals(target.getIsGovernmentEligible())
				&& t.getAccessibility().equalsIgnoreCase(target.getAccessibility())
				&& t.getDurationTime().equalsIgnoreCase(target.getDurationTime())
				&& t.getDuration().equals(target.getDuration()));
	}

	public void saveUpdateCourseFees(String userId, CourseDeliveryModes deliveryMode, List<CourseFeesDto> feesDtos) {
		if (!CollectionUtils.isEmpty(feesDtos)) {
			List<CourseFees> fees = deliveryMode.getFees();

			List<String> updateRequestIds = feesDtos.stream().filter(e -> StringUtils.hasText(e.getId()))
			.map(CourseFeesDto::getId).collect(Collectors.toList());
			fees.removeIf(e -> !Utils.contains(updateRequestIds, e.getId()));
			
			log.info("preparing map of exsiting course fees");
			Map<String, CourseFees> existingFeesMap = fees.stream().filter(e -> StringUtils.hasText(e.getId()))
					.collect(Collectors.toMap(CourseFees::getId, e -> e));
			feesDtos.stream().forEach(dto->{
				CourseFees model = new CourseFees();
				if (StringUtils.hasText(dto.getId())) {
					log.info(
							"id is present so going to see if it is present in db if yes then we have to update it");
					model = existingFeesMap.get(dto.getId());
					if (ObjectUtils.isEmpty(model)) {
						log.error("invalid course fees id : {}", dto.getId());
						throw new RuntimeNotFoundException("invalid course fees id : " + dto.getId());
					}
				}else {
					fees.add(model);
				}
				model.setName(dto.getName());
				model.setCurrency(dto.getCurrency());
				model.setAmount(dto.getAmount());
				model.setCourseDeliveryMode(deliveryMode);
				model.setAuditFields(userId);
			});
			
		} else {
			deliveryMode.getFees().clear();
		}
	}

	public void saveUpdateCourseDeliveryModeFunding(String userId, CourseDeliveryModes deliveryMode, List<CourseDeliveryModeFundingDto> deliveryModeFundingDtos) {
		if (!CollectionUtils.isEmpty(deliveryModeFundingDtos)) {
			List<CourseDeliveryModeFunding> fundings = deliveryMode.getFundings();
			
			
			List<String> updateRequestIds = deliveryModeFundingDtos.stream().filter(e -> StringUtils.hasText(e.getId()))
					.map(CourseDeliveryModeFundingDto::getId).collect(Collectors.toList());
			fundings.removeIf(e -> !Utils.contains(updateRequestIds, e.getId()));
					
			
			log.info("preparing map of exsiting course fundings");
			Map<String, CourseDeliveryModeFunding> existingFundingMap = fundings.stream().filter(e -> StringUtils.hasText(e.getId()))
					.collect(Collectors.toMap(CourseDeliveryModeFunding::getId, e -> e));
			
			deliveryModeFundingDtos.stream().forEach(dto->{
				CourseDeliveryModeFunding model = new CourseDeliveryModeFunding();
				if (StringUtils.hasText(dto.getId())) {
					log.info(
							"id is present so going to see if it is present in db if yes then we have to update it");
					model = existingFundingMap.get(dto.getId());
					if (ObjectUtils.isEmpty(model)) {
						log.error("invalid course funding id : {}", dto.getId());
						throw new RuntimeNotFoundException("invalid course funding id : " + dto.getId());
					}
				}else {
					fundings.add(model);
				}
				model.setName(dto.getName());
				model.setFundingNameId(dto.getFundingNameId());
				model.setCurrency(dto.getCurrency());
				model.setAmount(dto.getAmount());
				model.setCourseDeliveryMode(deliveryMode);
				model.setAuditFields(userId);
			});
			
		} else {
			deliveryMode.getFundings().clear();
		}
	}
}
