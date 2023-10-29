package com.azguards.app.processor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CoursePayment;
import com.azguards.app.bean.CoursePaymentItem;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CoursePaymentDao;
import com.azguards.common.lib.dto.institute.CoursePaymentDto;
import com.azguards.common.lib.dto.institute.CoursePaymentItemDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InternalServerException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CoursePaymentProcessor {
	@Autowired
	private CoursePaymentDao coursePaymentDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;
	
	@Autowired
	private CommonProcessor commonProcessor;
	
	@Autowired
	private ConversionProcessor conversionProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void saveUpdateCoursePayment(String userId, String courseId, @Valid CoursePaymentDto coursePaymentDto)
			throws ForbiddenException, NotFoundException, ValidationException {
		log.info("inside CoursePaymentProcessor.saveUpdateCoursePayment");
		Course course = getCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("course_payment.add.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("course_payment.add.no.access"));
		} else {
			CoursePayment coursePayment = course.getCoursePayment();
			if (ObjectUtils.isEmpty(coursePayment)) {
				coursePayment = new CoursePayment();
			}
			
			List<CoursePaymentItemDto> paymentItemDtos = coursePaymentDto.getPaymentItems();
			List<CoursePaymentItem> dbCoursePaymentItems = coursePayment.getPaymentItems();

			CoursePayment coursePaymentBeforeUpdate = new CoursePayment();
			BeanUtils.copyProperties(coursePayment, coursePaymentBeforeUpdate);
			List<CoursePaymentItem> coursePaymentItemsBeforeUpdate = dbCoursePaymentItems.stream().map(item -> {
				CoursePaymentItem clone = new CoursePaymentItem();
				BeanUtils.copyProperties(item, clone);
				return clone;
			}).collect(Collectors.toList());
			
			log.info("remove the payment items not present in request");
			dbCoursePaymentItems.removeIf(e -> paymentItemDtos.stream().noneMatch(t -> e.getName().equalsIgnoreCase(t.getName())));
			final CoursePayment finalCoursePayment = coursePayment;
			paymentItemDtos.stream().forEach(e -> {
				Optional<CoursePaymentItem> existingCoursePaymentItemOp = dbCoursePaymentItems.stream()
						.filter(t -> e.getName().equalsIgnoreCase(t.getName())).findAny();
				CoursePaymentItem coursePaymentItem = new CoursePaymentItem();
				String existingId = null;
				if (existingCoursePaymentItemOp.isPresent()) {
					log.info("payment item already present so going to update");
					coursePaymentItem = existingCoursePaymentItemOp.get();
					existingId = coursePaymentItem.getId();
				}
				BeanUtils.copyProperties(e, coursePaymentItem);
				coursePaymentItem.setId(existingId);
				coursePaymentItem.setCoursePayment(finalCoursePayment);
				coursePaymentItem.setAuditFields(userId);
				if (StringUtils.isEmpty(coursePaymentItem.getId())) {
					dbCoursePaymentItems.add(coursePaymentItem);
				}
			});
			coursePayment.setDescription(coursePaymentDto.getDescription());
			coursePayment.setCourse(course);
			coursePayment.setAuditFields(userId);
			log.info("going to save record in db");
			coursePaymentDao.save(coursePayment);
			
			if(! (coursePaymentBeforeUpdate.equals(coursePayment) &&
					coursePaymentItemsBeforeUpdate.equals(coursePayment.getPaymentItems()))) {
				commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", Arrays.asList(course));
			}
			
			log.info("Calling elastic service to save/update course on elastic index having courseId: ", courseId);
			publishSystemEventHandler.syncCourses(Arrays.asList(conversionProcessor.convertToCourseSyncDTOSyncDataEntity(course)));
		}
	}

	@Transactional
	public void deleteCoursePayment(String userId, String courseId)
			throws ForbiddenException, NotFoundException, InternalServerException {
		Course course = getCourseById(courseId);
		if (!course.getCreatedBy().equals(userId)) {
			log.error(messageTranslator.toLocale("course_payment.delete.no.access",Locale.US));
			throw new ForbiddenException(messageTranslator.toLocale("course_payment.delete.no.access"));
		}
		CoursePayment coursePayment = course.getCoursePayment();
		if (!ObjectUtils.isEmpty(coursePayment)) {
			course.setCoursePayment(null);
			try {
				courseDao.addUpdateCourse(course);
				
				log.info("Send notification for course content updates");
				commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", Arrays.asList(course));
				
				log.info("Calling elastic service to save/update course on elastic index having courseId: ", courseId);
				publishSystemEventHandler.syncCourses(Arrays.asList(conversionProcessor.convertToCourseSyncDTOSyncDataEntity(course)));
			} catch (ValidationException e) {
				log.error(messageTranslator.toLocale("course_payment.delete.error",Locale.US));
				throw new InternalServerException(messageTranslator.toLocale("course_payment.delete.error"));
			}
		} else {
			log.error(messageTranslator.toLocale("course_payment.notfound",Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_payment.notfound"));
		}
	}

	private Course getCourseById(String courseId) throws NotFoundException {
		Course course = courseDao.get(courseId);
		if (ObjectUtils.isEmpty(course)) {
			log.error(messageTranslator.toLocale("course_payment.course.id.invalid",courseId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("course_payment.course.id.invalid",courseId));
		}
		return course;
	}
}
