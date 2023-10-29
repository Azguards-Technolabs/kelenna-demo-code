package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.bean.Course;
import com.azguards.app.service.UserRecommendationService;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("userRecommendationControllerV1")
@RequestMapping("/api/v1/user")
public class UserRecommendationController {

	@Autowired
	private UserRecommendationService userRecommendationService;
	@Autowired
	private MessageTranslator messageTranslator;
	@GetMapping(value = "/recommend/course/{courseId}")
	public ResponseEntity<?> getUserRecommendCourse(@PathVariable final String courseId, @RequestParam final String userId) throws Exception {
		List<Course> recommendCourses = userRecommendationService.getRecommendCourse(courseId, userId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(recommendCourses).setMessage(messageTranslator.toLocale("user_recommendation.list.retrieved"))
				.create();
	}

	@GetMapping(value = "/related/course/{courseId}")
	public ResponseEntity<?> getRelatedCourse(@PathVariable final String courseId) throws Exception {
		List<Course> recommendCourses = userRecommendationService.getRelatedCourse(courseId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(recommendCourses).setMessage(messageTranslator.toLocale("user_recommendation.related.list.retrieved")).create();
	}
}
