package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseSemester;
import com.azguards.app.bean.SemesterSubject;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dto.CourseSemesterRequestWrapper;
import com.azguards.common.lib.dto.institute.CourseSemesterDto;
import com.azguards.common.lib.dto.institute.SemesterSubjectDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.RuntimeNotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseSemesterProcessor {

	@Autowired
	InstituteDao instituteDao;

	@Autowired
	CourseDao courseDao;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CommonProcessor commonProcessor;

	@Transactional
	public void saveUpdateCourseSemesters(String userId, String courseId, CourseSemesterRequestWrapper request)
			throws NotFoundException, ValidationException {
		log.info("inside CourseSubjectProcessor.saveUpdateCourseSemesters");
		List<CourseSemesterDto> courseSubjectDtos = request.getCourseSemesterDtos();
		Course course = courseDao.get(courseId);
		if (!ObjectUtils.isEmpty(course)) {

			log.info("preparing map of exsiting course subjects");
			Map<String, CourseSemester> existingCourseSubjectsMap = course.getCourseSemesters().stream()
					.collect(Collectors.toMap(CourseSemester::getId, e -> e));

			List<CourseSemester> courseSubjects = course.getCourseSemesters();
			log.info("loop the requested list to collect the entitities to be saved/updated");
			courseSubjectDtos.stream().forEach(e -> {
				CourseSemester courseSubject = new CourseSemester();
				if (!StringUtils.isEmpty(e.getId())) {
					log.info(
							"entityId is present so going to see if it is present in db if yes then we have to update it");
					courseSubject = existingCourseSubjectsMap.get(e.getId());
					if (courseSubject == null) {
						log.error("invalid course subject id : {}", e.getId());
						throw new RuntimeNotFoundException("invalid course subject id : " + e.getId());
					}
				}
				BeanUtils.copyProperties(e, courseSubject);
				courseSubject.setCourse(course);
				courseSubject.setAuditFields(userId);
				if (StringUtils.isEmpty(e.getId())) {
					courseSubjects.add(courseSubject);
				}
				saveUpdateSubjects(userId, courseSubject, e.getSubjects());
			});
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			courseDao.saveAll(coursesToBeSavedOrUpdated);
			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error("invalid course id: {}", courseId);
			throw new NotFoundException("invalid course id: " + courseId);
		}
	}

	@Transactional
	public void deleteByCourseSemesterIds(String userId, String courseId, List<String> courseSubjectIds,
			List<String> linkedCourseIds) throws NotFoundException, ValidationException {
		log.info("inside CourseSubjectProcessor.deleteByCourseSubjectIds");
		Course course = courseProcessor.validateAndGetCourseById(courseId);
		List<CourseSemester> courseSubjects = course.getCourseSemesters();
		if (courseSubjects.stream().map(CourseSemester::getId).collect(Collectors.toSet())
				.containsAll(courseSubjectIds)) {
			if (courseSubjects.stream().anyMatch(e -> !e.getCreatedBy().equals(userId))) {
				log.error("no access to delete one more subjects");
				throw new ForbiddenException("no access to delete one more subjects");
			}
			courseSubjects.removeIf(e -> Utils.contains(courseSubjectIds, e.getId()));
			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
			courseDao.saveAll(coursesToBeSavedOrUpdated);
			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error("one or more invalid course_subject_ids");
			throw new NotFoundException("one or more invalid course_subject_ids");
		}
	}
	
	public void saveUpdateSubjects(String userId, CourseSemester courseSemester, List<SemesterSubjectDto> subjectDtos) {
		if (!CollectionUtils.isEmpty(subjectDtos)) {
			List<SemesterSubject> subjects = courseSemester.getSubjects();

			List<String> updateRequestIds = subjectDtos.stream().filter(e -> StringUtils.hasText(e.getId()))
			.map(SemesterSubjectDto::getId).collect(Collectors.toList());
			subjects.removeIf(e -> !Utils.contains(updateRequestIds, e.getId()));
			
			log.info("preparing map of exsiting course fees");
			Map<String, SemesterSubject> existingSubjectMap = subjects.stream().filter(e -> StringUtils.hasText(e.getId()))
					.collect(Collectors.toMap(SemesterSubject::getId, e -> e));
			subjectDtos.stream().forEach(dto->{
				SemesterSubject model = new SemesterSubject();
				if (StringUtils.hasText(dto.getId())) {
					log.info(
							"id is present so going to see if it is present in db if yes then we have to update it");
					model = existingSubjectMap.get(dto.getId());
					if (ObjectUtils.isEmpty(model)) {
						log.error("invalid course fees id : {}", dto.getId());
						throw new RuntimeNotFoundException("invalid course fees id : " + dto.getId());
					}
				}else {
					subjects.add(model);
				}
				model.setName(dto.getName());
				model.setDescription(dto.getDescription());
				model.setAuditFields(userId);
				model.setCourseSemester(courseSemester);
			});
			
		} else {
			courseSemester.getSubjects().clear();
		}
	}
}