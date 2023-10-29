package com.azguards.app.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseCurriculum;
import com.azguards.app.bean.CourseDeliveryModes;
import com.azguards.app.bean.CourseEnglishEligibility;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.CoursePrerequisite;
import com.azguards.app.dao.CourseCurriculumDao;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.FacultyDao;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.LevelDao;
import com.azguards.app.dto.uploader.CourseCsvDto;
import com.azguards.common.lib.dto.common.CurrencyRateDto;
import com.azguards.common.lib.handler.CommonHandler;
import com.azguards.common.lib.util.DateUtil;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseItemProcessor implements ItemProcessor<CourseCsvDto, Course> {
	
	@Autowired
	private InstituteDao institueDao;

	@Autowired
	private FacultyDao facultyDao;

	@Autowired
	private LevelDao levelDao;
	
	@Autowired
	private CourseCurriculumDao courseCurriculumDao;
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private CommonHandler commonHandler;
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public Course process(CourseCsvDto courseDto) throws Exception {
		log.info("Fetching all level from db");
		Map<String, String> levelMap = levelDao.getAllLevelMap();
		log.info("Fetching all institute from db");
		Map<String, String> instituteMap = institueDao.getAllInstituteMap();
		log.info("Fetching all faculty from db");
		Map<String, String> facultyMap = facultyDao.getFacultyNameIdMap();
		List<CourseCurriculum> courseCurriculumList = courseCurriculumDao.getAll();
		Map<String, CourseCurriculum> curriculumMap  = courseCurriculumList.stream().collect(Collectors.toMap(CourseCurriculum::getName, curriculum -> curriculum));
		Course course = new Course();
		course.setId(UUID.randomUUID().toString());
		course.setAbbreviation(courseDto.getAbbreviation());
		course.setWorldRanking(courseDto.getWorldRanking());
		course.setName(courseDto.getName());
		if(!StringUtils.isEmpty(courseDto.getCurriculum())) {
			course.setCourseCurriculum(curriculumMap.get(courseDto.getCurriculum()));
		}
		course.setRemarks(courseDto.getRemarks());
		course.setWebsite(courseDto.getWebsite());
		course.setStars(courseDto.getStars());
		course.setDescription(courseDto.getDescription());
		course.setFaculty(facultyDao.getFaculty(facultyMap.get(courseDto.getFacultyName())));
		course.setInstitute(institueDao.getInstitute(
				instituteMap.get(courseDto.getInstituteName() + "~" + courseDto.getCityName() + "~" + StringUtils.trim(courseDto.getCountryName()))));
		course.setIsActive(true);
		course.setRecognition(courseDto.getRecognition());
		course.setRecognitionType(courseDto.getRecognitionType());
		course.setLevel(levelDao.getLevel(levelMap.get(courseDto.getLevelCode())));
		course.setAvailabilty(courseDto.getAvailabilty());
		course.setExaminationBoard(courseDto.getExaminationBoard());
		course.setDomesticApplicationFee(courseDto.getDomesticApplicationFee());
		course.setInternationalApplicationFee(courseDto.getInternationalApplicationFee());
		course.setDomesticEnrollmentFee(courseDto.getDomesticEnrollmentFee());
		course.setInternationalEnrollmentFee(courseDto.getInternationalEnrollmentFee());
		course.setEntranceExam(courseDto.getEntranceExam());

		course.setDescription(courseDto.getDescription());
		course.setCurrency(courseDto.getCurrency());
		course.setCurrencyTime(courseDto.getCurrencyTime());

		course.setCreatedBy("AUTO");
		course.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		course.setUpdatedBy("AUTO");
		course.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		course.setContent(getContent(courseDto));
		course.setGlobalGpa(courseDto.getGlobalGpa());
		setReadableIdForCourse(course);

		log.info("Adding englist eligibility into course");
		if (courseDto.getIeltsOverall() != null) {
			log.info("Associating IELTS score with course");
			CourseEnglishEligibility courseEnglishEligibility = new CourseEnglishEligibility("IELTS", courseDto.getIeltsReading(), courseDto.getIeltsWriting(), courseDto.getIeltsSpeaking(), courseDto.getIeltsListening(), courseDto.getIeltsOverall(), true, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), null, "AUTO", "AUTO", false);
			course.getCourseEnglishEligibilities().add(courseEnglishEligibility);
			courseEnglishEligibility.setCourse(course);
		}
		
		if (courseDto.getToflOverall() != null) {
			log.info("Associating TOEFL score with course");
			CourseEnglishEligibility courseEnglishEligibility = new CourseEnglishEligibility("TOEFL", courseDto.getToflReading(), courseDto.getToflWriting(), courseDto.getToflSpeaking(), courseDto.getToflListening(), courseDto.getToflOverall(), true, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), null, "AUTO", "AUTO", false);
			course.getCourseEnglishEligibilities().add(courseEnglishEligibility);
			courseEnglishEligibility.setCourse(course);
		}
		
		if (!org.springframework.util.ObjectUtils.isEmpty(course.getInstitute())) {
			log.info("Institute not null saving world ranking");
			saveWorldRanking(course, courseDto.getWorldRanking());
		} 
		
		if (!StringUtils.isEmpty(courseDto.getLanguage())) {
			log.info("Saving course language into DB");
			saveCourseLanguage(Arrays.asList(courseDto.getLanguage().split(",")), course);
		}
		
		List<String> deliveryModes = new ArrayList<>();
		
		if(!StringUtils.isEmpty(courseDto.getClassroom()) && courseDto.getClassroom().equalsIgnoreCase("Yes") ) {
			deliveryModes.add("Classroom");
		}
		if(!StringUtils.isEmpty(courseDto.getOnline()) && courseDto.getOnline().equalsIgnoreCase("Yes") ) {
			deliveryModes.add("Online");
		}
		if(!StringUtils.isEmpty(courseDto.getBlended()) && courseDto.getBlended().equalsIgnoreCase("Yes")) {
			deliveryModes.add("Blended");
		}
		log.info("Calling saveCourseDeliveryModes() method");
		this.saveCourseDeliveryModes(deliveryModes, courseDto, course);
		log.info("Calling populateUsdPrices method()");
		this.populateUsdPrices(course,courseDto);
		return course;
	}
	
	private void saveWorldRanking(final Course course, final Integer worldRanking) {
		if (course.getInstitute().getWorldRanking() == null) {
			log.info("World Ranking {} for institute id {}: ", worldRanking, course.getInstitute().getId());
			course.getInstitute().setWorldRanking(worldRanking);
		}
	}

	private String getContent(final CourseCsvDto courseDto) {
		StringBuilder content = new StringBuilder();
		if (courseDto.getContent() != null && !courseDto.getContent().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent());
		}
		if (courseDto.getContent2() != null && !courseDto.getContent2().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent2());
		}
		if (courseDto.getContent3() != null && !courseDto.getContent3().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent3());
		}
		if (courseDto.getContent4() != null && !courseDto.getContent4().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent4());
		}
		if (courseDto.getContent5() != null && !courseDto.getContent5().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent5());
		}
		if (courseDto.getContent6() != null && !courseDto.getContent6().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent6());
		}
		if (courseDto.getContent7() != null && !courseDto.getContent7().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent7());
		}
		if (courseDto.getContent8() != null && !courseDto.getContent8().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent8());
		}
		if (courseDto.getContent9() != null && !courseDto.getContent9().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent9());
		}
		if (courseDto.getContent10() != null && !courseDto.getContent10().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent10());
		}
		if (courseDto.getContent11() != null && !courseDto.getContent11().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent11());
		}
		if (courseDto.getContent12() != null && !courseDto.getContent12().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent12());
		}
		if (courseDto.getContent13() != null && !courseDto.getContent13().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent13());
		}
		if (courseDto.getContent14() != null && !courseDto.getContent14().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent14());
		}
		if (courseDto.getContent15() != null && !courseDto.getContent15().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent15());
		}
		if (courseDto.getContent16() != null && !courseDto.getContent16().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent16());
		}
		if (courseDto.getContent17() != null && !courseDto.getContent17().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent17());
		}
		if (courseDto.getContent18() != null && !courseDto.getContent18().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent18());
		}
		if (courseDto.getContent19() != null && !courseDto.getContent19().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent19());
		}
		if (courseDto.getContent20() != null && !courseDto.getContent20().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent20());
		}
		if (courseDto.getContent21() != null && !courseDto.getContent21().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent21());
		}
		if (courseDto.getContent22() != null && !courseDto.getContent22().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent22());
		}
		if (courseDto.getContent23() != null && !courseDto.getContent23().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent23());
		}
		if (courseDto.getContent24() != null && !courseDto.getContent24().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent24());
		}
		if (courseDto.getContent25() != null && !courseDto.getContent25().equalsIgnoreCase("NULL")) {
			content = content.append(" ").append(courseDto.getContent25());
		}
		return content.toString();
	}
	
	private void saveCourseLanguage(List<String> languages, Course course) {
		languages.stream().forEach(language -> {
			CourseLanguage courseLanguage = new CourseLanguage(language, course, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "API", "API");
			course.getCourseLanguages().add(courseLanguage);
		});
	}
	
	private void saveCourseDeliveryModes(List<String> deliveryModes, CourseCsvDto courseDto, Course course) {
		List<CourseDeliveryModes> courseDeliveryModes = new ArrayList<>();
		deliveryModes.stream().forEach(deliveryMode -> {
			
			if(courseDto.getFullTime().equalsIgnoreCase("Yes")) {
				CourseDeliveryModes courseDeliveryModeWithFullTime = new CourseDeliveryModes();
				courseDeliveryModeWithFullTime.setDeliveryType(deliveryMode);
				courseDeliveryModeWithFullTime.setStudyMode("Full Time");
//				courseDeliveryModeWithFullTime.setDomesticFee(courseDto.getDomesticFee());
//				courseDeliveryModeWithFullTime.setInternationalFee(courseDto.getInternationalFee());
				courseDeliveryModeWithFullTime.setDuration(courseDto.getDuration());
				courseDeliveryModeWithFullTime.setDurationTime(courseDto.getDurationTime());
				courseDeliveryModeWithFullTime.setCreatedBy("API");
				courseDeliveryModeWithFullTime.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
				courseDeliveryModeWithFullTime.setCourse(course);
				courseDeliveryModes.add(courseDeliveryModeWithFullTime);
	 		}
			
			if(courseDto.getPartTime().equalsIgnoreCase("Yes")) {
				CourseDeliveryModes courseDeliveryModeWithPartTime = new CourseDeliveryModes();
				courseDeliveryModeWithPartTime.setDeliveryType(deliveryMode);
				courseDeliveryModeWithPartTime.setStudyMode("Part Time");
//				courseDeliveryModeWithPartTime.setDomesticFee(courseDto.getDomesticFee());
//				courseDeliveryModeWithPartTime.setInternationalFee(courseDto.getInternationalFee());
				courseDeliveryModeWithPartTime.setDuration(courseDto.getDuration());
				courseDeliveryModeWithPartTime.setDurationTime(courseDto.getDurationTime());
				courseDeliveryModeWithPartTime.setCreatedBy("API");
				courseDeliveryModeWithPartTime.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
				courseDeliveryModeWithPartTime.setCourse(course);
				courseDeliveryModes.add(courseDeliveryModeWithPartTime);
			}
		});
		this.populateCourseUsdFee(courseDeliveryModes, course.getCurrency() );
		course.setCourseDeliveryModes(courseDeliveryModes);
	}
	
	
	private void populateCourseUsdFee(List<CourseDeliveryModes> listOfCourseDeliveryModes, String currencyCode) {
		listOfCourseDeliveryModes.stream().forEach(courseDeliveryMode -> {
			log.info("fetching currency rate based on currencyCode {}", currencyCode);
			CurrencyRateDto currencyRate = commonHandler.getCurrencyRateByCurrencyCode(currencyCode);
//			if (ObjectUtils.isEmpty(currencyRate)) {
//				try {
//					log.info("currency rate is not null going to saving currency rate in DB");
//					Map<String, CurrencyRate> currencyRateMap = commonHandler.saveCurrencyRate(currencyCode);
//					if (currencyRateMap != null) {
//						currencyRate = currencyRateMap.get(currencyCode);
//					}
//				} catch (IOException e) {
//					log.error("Exception while fetching currencyRate", e);
//				}
//			}

//			if (!ObjectUtils.isEmpty(courseDeliveryMode.getDomesticFee())) {
//				Double convertedRate = currencyRate.getConversionRate();
//				log.info("converting domestic Fee on basis of conviersionRate {}", convertedRate);
//				if (convertedRate != null && convertedRate != 0.0) {
//					log.info("converting domesticFee into usdDomesticFee");
//					Double usdDomesticFee = courseDeliveryMode.getDomesticFee() / convertedRate;
//					courseDeliveryMode.setUsdDomesticFee(usdDomesticFee);
//				}
//			}
//			if (!ObjectUtils.isEmpty(courseDeliveryMode.getInternationalFee())) {
//				Double convertedRate = currencyRate.getConversionRate();
//				log.info("converting international Fee on basis of conviersionRate {}", convertedRate);
//				if (convertedRate != null) {
//					log.info("converting internationalFee into usdInternationalFee");
//					Double usdInternationalFee = courseDeliveryMode.getInternationalFee() / convertedRate;
//					courseDeliveryMode.setUsdInternationalFee(usdInternationalFee);
//				}
//			}
		});
	}
	
	private void populateCoursePrerequisiteCertificate (CourseCsvDto courseDto, Course course) {
		List<CoursePrerequisite> listOfCoursePrerequisite = new ArrayList<> ();
		if (!courseDto.getCoursePreRequisiteCertificate01().equalsIgnoreCase("0") && !StringUtils.isEmpty(courseDto.getCoursePreRequisiteCertificate01())) {
			listOfCoursePrerequisite.add(new CoursePrerequisite(courseDto.getCoursePreRequisiteCertificate01(), course, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "API", "API"));
		}
		
		if (!courseDto.getCoursePreRequisiteCertificate02().equalsIgnoreCase("0")  && !StringUtils.isEmpty(courseDto.getCoursePreRequisiteCertificate02())) {
			listOfCoursePrerequisite.add(new CoursePrerequisite(courseDto.getCoursePreRequisiteCertificate02(), course, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "API", "API"));
		}
		
		if (!courseDto.getCoursePreRequisiteCertificate03().equalsIgnoreCase("0") && !StringUtils.isEmpty(courseDto.getCoursePreRequisiteCertificate03())) {
			listOfCoursePrerequisite.add(new CoursePrerequisite(courseDto.getCoursePreRequisiteCertificate03(), course, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "API", "API"));
		}
		
		course.setCoursePrerequisites(listOfCoursePrerequisite);
	}
	
	private void populateUsdPrices(Course course, CourseCsvDto courseDto) {
		log.info("fetching currency rate based on currencyCode {}", courseDto.getCurrency());
		CurrencyRateDto currencyRate = commonHandler.getCurrencyRateByCurrencyCode(courseDto.getCurrency());
//		if (ObjectUtils.isEmpty(currencyRate)) {
//			try {
//				log.info("currency rate is not null going to saving currency rate in DB");
//				Map<String, CurrencyRate> currencyRateMap = commonHandler.saveCurrencyRate(courseDto.getCurrency());
//				if (currencyRateMap != null) {
//					currencyRate = currencyRateMap.get(courseDto.getCurrency());
//				}
//			} catch (IOException e) {
//				log.error("Exception while fetching currencyRate", e);
//			}
//		}
		
		if (!ObjectUtils.isEmpty(course.getDomesticApplicationFee())) {
			Double convertedRate = currencyRate.getConversionRate();
			log.info("converting domestic application Fee on basis of conviersionRate {}", convertedRate);
			if (convertedRate != null && convertedRate != 0.0) {
				log.info("converting domesticApplicationFee to usdDomesticApplicationFee");
				Double usdDomesticApplicationFee = course.getDomesticApplicationFee() / convertedRate;
				course.setUsdDomesticApplicationFee(usdDomesticApplicationFee);
			}
		}
		if (!ObjectUtils.isEmpty(course.getInternationalApplicationFee())) {
			Double convertedRate = currencyRate.getConversionRate();
			log.info("converting international application Fee on basis of conviersionRate {}", convertedRate);
			if (convertedRate != null) {
				log.info("converting internationalApplicationFee into usdInternationalApplicationFee");
				Double usdInternationalApplicationFee = course.getInternationalApplicationFee() / convertedRate;
				course.setUsdInternationalApplicationFee(usdInternationalApplicationFee);
				
			}
		}	
		if (!ObjectUtils.isEmpty(course.getDomesticEnrollmentFee())) {
			Double convertedRate = currencyRate.getConversionRate();
			log.info("converting domestic enrollment Fee on basis of conviersionRate {}", convertedRate);
			if (convertedRate != null && convertedRate != 0.0) {
				log.info("converting domesticEnrollmentFee to usdDomesticEnrollmentFee");
				Double usdDomesticEnrollmentFee = course.getDomesticEnrollmentFee() / convertedRate;
				course.setUsdDomesticEnrollmentFee(usdDomesticEnrollmentFee);
			}
		}
		if (!ObjectUtils.isEmpty(course.getInternationalEnrollmentFee())) {
			Double convertedRate = currencyRate.getConversionRate();
			log.info("converting international Enrollment Fee on basis of conviersionRate {}", convertedRate);
			if (convertedRate != null) {
				log.info("converting internationalEnrollmentFee into usdInternationalEnrollmentFee");
				Double usdInternationalEnrollmentFee = course.getInternationalEnrollmentFee() / convertedRate;
				course.setUsdInternationalEnrollmentFee(usdInternationalEnrollmentFee);	
			}
		}
	}
	
	private void setReadableIdForCourse(Course course) {
		log.info("going to generate code for course");
		boolean reGenerateCode = false;
		do {
			reGenerateCode = false;
			String onlyName = Utils.convertToLowerCaseAndRemoveSpace(course.getName());
			String readableId = Utils.generateReadableId(onlyName);
			List<Course> sameCodeInsts = courseDao.findByReadableIdIn(Arrays.asList(onlyName, readableId));
			if (ObjectUtils.isEmpty(sameCodeInsts)) {
				course.setReadableId(onlyName);
			} else if (sameCodeInsts.size() == 1) {
				course.setReadableId(readableId);
			} else {
				reGenerateCode = true;
			}
		} while (reGenerateCode);
	}

}