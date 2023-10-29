	package com.azguards.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.dao.UserRecommendationDao;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.processor.CourseDeliveryModesProcessor;
import com.azguards.app.processor.CourseProcessor;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;
@Service
@Transactional(rollbackFor = Throwable.class)
@CommonsLog
public class UserRecommendationServiceImpl implements UserRecommendationService {

	@Autowired
	private UserRecommendationDao userRecommendationDao;

	@Autowired
	@Lazy
	private CourseProcessor courseProcessor;

	@Autowired
	private StorageHandler storageHandler;
	
	@Autowired
	private CourseDeliveryModesProcessor courseDeliveryModesProcessor;

	@Autowired
	private MessageTranslator messageTranslator;

	@Override
	public List<Course> getRecommendCourse(final String courseId, final String userId) throws ValidationException {
		Course existingCourse = courseProcessor.getCourseData(courseId);
		if (existingCourse == null) {
			throw new ValidationException(messageTranslator.toLocale("recommendation.not_found.course", courseId));
		}
		String facultyId = null;
		String instituteId = null;
		String countryId = null;
		String cityId = null;
		if (existingCourse.getFaculty() != null) {
			facultyId = existingCourse.getFaculty().getId();
		}
		if (existingCourse.getInstitute() != null) {
			instituteId = existingCourse.getInstitute().getId();
		}
//		Double price = existingCourse.getUsdInternationFee();
		List<Course> resultList = new ArrayList<>();

		List<String> courseIds = new ArrayList<>();
		courseIds.add(courseId);
		/**
		 * same faculty , same institute , same country , same city -> with
		 * usdInternationFee +-2000, +-5000 +-10000
		 */
		List<Course> firstCourses = userRecommendationDao.getRecommendCourse(facultyId, instituteId, countryId, cityId, null, Double.valueOf(2000), 2,
				courseIds);
		if (firstCourses.size() < 2) {
			firstCourses = userRecommendationDao.getRecommendCourse(facultyId, instituteId, countryId, cityId, null, Double.valueOf(5000), 2, courseIds);
		}
		if (firstCourses.size() < 2) {
			firstCourses = userRecommendationDao.getRecommendCourse(facultyId, instituteId, countryId, cityId, null, Double.valueOf(10000), 2, courseIds);
		}
		resultList.addAll(firstCourses);

		/**
		 * same faculty , different institute , same country , same city -> with
		 * usdInternationFee +-2000, +-5000 +-10000
		 */
		int remainingCourse = 4 - resultList.size();
		courseIds = resultList.stream().map(Course::getId).collect(Collectors.toList());
		courseIds.add(courseId);
		List<Course> secondCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, cityId, null, Double.valueOf(2000), remainingCourse,
				courseIds);
		if (secondCourses.size() < remainingCourse) {
			secondCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, cityId, null, Double.valueOf(5000), remainingCourse,
					courseIds);
		}
		if (secondCourses.size() < remainingCourse) {
			secondCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, cityId, null, Double.valueOf(10000), remainingCourse,
					courseIds);
		}
		resultList.addAll(secondCourses);

		/**
		 * same faculty , different institute , same country , different city -> with
		 * usdInternationFee +-2000, +-5000 +-10000
		 */
		remainingCourse = 4 - resultList.size();
		List<Course> thirdCourses = new ArrayList<>();
		if (remainingCourse != 0) {
			courseIds = resultList.stream().map(Course::getId).collect(Collectors.toList());
			courseIds.add(courseId);
			thirdCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, null, null, Double.valueOf(2000), remainingCourse, courseIds);
			if (thirdCourses.size() < remainingCourse) {
				thirdCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, null, null, Double.valueOf(5000), remainingCourse,
						courseIds);
			}
			if (thirdCourses.size() < remainingCourse) {
				thirdCourses = userRecommendationDao.getRecommendCourse(facultyId, null, countryId, null, null, Double.valueOf(10000), remainingCourse,
						courseIds);
			}
			resultList.addAll(thirdCourses);
		}

		/**
		 * same faculty , different institute , different country , different city ->
		 * with usdInternationFee +-2000, +-5000 +-10000
		 */
		remainingCourse = 4 - resultList.size();
		List<Course> forthCourses = new ArrayList<>();
		if (remainingCourse != 0) {
			courseIds = resultList.stream().map(Course::getId).collect(Collectors.toList());
			courseIds.add(courseId);
			forthCourses = userRecommendationDao.getRecommendCourse(facultyId, null, null, null, null, Double.valueOf(2000), remainingCourse, courseIds);
			if (forthCourses.size() < remainingCourse) {
				forthCourses = userRecommendationDao.getRecommendCourse(facultyId, null, null, null, null, Double.valueOf(5000), remainingCourse, courseIds);
			} else if (forthCourses.size() < remainingCourse) {
				forthCourses = userRecommendationDao.getRecommendCourse(facultyId, null, null, null, null, Double.valueOf(10000), remainingCourse, courseIds);
			}
			resultList.addAll(forthCourses);
		}

		remainingCourse = 4 - resultList.size();

		if (remainingCourse < 5 && userId != null) {
			courseIds = resultList.stream().map(Course::getId).collect(Collectors.toList());
			courseIds.add(courseId);
			List<Course> courseWatchList = courseProcessor.getAllCoursesUsingId(courseIds);

			while (remainingCourse != 0) {
				for (Course course : courseWatchList) {
					if (!courseIds.contains(course.getId())) {
						resultList.add(course);
						remainingCourse--;
					}
				}
			}
		}
		return resultList;
	}

	@Override
	public List<Course> getRelatedCourse(final String courseId) throws ValidationException {
		Course existingCourse = courseProcessor.getCourseData(courseId);
		if (existingCourse == null) {
			throw new ValidationException(messageTranslator.toLocale("recommendation.not_found.course", courseId));
		}
		String facultyId = null;
		String instituteId = null;
		String countryId = null;
		String cityId = null;
		if (existingCourse.getFaculty() != null) {
			facultyId = existingCourse.getFaculty().getId();
		}
		if (existingCourse.getInstitute() != null) {
			instituteId = existingCourse.getInstitute().getId();
		}
		String courseName = existingCourse.getName();
//		Double price = existingCourse.getUsdInternationFee();
		List<Course> resultList = new ArrayList<>();

		/**
		 * Same CourseName, same faculty , same institute , same country , same city ->
		 * with usdInternationFee +-2000, +-5000 +-10000
		 */
		List<Course> firstCourses = getRelatedCoursesPrice(facultyId, instituteId, countryId, cityId, courseName, null, resultList, courseId);
		resultList.addAll(firstCourses);

		/**
		 * Same CourseName, same faculty , different institute , same country , same
		 * city -> with usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, countryId, cityId, courseName, null, resultList, courseId);
			resultList.addAll(courses);
		}

		/**
		 * Same CourseName, same faculty , different institute , same country ,
		 * different city -> with usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, countryId, null, courseName, null, resultList, courseId);
			resultList.addAll(courses);
		}

		/**
		 * Same CourseName, same faculty , different institute , different country ,
		 * different city -> with usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, null, null, courseName, null, resultList, courseId);
			resultList.addAll(courses);
		}

		/**
		 * same faculty , different institute , same country , same city -> with
		 * usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, countryId, cityId, null, null, resultList, courseId);
			resultList.addAll(courses);
		}

		/**
		 * same faculty , different institute , same country , different city -> with
		 * usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, countryId, null, null, null, resultList, courseId);
			resultList.addAll(courses);
		}

		/**
		 * same faculty , different institute , different country , different city ->
		 * with usdInternationFee +-2000, +-5000 +-10000
		 */
		if (resultList.size() < 5) {
			List<Course> courses = getRelatedCoursesPrice(facultyId, null, null, null, null, null, resultList, courseId);
			resultList.addAll(courses);
		}

		return resultList;
	}

	/**
	 * General method for related courses with all same parameter but different
	 * price.
	 *
	 * @param facultyId
	 * @param instituteId
	 * @param countryId
	 * @param cityId
	 * @param courseName
	 * @param price
	 * @param resultList
	 * @return
	 */
	private List<Course> getRelatedCoursesPrice(final String facultyId, final String instituteId, final String countryId, final String cityId,
			final String courseName, final Double price, final List<Course> resultList, final String courseId) {
		List<String> courseIds = new ArrayList<>();
		courseIds.add(courseId);
		if (!resultList.isEmpty()) {
			List<String> list = resultList.stream().map(Course::getId).collect(Collectors.toList());
			courseIds.addAll(list);
		}
		int pageSize = 5 - resultList.size();
		List<Course> courseList = userRecommendationDao.getRelatedCourse(facultyId, instituteId, countryId, cityId, price, Double.valueOf(2000), pageSize,
				courseIds, courseName);
		if (courseList.size() < 5) {
			courseList = userRecommendationDao.getRelatedCourse(facultyId, instituteId, countryId, cityId, price, Double.valueOf(5000), pageSize, courseIds,
					courseName);
		}
		if (courseList.size() < 5) {
			courseList = userRecommendationDao.getRelatedCourse(facultyId, instituteId, countryId, cityId, price, Double.valueOf(10000), pageSize, courseIds,
					courseName);
		}
		return courseList;
	}

	/**
	 * For restricted response for related course
	 */
	@Override
	public List<CourseResponseDto> getCourseRelated(final String courseId) throws ValidationException {
		List<Course> courseList = getRelatedCourse(courseId);
		return convertCourseToCourseRespone(courseList);
	}

	@Override
	public List<CourseResponseDto> getCourseRecommended(final String courseId) throws ValidationException {
		List<Course> courseList = getRecommendCourse(courseId, null);
		return convertCourseToCourseRespone(courseList);
	}

	private List<CourseResponseDto> convertCourseToCourseRespone(final List<Course> courseList)
			throws ValidationException {
		List<CourseResponseDto> resultList = new ArrayList<>();
		courseList.stream().forEach(course -> {
			if (!ObjectUtils.isEmpty(course.getInstitute())) {
				CourseResponseDto courseResponseDto = new CourseResponseDto();
				courseResponseDto.setId(course.getId());
				courseResponseDto.setName(course.getName());
				courseResponseDto.setInstituteId(course.getInstitute().getId());
				courseResponseDto.setInstituteName(course.getInstitute().getName());
				courseResponseDto.setRequirements(course.getRemarks());
				if (course.getStars() != null) {
					courseResponseDto.setStars(Double.valueOf(course.getStars()));
				}
				courseResponseDto.setCourseRanking(course.getWorldRanking());
				courseResponseDto.setCityName(course.getInstitute().getCityName());
				courseResponseDto.setCountryName(course.getInstitute().getCountryName());
				courseResponseDto.setLocation(course.getInstitute().getCityName() + "," + course.getInstitute().getCountryName());
				courseResponseDto.setLatitude(course.getInstitute().getLatitude());
				courseResponseDto.setLongitude(course.getInstitute().getLongitude());
				courseResponseDto.setCurrencyCode(course.getCurrency());
				if (!ObjectUtils.isEmpty(course.getFaculty())) {
					courseResponseDto.setFacultyId(course.getFaculty().getId());
					courseResponseDto.setFacultyName(course.getFaculty().getName());
				}
				try {
					List<StorageDto> storageDTOList = storageHandler.getStorages(
							course.getInstitute().getId(), EntityTypeEnum.COURSE,EntitySubTypeEnum.IMAGES);
					courseResponseDto.setStorageList(storageDTOList);
				} catch (Exception e) {
					log.error("Exception while invoking storage service", e);
				}
				courseResponseDto.setCourseDeliveryModes(
						courseDeliveryModesProcessor.getCourseDeliveryModesByCourseId(course.getId()));
				resultList.add(courseResponseDto);
			}
		});
		return resultList;
	}

	/**
	 * First we find based on faculty and country
	 */
	@Override
	public List<CourseResponseDto> getCourseNoResultRecommendation(final String facultyId, final String countryId, final Integer startIndex,
			final Integer pageSize) throws ValidationException {
		/**
		 * Find courses with the same faculty and country
		 */
		List<Course> courseList = userRecommendationDao.getCourseNoResultRecommendation(facultyId, countryId, null, startIndex, pageSize);
		/**
		 * If required courses are not found then we will find courses with any faculty
		 * but with the same country.
		 *
		 */
		if (!courseList.isEmpty() && courseList.size() <= pageSize) {
			List<Course> courseCountryList = userRecommendationDao.getCourseNoResultRecommendation(null, countryId,
					courseList.stream().map(Course::getId).collect(Collectors.toList()), startIndex, pageSize - courseList.size());
			courseList.addAll(courseCountryList);
		} else {
			List<Course> courseCountryList = userRecommendationDao.getCourseNoResultRecommendation(null, countryId, null, startIndex, pageSize);
			courseList.addAll(courseCountryList);
		}
		return convertCourseToCourseRespone(courseList);
	}

	@Override
	public List<CourseResponseDto> getCheapestCourse(final String facultyId, final String countryId, final String levelId, final String cityId,
			final Integer startIndex, final Integer pageSize) throws ValidationException {
		/**
		 * First we find courses based on same faculty, same country and same level but
		 * different city
		 *
		 */
		List<Course> courseList = userRecommendationDao.getCheapestCourse(facultyId, countryId, levelId, cityId, null, startIndex, pageSize);

		log.info("Filtering distinct courses based on courseId and collect in list");
		List<Course> courseFilteredList = courseList.stream().filter(Utils.distinctByKey(Course::getId))
				.collect(Collectors.toList());
		
		/**
		 * if required courses not found then other courses find based on same faculty,
		 * same country but different city
		 *
		 */
		if (!courseFilteredList.isEmpty() && courseFilteredList.size() <= pageSize) {
			List<Course> moreCourses = userRecommendationDao.getCheapestCourse(facultyId, countryId, null, cityId,
					courseFilteredList.stream().map(Course::getId).collect(Collectors.toList()), startIndex, pageSize - courseFilteredList.size());
			courseFilteredList.addAll(moreCourses);
		}

		/**
		 * if required courses not found then other courses find based on same country
		 * but different city
		 *
		 */
		if (!courseFilteredList.isEmpty() && courseFilteredList.size() <= pageSize) {
			List<Course> moreCourses = userRecommendationDao.getCheapestCourse(null, countryId, null, cityId,
					courseFilteredList.stream().map(Course::getId).collect(Collectors.toList()), startIndex, pageSize - courseFilteredList.size());
			courseFilteredList.addAll(moreCourses);
		}

		return convertCourseToCourseRespone(courseFilteredList);
	}
}
