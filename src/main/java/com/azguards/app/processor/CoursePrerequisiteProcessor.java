package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseDeliveryModes;
import com.azguards.app.bean.CoursePrerequisite;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.CoursePreRequisiteRequestWrapper;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.CoursePreRequisiteDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeNotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoursePrerequisiteProcessor {

	@Autowired
	private CourseProcessor courseProcessor;
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Transactional
	public List<CoursePreRequisiteDto> saveUpdatePreRequisites(String userId, String courseId,
			@Valid CoursePreRequisiteRequestWrapper request)
			throws NotFoundException, ValidationException, InternalServerException {
		log.info("inside CoursePrerequisiteProcessor.saveUpdatePreRequisites");
		List<CoursePreRequisiteDto> coursePreRequisiteDtos = request.getCoursePreRequisiteDtos();
		Course course = courseDao.get(courseId);
		if (!ObjectUtils.isEmpty(course)) {
			List<CoursePrerequisite> coursePrerequisiteBeforeUpdate = course.getCoursePrerequisites().stream().map(deliveryMode -> {
				CoursePrerequisite clone = new CoursePrerequisite();
				BeanUtils.copyProperties(deliveryMode, clone);
				return clone;
			}).collect(Collectors.toList());
			

			log.info("preparing map of exsiting course delivery modes");
			Map<String, CoursePrerequisite> existingCoursePrerequisiteMap = course.getCoursePrerequisites().stream()
					.collect(Collectors.toMap(CoursePrerequisite::getId, e -> e));

			List<CoursePrerequisite> coursePrerequisites = course.getCoursePrerequisites();

			log.info("loop the requested list to collect the entitities to be saved/updated");
			coursePreRequisiteDtos.stream().forEach(e -> {
				CoursePrerequisite coursePrerequisite = new CoursePrerequisite();
				if (!StringUtils.isEmpty(e.getId())) {
					log.info(
							"entityId is present so going to see if it is present in db if yes then we have to update it");
					coursePrerequisite = existingCoursePrerequisiteMap.get(e.getId());
					if (ObjectUtils.isEmpty(coursePrerequisite)) {
						log.error("invalid course Prerequisite id : {}", e.getId());
						throw new RuntimeNotFoundException("invalid course Prerequisite id : " + e.getId());
					}
				}

				coursePrerequisite.setCourse(course);
				coursePrerequisite.setDescription(e.getDescription());
				coursePrerequisite.setAuditFields(userId);
				if (StringUtils.isEmpty(e.getId())) {
					coursePrerequisites.add(coursePrerequisite);
				}
			});
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			coursesToBeSavedOrUpdated = courseDao.saveAll(coursesToBeSavedOrUpdated);
			return coursesToBeSavedOrUpdated.get(0).getCoursePrerequisites().stream().map(e->modelMapper.map(e,CoursePreRequisiteDto.class)).toList();

		} else {
			log.error("invalid course id: {}", courseId);
			throw new NotFoundException("invalid course id: " + courseId);
		}
	}
	
	@Transactional
	public void deleteByPreRequisiteIds(String userId, String courseId, List<String> deliveryModeIds,
			List<String> linkedCourseIds) throws NotFoundException, ValidationException {
		log.info("inside CoursePrerequisiteProcessor.deleteByPreRequisiteIds");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<CoursePrerequisite> coursePrerequisite = course.getCoursePrerequisites();
		if (coursePrerequisite.stream().map(CoursePrerequisite::getId).collect(Collectors.toSet())
				.containsAll(deliveryModeIds)) {
			if (coursePrerequisite.stream().anyMatch(e -> !e.getCreatedBy().equals(userId))) {
				log.error("no access to delete one more course_delivery_modes");
				throw new ForbiddenException("no access to delete one more course_delivery_modes");
			}
			coursePrerequisite.removeIf(e -> Utils.contains(deliveryModeIds, e.getId()));
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			
			courseDao.saveAll(coursesToBeSavedOrUpdated);
			
			log.info("Notify course information changed");
		} else {
			log.error("one or more invalid course_delivery_mode_ids");
			throw new NotFoundException("one or more invalid course_delivery_mode_ids");
		}
	}

	public static boolean contains(List<CourseDeliveryModesDto> lst, CourseDeliveryModes target) {
		return lst.stream().anyMatch(e -> e.getDeliveryType().equalsIgnoreCase(target.getDeliveryType())
				&& e.getStudyMode().equalsIgnoreCase(target.getStudyMode()));
	}
}
