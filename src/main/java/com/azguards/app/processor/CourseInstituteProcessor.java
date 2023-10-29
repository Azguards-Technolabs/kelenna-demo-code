package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseInstitute;
import com.azguards.app.bean.CoursePayment;
import com.azguards.app.bean.CourseScholarship;
import com.azguards.app.bean.Institute;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseInstituteDao;
import com.azguards.app.dao.TimingDao;
import com.azguards.app.dto.CourseLinkedInstituteDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.app.dto.UnLinkInsituteDto;
import com.azguards.app.util.CommonUtil;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeValidationException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseInstituteProcessor {
	@Autowired
	CourseDao courseDao;
	
	@Autowired
	ReadableIdProcessor readableIdProcessor;

	@Autowired
	@Lazy
	CourseProcessor courseProcessor;

	@Autowired
	@Lazy
	InstituteProcessor instituteProcessor;

	@Autowired
	CourseInstituteDao courseInstituteDao;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private TimingDao timingDao;
	
	@Autowired
	PublishSystemEventHandler publishSystemEventHandler;
	
	@Autowired
	ConversionProcessor conversionProcessor;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public void createLinks(String userId, String courseId, List<String> instituteIds)
			throws NotFoundException, ValidationException {
		log.info("Inside CourseInstituteProcessor.createLinks");
		log.info("user: {} is going to create the copy course: {} for the instituteIds: {}", userId, courseId,
				instituteIds);
		List<CourseSyncDTO> syncCourses = new ArrayList<CourseSyncDTO>();
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<Institute> institutes = instituteProcessor.validateAndGetInstituteByIds(instituteIds);
		Set<String> linkedInstituteIds = getLinkedInstituteCourses(courseId).keySet();
		if (instituteIds.stream().anyMatch(e -> e.equals(course.getInstitute().getId()))) {
			log.error(messageTranslator.toLocale("course_institute.same.ids",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course_institute.same.ids"));
		}
		if (!Collections.disjoint(linkedInstituteIds, instituteIds)) {
			log.error(messageTranslator.toLocale("course_institute.already.linked", courseId,Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course_institute.already.linked", courseId));
		}
		List<CourseInstitute> courseInstitutes = new ArrayList<>();
		// TODO: check the access of institutes against userId
		institutes.stream().forEach(e -> {
			Course copiedCourse = modelMapper.map(course, Course.class);
			copiedCourse.setId(null);
			if (!ObjectUtils.isEmpty(copiedCourse.getOffCampusCourse())) {
				copiedCourse.getOffCampusCourse().setId(null);
				copiedCourse.getOffCampusCourse().setCourse(copiedCourse);
			}
			if (!ObjectUtils.isEmpty(copiedCourse.getCoursePayment())) {
				copiedCourse.getCoursePayment().setId(null);
				copiedCourse.getCoursePayment().setCourse(copiedCourse);
				final CoursePayment finalCoursePayment = copiedCourse.getCoursePayment();
				copiedCourse.getCoursePayment().getPaymentItems().stream().forEach(s -> {
					s.setId(null);
					s.setCoursePayment(finalCoursePayment);
				});
			}
			
			copiedCourse.setInstitute(e);
			copiedCourse.setAuditFields(userId);
			final Course finalCopiedCourse = copiedCourse;
			copiedCourse.getCourseDeliveryModes().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			copiedCourse.getCourseEnglishEligibilities().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			if (!ObjectUtils.isEmpty(copiedCourse.getCourseIntake())) {
				copiedCourse.getCourseIntake().setId(null);
				copiedCourse.getCourseIntake().setCourse(copiedCourse);
			}
			copiedCourse.getCourseLanguages().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			copiedCourse.getCourseMinRequirements().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
				d.getCourseMinRequirementSubjects().stream().forEach(s -> {
					s.setId(null);
					s.setCourseMinRequirement(d);
				});
			});
			copiedCourse.getCoursePrerequisites().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			copiedCourse.getCourseCareerOutcomes().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			copiedCourse.getCourseSemesters().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			copiedCourse.getCourseFundings().stream().forEach(d -> {
				d.setId(null);
				d.setCourse(finalCopiedCourse);
			});
			if (!ObjectUtils.isEmpty(copiedCourse.getCourseScholarship())) {
				copiedCourse.getCourseScholarship().setId(null);
				copiedCourse.getCourseScholarship().setCourse(copiedCourse);
				final CourseScholarship finalCourseScholarship = copiedCourse.getCourseScholarship();
				copiedCourse.getCourseScholarship().getScholarshipItems().stream().forEach(s -> {
					s.setId(null);
					s.setCourseScholarship(finalCourseScholarship);
				});
			}
			try {
				readableIdProcessor.setReadableIdForCourse(copiedCourse);
				copiedCourse = courseDao.addUpdateCourse(copiedCourse);
				syncCourses.add(conversionProcessor.convertToCourseSyncDTOSyncDataEntity(copiedCourse));
			} catch (ValidationException e1) {
				log.error(
						messageTranslator.toLocale("course_institute.managing.same",Locale.US));
				throw new RuntimeValidationException(
						messageTranslator.toLocale("course_institute.managing.same"));
			}

			CourseInstitute courseInstitute = new CourseInstitute();
			courseInstitute.setAuditFields(userId);
			courseInstitute.setSourceCourse(course);
			courseInstitute.setSourceInstitute(course.getInstitute());
			CourseInstitute existingSecondaryCourse = courseInstituteDao.findByDestinationCourseId(courseId);
			if (!ObjectUtils.isEmpty(existingSecondaryCourse)) {
				courseInstitute.setSourceCourse(existingSecondaryCourse.getSourceCourse());
				courseInstitute.setSourceInstitute(existingSecondaryCourse.getSourceInstitute());
			}
			courseInstitute.setDestinationCourse(copiedCourse);
			courseInstitute.setDestinationInstitute(e);
			courseInstitutes.add(courseInstitute);
		});
		courseInstituteDao.saveAll(courseInstitutes);
		publishSystemEventHandler.syncCourses(syncCourses);
	}

	@Transactional(readOnly = true)
	public List<CourseLinkedInstituteDto> getLinkedInstitutes(String userId, String courseId) throws Exception {
		log.info("Inside CourseInstituteProcessor.getLinkedInstitutes for courseId: {}", courseId);
		log.info("userId: {}, courseId: {}", userId, courseId);
		List<CourseLinkedInstituteDto> linkedInstituteResponse = new ArrayList<>();
		Map<String, Course> linkedInstituteCourses = getLinkedInstituteCourses(courseId);
		if (!linkedInstituteCourses.isEmpty()) {
			List<InstituteResponseDto> instituteResponseDtos = instituteProcessor
					.getInstitutesByIdList(linkedInstituteCourses.keySet().stream().collect(Collectors.toList()));
			if (!CollectionUtils.isEmpty(instituteResponseDtos)) {
				instituteResponseDtos.stream().forEach(e -> {
					CourseLinkedInstituteDto dto = new CourseLinkedInstituteDto();
					Course course = linkedInstituteCourses.get(e.getId());
					if (course.getCreatedBy().equals(userId)) {
						dto.setHasCourseEditAccess(true);
					}
					dto.setCourseId(course.getId());
					dto.setInstitute(e);
					linkedInstituteResponse.add(dto);
				});
			}
		}
		return linkedInstituteResponse;
	}

	private Map<String, Course> getLinkedInstituteCourses(String courseId) throws NotFoundException {
		log.info("inside CourseInstituteProcessor.getLinkedInstituteIds");
		log.info("courseId: {}", courseId);
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<CourseInstitute> courseInstitutes = courseInstituteDao.findLinkedInstitutes(courseId);
		Map<String, Course> linkedInstitutesMap = new HashMap<>();
		courseInstitutes.stream().forEach(e -> {
			linkedInstitutesMap.put(e.getSourceInstitute().getId(), e.getDestinationCourse());
			linkedInstitutesMap.put(e.getDestinationInstitute().getId(), e.getSourceCourse());
		});
		linkedInstitutesMap.remove(course.getInstitute().getId());
		return linkedInstitutesMap;
	}

	@Transactional
	public void unLinkInstitutes(String userId, String courseId, @Valid List<UnLinkInsituteDto> request)
			throws NotFoundException {
		log.info("inside CourseInstituteProcessor.getLinkedInstituteIds");
		log.info("PARAMETERS ====> userId: {} ,courseId: {}, request: {}", userId, courseId, request);
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		CommonUtil.validateEditAccess(userId, course);
		Map<String, Boolean> deleteCourseRequestMap = request.stream()
				.collect(Collectors.toMap(UnLinkInsituteDto::getCourseId, UnLinkInsituteDto::isDeleteCourse));
		Map<String, Course> requestCourses = courseProcessor
				.validateAndGetCourseByIds(deleteCourseRequestMap.keySet().stream().collect(Collectors.toList()))
				.stream().collect(Collectors.toMap(Course::getId, e -> e));
		List<CourseInstitute> linkedInstitutes = courseInstituteDao.findLinkedInstitutes(courseId);

		List<CourseInstitute> linkedCoursesTobeDeleted = new ArrayList<>();
		List<Course> coursesTobeDeleted = new ArrayList<>();
		request.stream().forEach(unLinkInstituteDto -> {
			Course requestedCourse = requestCourses.get(unLinkInstituteDto.getCourseId());
			if (deleteCourseRequestMap.get(requestedCourse.getId()).booleanValue()) {
				CommonUtil.validateEditAccess(userId, requestedCourse);
			}
			log.info("process to delete (un-link) the requested course : {}", unLinkInstituteDto.getCourseId());
			if (CollectionUtils.isEmpty(linkedInstitutes)) {
				log.error("CourseId: {} is not lnked with the cureent course", unLinkInstituteDto.getCourseId());
				throw new RuntimeValidationException(
						"CourseId: " + unLinkInstituteDto.getCourseId() + " is not lnked with the cureent course");
			} else {
				log.debug("going to see if course to be unLinked is linked as primart or secondary course");
				Optional<CourseInstitute> secondaryCourse = linkedInstitutes.stream()
						.filter(e -> e.getDestinationCourse().getId().equals(unLinkInstituteDto.getCourseId()))
						.findAny();
				CourseInstitute toBeDeleted = null;
				if (secondaryCourse.isPresent()) {
					log.debug("course is secondary course");
					toBeDeleted = secondaryCourse.get();
				} else {
					log.debug("course is primary course");
					Course firstSecondayCourse = linkedInstitutes.get(0).getDestinationCourse();
					linkedInstitutes.stream().forEach(e -> {
						e.setSourceCourse(firstSecondayCourse);
						e.setSourceInstitute(firstSecondayCourse.getInstitute());
					});
					toBeDeleted = linkedInstitutes.get(0);
				}
				linkedCoursesTobeDeleted.add(toBeDeleted);
				if (deleteCourseRequestMap.get(unLinkInstituteDto.getCourseId()).booleanValue()) {
					coursesTobeDeleted.add(requestedCourse);
				}
			}
		});
		if (!CollectionUtils.isEmpty(linkedCoursesTobeDeleted)) {
			courseInstituteDao.deleteAll(linkedCoursesTobeDeleted);
		}
		if (!CollectionUtils.isEmpty(coursesTobeDeleted)) {
			timingDao.deleteByEntityTypeAndEntityIdIn(EntityTypeEnum.COURSE,
					coursesTobeDeleted.stream().map(Course::getId).collect(Collectors.toList()));
			courseDao.deleteAll(coursesTobeDeleted);
		}
	}

	@Transactional
	public void removeAllLinksWithInstitutes(Course course) {
		log.info("inside CourseInstiuteProcessor.removeAllLinksWithInstitutes");
		if (!ObjectUtils.isEmpty(course)) {
			List<CourseInstitute> linkedInstitutes = courseInstituteDao.findLinkedInstitutes(course.getId());
			if (!CollectionUtils.isEmpty(linkedInstitutes)) {
				log.debug("going to see if course to be unLinked is linked as primart or secondary course");
				Optional<CourseInstitute> secondaryCourse = linkedInstitutes.stream()
						.filter(e -> e.getDestinationCourse().getId().equals(course.getId())).findAny();
				CourseInstitute toBeDeleted = null;
				if (secondaryCourse.isPresent()) {
					log.debug("course is secondary course");
					toBeDeleted = secondaryCourse.get();
				} else {
					log.debug("course is primary course");
					Course firstSecondayCourse = linkedInstitutes.get(0).getDestinationCourse();
					linkedInstitutes.stream().forEach(e -> {
						e.setSourceCourse(firstSecondayCourse);
						e.setSourceInstitute(firstSecondayCourse.getInstitute());
					});
					toBeDeleted = linkedInstitutes.get(0);
				}
				courseInstituteDao.deleteAll(Arrays.asList(toBeDeleted));
			}
		}
	}
}
