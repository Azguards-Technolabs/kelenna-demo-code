package com.azguards.app.entitylistener;

import java.util.Arrays;

import javax.persistence.PostUpdate;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.OffCampusCourse;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.processor.CommonProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class CourseUpdateListener {
	
	private static CommonProcessor commonProcessor;
	
	@Autowired
	private void setCommonProcessor(CommonProcessor commonProcessor) {
		CourseUpdateListener.commonProcessor = commonProcessor;
	}
	
	public static void putCourseInTransaction(String key, Course value) {
		RequestContextHolder.getRequestAttributes().setAttribute(key, value, RequestAttributes.SCOPE_REQUEST);  
	}
			
	@PostUpdate
	public void afterCourseUpdate(Course course) {
		log.info("after course update");
		Course courseBeforeUpdate = (Course)RequestContextHolder.getRequestAttributes().getAttribute(course.getId(), RequestAttributes.SCOPE_REQUEST);
		if(!ObjectUtils.isEmpty(courseBeforeUpdate)) {
			
			log.info("Course updated {}", !courseBeforeUpdate.equals(course));
			if(!(courseBeforeUpdate.equals(course)
					&& !ObjectUtils.isEmpty(courseBeforeUpdate.getCourseIntake()) && courseBeforeUpdate.getCourseIntake().equals(course.getCourseIntake())
					&& courseBeforeUpdate.getCourseLanguages().equals(course.getCourseLanguages())
					&& courseBeforeUpdate.getCourseDeliveryModes().equals(course.getCourseDeliveryModes())
					&& compareOffCampusCourse(courseBeforeUpdate.getOffCampusCourse(), course.getOffCampusCourse())
					&& courseBeforeUpdate.getCourseFundings().equals(course.getCourseFundings()))) {
				log.info("Course updated sending notifications to the users whose favorite course is {}",course.getId());
				String notificationType = "COURSE_CONTENT_UPDATED";
				if(!courseBeforeUpdate.getCourseDeliveryModes().equals(course.getCourseDeliveryModes())) {
					log.info("Notify course information changed");
					notificationType = commonProcessor.checkIfPriceChanged(courseBeforeUpdate.getCourseDeliveryModes(), course.getCourseDeliveryModes()) ? "COURSE_PRICE_CHANGED" : "COURSE_CONTENT_UPDATED";
				} else if(!compareOffCampusCourse(courseBeforeUpdate.getOffCampusCourse(), course.getOffCampusCourse())){
					if(!( StringUtils.hasText(courseBeforeUpdate.getOffCampusCourse().getAddress()) && courseBeforeUpdate.getOffCampusCourse().getAddress().equals(course.getOffCampusCourse().getAddress())
							&& courseBeforeUpdate.getOffCampusCourse().getCityName().equals(course.getOffCampusCourse().getCityName())
							&& courseBeforeUpdate.getOffCampusCourse().getCountryName().equals(course.getOffCampusCourse().getCountryName())
							&& courseBeforeUpdate.getOffCampusCourse().getStateName().equals(course.getOffCampusCourse().getStateName())
							&& courseBeforeUpdate.getOffCampusCourse().getPostalCode().equals(course.getOffCampusCourse().getPostalCode())
							&& courseBeforeUpdate.getOffCampusCourse().getLatitude().equals(course.getOffCampusCourse().getLongitude()))){
						notificationType = "COURSE_VENUE_CHANGED";
					}
				}
				commonProcessor.notifyCourseUpdates(notificationType, Arrays.asList(course));
			}
		}
	}
	
	private boolean compareOffCampusCourse(OffCampusCourse obj1, OffCampusCourse obj2) {
		return (obj1.equals(obj2) && Long.compare(obj1.getStartDate().getTime(),obj2.getStartDate().getTime()) == 0 && Long.compare(obj1.getEndDate().getTime(),obj2.getEndDate().getTime())==0);
	}
	
	@After("execution(* com.azguards.app.controller.v1.CourseController.*(..)) && args(loggedInUserId,instituteId,courseDto,id,..)")
	public void afterCourseUpdate(String loggedInUserId, String instituteId, CourseRequest courseDto, String id) {
		log.info("removing course cache loggedInUserId {} instituteId {} courseId {}",loggedInUserId,instituteId,id);
		RequestContextHolder.getRequestAttributes().removeAttribute(id, RequestAttributes.SCOPE_REQUEST);		
	}

}
