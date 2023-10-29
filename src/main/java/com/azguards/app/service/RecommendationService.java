package com.azguards.app.service;
/*package com.seeka.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.seeka.app.bean.Course;
import com.seeka.app.bean.Faculty;
import com.seeka.app.bean.Institute;
import com.seeka.app.bean.SeekaArticles;
import com.seeka.app.bean.UserViewData;
import com.seeka.app.dto.ArticleResposeDto;
import com.seeka.app.dto.CourseResponseDto;
import com.seeka.app.dto.GlobalData;
import com.seeka.app.dto.InstituteResponseDto;
import com.seeka.app.dto.MyHistoryDto;
import com.seeka.app.dto.ScholarshipDto;
import com.seeka.app.dto.StorageDto;
import com.seeka.app.dto.UserDto;
import com.seeka.app.enumeration.ImageCategory;
import com.seeka.app.exception.NotFoundException;
import com.seeka.app.exception.ValidationException;
import com.seeka.app.message.MessageByLocaleService;
import com.seeka.app.processor.CourseProcessor;
import com.seeka.app.processor.FacultyProcessor;
import com.seeka.app.processor.InstituteProcessor;
import com.seeka.app.processor.ScholarshipProcessor;
import com.seeka.app.processor.StorageProcessor;
import com.seeka.app.util.IConstant;

import lombok.extern.apachecommons.CommonsLog;

@Service
@Transactional
@CommonsLog
public class RecommendationService implements IRecommendationService {

	@Autowired
	private FacultyProcessor facultyProcessor;

	@Autowired
	private IArticleService iArticleService;

	@Autowired
	private IGlobalStudentData iGlobalStudentData;

	@Autowired
	private ITop10CourseService iTop10CourseService;

	@Autowired
	private InstituteProcessor instituteProcessor;

	@Autowired
	private CourseProcessor courseProcessor;

	@Autowired
	private IUsersService iUsersService;

	@Autowired
	private IViewService viewService;

	@Autowired
	private IGlobalStudentData iGlobalStudentDataService;

	@Autowired
	private MessageByLocaleService messageByLocalService;

	@Autowired
	private StorageProcessor iStorageService;

	@Autowired
	private ScholarshipProcessor scholarshipProcessor;
	
//	@Autowired
//	private IUserMyCourseDAO userMyCourseDAO;

	@Value("${total.scholarship.per.page}")
	private Integer totalScholarshipPerPage;
	
	@Override
	public List<InstituteResponseDto> getRecommendedInstitutes(final String userId, 
																						 * Long startIndex, final Long pageSize, Long pageNumber,
																						 
			final String language) throws ValidationException, NotFoundException {

		UserDto userDto = iUsersService.getUserById(userId);
		if (userDto == null) {
			throw new NotFoundException(messageByLocalService.getMessage("user.not.found", new Object[] { userId }, language));
		} else if ((userDto.getCitizenship() == null) || userDto.getCitizenship().isEmpty()) {
			throw new ValidationException(messageByLocalService.getMessage("user.citizenship.not.present", new Object[] { userId }, language));
		}

		*//**
		 * Get Country Id Based on citizenship
		 *//*
//		Country country = iCountryService.getCountryBasedOnCitizenship(userDto.getCitizenship());
//		if ((country == null) || (country.getId() == null)) {
//			throw new ValidationException(
//					messageByLocalService.getMessage("invalid.citizenship.for.user", new Object[] { userDto.getCitizenship() }, language));
//		}

		*//**
		 * Final institute list to recommend
		 *//*
		Set<String> instituteListToRecommend = new HashSet<>();

		*//**
		 * Get institutes based on Past User Search based on country of courses
		 *//*
		List<String> instituteBasedOnPastSearch = getInstituteIdsBasedOnPastSearch(userId);

		*//**
		 * Get institutes Based on User's Country
		 *//*
		List<String> institutesBasedOnUserCountry = instituteProcessor.getTopInstituteIdByCountry(userDto.getCitizenship());

		*//**
		 * Get courses based on the country that other users from user's country are
		 * most interested to migrate to.
		 *//*

		List<GlobalData> globalDataDtoList = iGlobalStudentDataService.getCountryWiseStudentList(userDto.getCitizenship());

		List<String> countryNames = globalDataDtoList.stream().map(i -> i.getDestinationCountry()).collect(Collectors.toList());
		//List<String> allCountryIds = iCountryService.getCountryBasedOnCitizenship(countryNames);
		List<String> institutesById = instituteProcessor.getRandomInstituteIdByCountry(countryNames);
		*//**
		 * Select Random two courses from User Past Search
		 *//*
		instituteListToRecommend.addAll(instituteBasedOnPastSearch);
		instituteListToRecommend.addAll(institutesBasedOnUserCountry);
		instituteListToRecommend.addAll(institutesById);

		List<String> instList = new ArrayList<>();

		int count = 0;
		for (String id : instituteListToRecommend) {
			instList.add(id);
			if (count >= 20) {
				break;
			}
			count++;
		}

		return instituteProcessor.getAllInstituteByID(instList);

	}

	@Override
	public void getOtherPeopleSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CourseResponseDto> getRecommendedCourses(final String userId) throws ValidationException {
		UserDto userDto = iUsersService.getUserById(userId);
		*//**
		 * Validations of for user.
		 *//*
		if (userDto == null) {
			throw new ValidationException("Invalid User");
		} else if ((userDto.getCitizenship() == null) || userDto.getCitizenship().isEmpty()) {
			throw new ValidationException("User needs to have a citizenship");
		}

		*//**
		 * Get country object based on citizenship
		 *//*
//		Country country = getCountryBasedOnCitizenship(userDto.getCitizenship());
//
//		if ((country == null) || (country.getId() == null)) {
//			throw new ValidationException("Invalid country citizenship for the user");
//		}
		*//**
		 * Final course list to recommend
		 *//*
		List<String> courseListToRecommend = new ArrayList<>();

		*//**
		 * Get courses based on Past User Search based on country of courses
		 *//*
		List<String> coursesBasedOnPastSearch = courseProcessor.getTopSearchedCoursesByUsers(userId);

		*//**
		 * Get Courses Based on User's Country
		 *//*
		List<String> coursesBasedOnUserCountry = courseProcessor.courseIdsForCountry(userDto.getCitizenship());

		*//**
		 * Get courses based on the country that other users from user's country are
		 * most interested to migrate to.
		 *//*
		List<String> coursesBasedOnUserMigrationCountry = courseProcessor.courseIdsForMigratedCountries(userDto.getCitizenship());
		*//**
		 * Select Random two courses from User Past Search
		 *//*
		Random rand = new Random();
		if (coursesBasedOnPastSearch != null) {
			for (int i = 0; i < 2; i++) {
				if (coursesBasedOnPastSearch.size() > 0) {
					int randomIndex = rand.nextInt(coursesBasedOnPastSearch.size());
					String randomElement = coursesBasedOnPastSearch.get(randomIndex);
					coursesBasedOnPastSearch.remove(randomIndex);
					courseListToRecommend.add(randomElement);
				} else {
					break;
				}
			}
		}
		*//**
		 * Select Random five courses from User's Country
		 *//*
		if (coursesBasedOnUserCountry != null) {
			for (int i = 0; i < 5; i++) {
				if (coursesBasedOnUserCountry.size() > 0) {
					int randomIndex = rand.nextInt(coursesBasedOnUserCountry.size());
					String randomElement = String.valueOf(coursesBasedOnUserCountry.get(randomIndex));
					coursesBasedOnUserCountry.remove(randomIndex);
					courseListToRecommend.add(randomElement);
				} else {
					break;
				}
			}
		}

		*//**
		 * Select remaining random courses from most migrated countries as per users
		 * current country.
		 *//*
		if (coursesBasedOnUserMigrationCountry != null) {
			while ((20 - courseListToRecommend.size()) > 0) {
				if (coursesBasedOnUserMigrationCountry.size() > 0) {
					int randomIndex = rand.nextInt(coursesBasedOnUserMigrationCountry.size());
					String randomElement = String.valueOf(coursesBasedOnUserMigrationCountry.get(randomIndex));
					coursesBasedOnUserMigrationCountry.remove(randomIndex);
					courseListToRecommend.add(randomElement);
				} else {
					break;
				}
			}
		}
		*//**
		 * If no recommended courses are available, return an empty array.
		 *//*
		if ((courseListToRecommend == null) || courseListToRecommend.isEmpty()) {
			return new ArrayList<>();
		}
		List<Course> courseList = courseProcessor.getCoursesById(courseListToRecommend);
		List<CourseResponseDto> courseResponseDTOList = new ArrayList<>();
		for (Course course : courseList) {
			CourseResponseDto courseResponseDto = new CourseResponseDto();
			BeanUtils.copyProperties(course, courseResponseDto);
			courseResponseDto.setInstituteId(course.getInstitute().getId());
			courseResponseDto.setInstituteName(course.getInstitute().getName());
			List<StorageDto> storageDTOList = iStorageService.getStorageInformation(course.getInstitute().getId(), ImageCategory.INSTITUTE.toString(), null, "en");
			courseResponseDto.setStorageList(storageDTOList);
			if ((coursesBasedOnPastSearch != null) && coursesBasedOnPastSearch.contains(course.getId())) {
				courseResponseDto.setIsViewed(true);
			}
			courseResponseDTOList.add(courseResponseDto);
		}

		return courseResponseDTOList;
	}

	private List<Faculty> getAllFacultyIds() {
		List<String> facultyNames = iTop10CourseService.getAllDistinctFaculty();
		List<Faculty> facultyList = facultyProcessor.getFacultyListByName(facultyNames);
		return facultyList;
	}

	private List<Institute> getAllInstituteRankingWisePerCountry(final String country) {
		return instituteProcessor.ratingWiseInstituteListByCountry(country);
	}

	private List<Course> facultyWiseCourseForInstitute(final List<Faculty> facultyList, final Institute instituteId) {
		return courseProcessor.facultyWiseCourseForInstitute(facultyList, instituteId);
	}

	private Map<String, String> facultyWiseCourseIdMapForInstitute(final List<Faculty> facultyList, final Institute instituteId) {
		return courseProcessor.facultyWiseCourseIdMapForInstitute(facultyList, instituteId.getId());
	}

//	private Country getCountryBasedOnCitizenship(final String citizenship) {
//		return iCountryService.getCountryBasedOnCitizenship(citizenship);
//	}

	private long checkIfCoursesPresentForCountry(final String country) {
		return courseProcessor.checkIfCoursesPresentForCountry(country);
	}

	private List<Course> getTopRatedCoursesForCountryWorldRankingWise(final String country) {
		if (country != null) {
			return courseProcessor.getTopRatedCoursesForCountryWorldRankingWise(country);
		} else {
			return new ArrayList<>();
		}
	}

	private List<String> getTopRatedCourseIdsForCountryWorldRankingWise(final String country) {
		if (country != null) {
			return courseProcessor.getTopRatedCourseIdForCountryWorldRankingWise(country);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<Course> getTopSearchedCoursesForFaculty(final String facultyId, final String userId) {
		List<String> facultyWiseCourses = courseProcessor.getAllCourseUsingFacultyId(facultyId);
		List<String> allSearchCourses = courseProcessor.getTopSearchedCoursesByOtherUsers(userId);
		allSearchCourses.retainAll(facultyWiseCourses);
		System.out.println("All Course Size -- " + allSearchCourses.size());
		if ((allSearchCourses == null) || (allSearchCourses.size() == 0)) {
			allSearchCourses = facultyWiseCourses.size() > 10 ? facultyWiseCourses.subList(0, 9) : facultyWiseCourses;
		}

		return courseProcessor.getCoursesById(allSearchCourses);
	}

	@Override
	public Set<Course> displayRelatedCourseAsPerUserPastSearch(final String userId) throws ValidationException {
		List<String> userSearchCourseIdList = courseProcessor.getTopSearchedCoursesByUsers(userId);

		*//**
		 * Keeping this code to get related courses only for top 3 courses searched.
		 *//*
		if (userSearchCourseIdList.size() > 0) {
			userSearchCourseIdList = userSearchCourseIdList.size() > 3 ? userSearchCourseIdList.subList(0, 2)
					: userSearchCourseIdList.subList(0, userSearchCourseIdList.size() - 1);
		} else {
			return new HashSet<>();
		}
		*//**
		 * Logic ends
		 *//*
		Set<Course> userRelatedCourses = courseProcessor.getRelatedCoursesBasedOnPastSearch(userSearchCourseIdList);
		return userRelatedCourses;
	}

	@Override
	public List<ScholarshipDto> getRecommendedScholarships(final String userId, final String language) throws ValidationException, NotFoundException {

		*//**
		 * Get user details from userId
		 *//*
		UserDto userDto = iUsersService.getUserById(userId);
		if (userDto == null) {
			throw new NotFoundException(messageByLocalService.getMessage("user.not.found", new Object[] { userId }, language));
		} else if ((userDto.getCitizenship() == null) || userDto.getCitizenship().isEmpty()) {
			throw new ValidationException(messageByLocalService.getMessage("user.citizenship.not.present", new Object[] { userId }, language));
		}
		*//**
		 * Get courses based on Past User Search based on country of courses
		 *//*
		List<String> coursesBasedOnPastSearch = courseProcessor.getTopSearchedCoursesByUsers(userId);

		List<String> recommendedScholarships = new ArrayList<>();
		if ((coursesBasedOnPastSearch == null) || coursesBasedOnPastSearch.isEmpty()) {
			*//**
			 * If users donot have past search history run the below logic
			 *//*

			*//**
			 * Get Lis of all countries that users as interested to migrate to based on
			 * user's country
			 *//*
			List<String> distinctCountryList = iGlobalStudentDataService.getDistinctMigratedCountryForUserCountry(userDto.getCitizenship());
			if ((distinctCountryList != null) && !distinctCountryList.isEmpty()) {
				//List<Country> countries = iCountryService.getCountryListBasedOnCitizenship(distinctCountryList);
				//List<String> countryIdList = countries.stream().map(Country::getId).collect(Collectors.toList());
				*//**
				 * Get all scholarshipIds based on country
				 *//*
				recommendedScholarships.addAll(scholarshipProcessor.getScholarshipIdsByCountryId(distinctCountryList, totalScholarshipPerPage));
			}

			*//**
			 * If recommended scholarships are not equal to 20 then fetch more on random
			 * basis from the database
			 *//*
			if (recommendedScholarships.size() < totalScholarshipPerPage) {
				recommendedScholarships
						.addAll(scholarshipProcessor.getRandomScholarShipIds(totalScholarshipPerPage - recommendedScholarships.size()));
			}
		} else {
			*//**
			 * If users have past search history run the below logic.
			 *//*
			List<String> countryList = courseProcessor.getCountryForTopSearchedCourses(coursesBasedOnPastSearch);

			*//**
			 * Convert the countryList to Set to make it more random.
			 *//*
			Set<String> countrySet = new HashSet<>(countryList);
			for (String countryId : countrySet) {
				*//**
				 * Add all scholarship Ids obtained to recommended scholarships
				 *//*
				List<String> countryListForScholarship = new ArrayList<>();
				countryListForScholarship.add(countryId);
				recommendedScholarships.addAll(
						scholarshipProcessor.getScholarshipIdsByCountryId(countryListForScholarship, IConstant.SCHOLARSHIPS_PER_COUNTRY_FOR_RECOMMENDATION));
				*//**
				 * If recommended scholarships size exceeds 20, no need to consider other
				 * countries
				 *//*
				if (recommendedScholarships.size() >= totalScholarshipPerPage) {
					break;
				}
			}

			*//**
			 * If the recommended scholarship list is less than 20, then we need to consider
			 * more scholarships as per the Global Student Data file.
			 *//*
			if (recommendedScholarships.size() < totalScholarshipPerPage) {
				List<String> distinctCountryList = iGlobalStudentDataService.getDistinctMigratedCountryForUserCountry(userDto.getCitizenship());
				//List<Country> countries = iCountryService.getCountryListBasedOnCitizenship(distinctCountryList);
				// countries.stream().map(country ->
				// country.getId()).collect(Collectors.toList());
				//List<String> countryIdList = countries.stream().map(Country::getId).collect(Collectors.toList());

				recommendedScholarships.addAll(scholarshipProcessor.getScholarshipIdsByCountryId(distinctCountryList,
						totalScholarshipPerPage - recommendedScholarships.size()));
			}
			
			if (recommendedScholarships.size() < totalScholarshipPerPage) {
				recommendedScholarships
						.addAll(scholarshipProcessor.getRandomScholarShipIds(totalScholarshipPerPage - recommendedScholarships.size()));
			}
		}
		*//**
		 * Get Scholarship details for recommended scholarships
		 *//*
		List<ScholarshipDto> scholarshipDtoList = new ArrayList<>();
		if (!recommendedScholarships.isEmpty()) {
			scholarshipDtoList = scholarshipProcessor.getAllScholarshipDetailsFromId(recommendedScholarships);
		} 
		return scholarshipDtoList;
	}

	private List<String> getInstituteIdsBasedOnPastSearch(final String userId) throws ValidationException {

		List<String> instituteList = new ArrayList<>();

		List<String> topSearchedCourseIds = courseProcessor.getTopSearchedCoursesByUsers(userId);
		if ((topSearchedCourseIds != null) && !topSearchedCourseIds.isEmpty()) {
			List<String> countryForTopSearchedCourses = courseProcessor.getCountryForTopSearchedCourses(topSearchedCourseIds);
			for (String countryId : countryForTopSearchedCourses) {
				List<String> insituteId = instituteProcessor.getTopInstituteIdByCountry(countryId , 0L, 1L );
				instituteList.addAll(insituteId);
			}
		}

		return instituteList;
	}

	private List<Course> oldLogic(final String userId) throws ValidationException {
		UserDto userDto = iUsersService.getUserById(userId);
		*//**
		 * Validations of for user.
		 *//*
		if (userDto == null) {
			throw new ValidationException("Invalid User");
		} else if ((userDto.getCitizenship() == null) || userDto.getCitizenship().isEmpty()) {
			throw new ValidationException("User needs to have a citizenship");
		}

		*//**
		 * Get country object based on citizenship
		 *//*
//		Country country = getCountryBasedOnCitizenship(userDto.getCitizenship());
//
//		if ((country == null) || (country.getId() == null)) {
//			throw new ValidationException("Invalid country citizenship for the user");
//		}

		*//**
		 * Get all facultyIds from the excel uploaded by Top10Courses
		 *//*
		List<Faculty> allFaculty = getAllFacultyIds();

		*//**
		 * Check if the courses are present for the user's citizenship in our database.
		 *//*
		long courseCount = checkIfCoursesPresentForCountry(userDto.getCitizenship());

		if (courseCount > 0) {
			*//**
			 * Get all institutes based on world ranking for a particular country (of User)
			 *//*
			List<Institute> allInstitutesRankingWise = getAllInstituteRankingWisePerCountry(userDto.getCitizenship());

			*//**
			 * This list contains the details of the courses that are to be returned
			 *//*
			List<String> listOfRecommendedCourseIds = new ArrayList<>();
			List<Course> listOfRecommendedCourses = new ArrayList<>();
			Map<String, Faculty> facultyMap = new HashMap<>();

			// allFaculty.stream().forEach(i -> facultyMap.put(i.getId().longValue(), i));

			facultyMap = allFaculty.stream().collect(Collectors.toMap(i -> i.getId(), i -> i));
			Long count = 0L;

			System.out.println("insititute count -- " + allInstitutesRankingWise.size());

			for (Institute institute : allInstitutesRankingWise) {
				List<Course> listOfCourse = facultyWiseCourseForInstitute( allFaculty new ArrayList<>(facultyMap.values()),
						institute .getId().longValue() );
				Map<String, String> mapFacultyIdCourseId = facultyWiseCourseIdMapForInstitute(new ArrayList<>(facultyMap.values()), institute);
				System.out.println(count++);
				System.out.println("CourseList -- " + listOfCourse.size());

				for (Entry<String, String> entry : mapFacultyIdCourseId.entrySet()) {
					if (facultyMap.containsKey(entry.getValue())) {
						listOfRecommendedCourseIds.add(entry.getKey());
						*//**
						 * removing facultyIds from faculty map for which courses are already obtained
						 * so that we do not get any courses corresponding to those faculty for the next
						 * intitutes, also we get only one course per faculty in our final list of
						 * courses.
						 *//*
						facultyMap.remove(entry.getValue());
					}
				}

				*//**
				 * If we have obtained list of courses for all faculties then no need to further
				 * navigate through other institutes and hence breaking the loop
				 *//*
				if (facultyMap.isEmpty()) {
					break;
				}

			}
			System.out.println("insititute count -- " + allInstitutesRankingWise.size());

			listOfRecommendedCourses = courseProcessor.getAllCoursesUsingId(listOfRecommendedCourseIds);
			return listOfRecommendedCourses;
		} else {

			Map<String, List<String>> mapOfCountryToItsCoursesList = new TreeMap<>();

			List<Course> recommendedCourseList = new ArrayList<>();

			List<String> recommendedCourseListIds = new ArrayList<>();

			long count = iGlobalStudentDataService.checkForPresenceOfUserCountryInGlobalDataFile(userDto.getCitizenship());
			*//**
			 * If we dont get any country based on citizenship of user get data of China by
			 * default
			 *//*
			String countryName = null;
			if (count == 0) {
				countryName = "China";
			}

			*//**
			 * Get List of countries in which the people from user's country are interested
			 * in moving to. This data will be obtained from the table that contains the
			 * data from GlobalStudentData.xlsx file uploaded.
			 *//*
			List<GlobalData> countryWiseStudentCountListForUserCountry = iGlobalStudentDataService.getCountryWiseStudentList(countryName);
			List<String> courseList = new ArrayList<>();
			for (GlobalData globalDataDto : countryWiseStudentCountListForUserCountry) {
				courseList.add(globalDataDto.getDestinationCountry());
//				List<String> courseList = getTopRatedCourseIdsForCountryWorldRankingWise(
//						getCountryBasedOnCitizenship(globalDataDto.getDestinationCountry()));
				mapOfCountryToItsCoursesList.put(globalDataDto.getDestinationCountry(), courseList);
			}
			List<Integer> requiredCoursesPerCountry = new ArrayList<>();
			requiredCoursesPerCountry.add(2);
			requiredCoursesPerCountry.add(new Integer(5));
			requiredCoursesPerCountry.add(new Integer(10));
			requiredCoursesPerCountry.add(new Integer(20));
			int i = 0;
			while ((recommendedCourseList.size() < 20) && (i < 3)) {
				int courseCountPerCountry = requiredCoursesPerCountry.get(i);
				recommendedCourseList = new ArrayList<>();

				for (Entry<String, List<String>> entry : mapOfCountryToItsCoursesList.entrySet()) {
					List<String> cl = entry.getValue();
					if (cl.size() > 0) {
						recommendedCourseListIds.addAll(cl.subList(0, cl.size() > courseCountPerCountry ? courseCountPerCountry : cl.size() - 1));
					}
					if (recommendedCourseListIds.size() >= 20) {
						break;
					}
				}
				if (i == 3) {
					break;
				}
				i++;
			}

			recommendedCourseList = courseProcessor.getCoursesById(recommendedCourseListIds);

			System.out.println(recommendedCourseList);

			return recommendedCourseList;
		}
	}

	@Override
	public List<InstituteResponseDto> getinstitutesBasedOnOtherPeopleSearch(final String userId) {

		*//**
		 * Get all distinct courseIds searched by other users
		 *//*
		List<String> distinctCourseIds = courseProcessor.getTopSearchedCoursesByOtherUsers(userId);
		try {
			*//**
			 * Get all distinct country Ids for the course ids
			 *//*
			List<String> distinctCountryIds = courseProcessor.getCountryForTopSearchedCourses(distinctCourseIds);

			*//**
			 * Get all institutes for the various countries that other users have searched
			 * for
			 *//*
			List<String> allInstituteIds = instituteProcessor.getInstituteIdsFromCountry(distinctCountryIds);

			*//**
			 * If we dont get any institutes return an empty list
			 *//*
			if ((allInstituteIds == null) || allInstituteIds.isEmpty()) {
				return new ArrayList<>();
			}

			*//**
			 * If the list is less than 20 return all the institutes available
			 *//*
			if (allInstituteIds.size() < 20) {
				return instituteProcessor.getAllInstituteByID(allInstituteIds);
			}
			*//**
			 * If greater than 20 have a random logic for the same
			 *//*
			else {
				List<String> randomInstituteIds = new ArrayList<>();
				Random rand = new Random();
				while (randomInstituteIds.size() <= 20) {
					if (allInstituteIds.size() > 0) {
						int randomIndex = rand.nextInt(allInstituteIds.size());
						String randomElement = allInstituteIds.get(randomIndex);
						allInstituteIds.remove(randomIndex);
						randomInstituteIds.add(randomElement);
					} else {
						break;
					}
				}
				return instituteProcessor.getAllInstituteByID(randomInstituteIds);
			}

		} catch (ValidationException e) {
			return new ArrayList<>();
		}
	}

	private List<InstituteResponseDto> oldInstituteRecommendationLogic(final String userId, final Long startIndex, final Long pageSize,
			final Long pageNumber, final String language) {
		return null;
	}

	@Override
	public List<ArticleResposeDto> getRecommendedArticles(final String userId) throws ValidationException {
		*//**
		 * Query Identity to get UserDto from userId.
		 *//*

		UserDto userDto = iUsersService.getUserById(userId);
		*//**
		 * Validations of for user.
		 *//*
		if (userDto == null) {
			throw new ValidationException("Invalid User");
		}

		*//**
		 * Get List of all user viewd articles
		 *//*
		List<UserViewData> userViewDataList = viewService.getUserViewData((userDto.getId()), "ARTICLE", false, null, null);
		List<String> viewArticleIds = new ArrayList<>();
		for (UserViewData userViewData : userViewDataList) {
			viewArticleIds.add(userViewData.getEntityId());
		}

		//Country country;
		List<ArticleResposeDto> articleResposeDtolist = new ArrayList<>();
		List<SeekaArticles> seekaArticlelist = new ArrayList<>();

		if ((userDto.getCitizenship() == null) || userDto.getCitizenship().isEmpty()) {
			*//**
			 * If user citizenship doesn't exists use United States as a country by default.
			 *//*
			//country = getCountryBasedOnCitizenship("United States");

			*//**
			 * Get 2 articles per category from static list of categories provided by
			 * stakeholders.
			 *//*
			for (String categoryName : IConstant.LIST_OF_ARTICLE_CATEGORY) {
				List<SeekaArticles> articlelist = iArticleService.findArticleByCountryId("United States", categoryName,
						IConstant.ARTICLES_PER_CATEGORY_FOR_RECOMMENDATION, viewArticleIds);
				seekaArticlelist.addAll(articlelist);
			}
		} else {
			//country = getCountryBasedOnCitizenship(userDto.getCitizenship());
			*//**
			 * If the citizenship obtained from user doesn't exists in database
			 *//*
//			if (country == null) {
//				throw new ValidationException("Invalid country citizenship for the user");
//			}

			*//**
			 * Get 2 articles per category from static list of categories provided by
			 * stakeholders.
			 *//*
			for (String categoryName : IConstant.LIST_OF_ARTICLE_CATEGORY) {
				List<SeekaArticles> articlelist = iArticleService.findArticleByCountryId(userDto.getCitizenship(), categoryName,
						IConstant.ARTICLES_PER_CATEGORY_FOR_RECOMMENDATION, viewArticleIds);
				seekaArticlelist.addAll(articlelist);
			}

			*//**
			 * If No articles are obtained get the country from the global data student list
			 * and start displaying the articles
			 *//*
			if (seekaArticlelist.isEmpty() || (seekaArticlelist == null)) {
				List<GlobalData> globalDataList = iGlobalStudentData.getCountryWiseStudentList(userDto.getCitizenship());
				if ((globalDataList != null) && !globalDataList.isEmpty()) {

					*//**
					 * if first globalData destination country has no record for article then it's
					 * check for second globalData destination country and so on..
					 *//*
					for (GlobalData globalData : globalDataList) {
						//country = getCountryBasedOnCitizenship(globalData.getDestinationCountry());
						for (String categoryName : IConstant.LIST_OF_ARTICLE_CATEGORY) {
							List<SeekaArticles> articlelist = iArticleService.findArticleByCountryId(globalData.getDestinationCountry(), categoryName, 2, viewArticleIds);
							seekaArticlelist.addAll(articlelist);
						}

						*//**
						 * If articles are found break the loop
						 *//*
						if (!seekaArticlelist.isEmpty()) {
							break;
						}
					}

				}
			}
		}
		*//**
		 * bean converted into response Dto.
		 *//*
		for (SeekaArticles seekaArticles : seekaArticlelist) {
			ArticleResposeDto articleResposeDto = new ArticleResposeDto();
			BeanUtils.copyProperties(seekaArticles, articleResposeDto);
			if (seekaArticles.getCategory() != null) {
				articleResposeDto.setCategory(seekaArticles.getCategory().getName());
			}
			if (seekaArticles.getSubcategory() != null) {
				articleResposeDto.setSubcategory(seekaArticles.getSubcategory().getName());
			}
			if (seekaArticles.getCountryName() != null) {
				articleResposeDto.setCountry(seekaArticles.getCountryName());
			}
			if (seekaArticles.getCityName() != null) {
				articleResposeDto.setCity(seekaArticles.getCityName());
			}
			if (seekaArticles.getFaculty() != null) {
				articleResposeDto.setFaculty(seekaArticles.getFaculty().getName());
			}
			if (seekaArticles.getInstitute() != null) {
				articleResposeDto.setInstitute(seekaArticles.getInstitute().getName());
			}
			articleResposeDtolist.add(articleResposeDto);
		}
		return articleResposeDtolist;
	}

	@Override
	public List<MyHistoryDto> getRecommendedMyHistory(String userId) {
		log.debug("Inside getRecommendedMyHistory() method");
		List<MyHistoryDto> myHistoryDtos = new ArrayList<>();
		List<String> userViewCourseIds = null;
		int courseToDisplay = 10;
		log.info("fetching courseIDs from usermyCourses table for userId "+userId);
//		List<String> userMyCourseIds = userMyCourseDAO.getDataByUserIDWithPagination(userId, 0, courseToDisplay);

		if (userMyCourseIds.size() < 10) {
			log.info("if user saved course data is less than 10 then going to fetch user view courses from DB for userId "+userId);
			courseToDisplay = courseToDisplay - userMyCourseIds.size();
			userViewCourseIds = viewService.getRandomUserWatchCourseIds(userId, "COURSE", 0, courseToDisplay);
		}

		if (!CollectionUtils.isEmpty(userMyCourseIds)) {
			log.info("if user my course are not empty then start fetching courseID from it");
			userMyCourseIds.stream().forEach(userMyCourseId -> {
				MyHistoryDto myHistoryDto = new MyHistoryDto();
				log.info("fetching courses from DB having courseId "+userMyCourseId);
				Course courseFromDB = courseProcessor.getCourseData(userMyCourseId);
				myHistoryDto.setId(courseFromDB.getId());
				myHistoryDto.setName(courseFromDB.getName());
				myHistoryDto.setInstituteId(courseFromDB.getInstitute().getId());
				myHistoryDto.setInstituteName(courseFromDB.getInstitute().getName());
				myHistoryDto.setCityName(courseFromDB.getInstitute().getCityName());
				myHistoryDto.setCountryName(courseFromDB.getInstitute().getCountryName());
				myHistoryDto.setStars(Double.valueOf(courseFromDB.getStars()));
				try {
					log.info("start fetching logos from storage service for institute having instituteID "+courseFromDB.getInstitute().getId());
					List<StorageDto> storageDTOList = iStorageService.getStorageInformation(courseFromDB.getInstitute().getId(), 
							ImageCategory.INSTITUTE.toString(), null, "en");
					myHistoryDto.setStorage(storageDTOList);
				} catch (ValidationException e) {
					log.error("Exception in invoking storage service to fetch institute logos "+e);
				}
				myHistoryDtos.add(myHistoryDto);
			});
		}
		
		if (!CollectionUtils.isEmpty(userViewCourseIds)) {
			log.info("if user view course are not empty then start fetching courseID from it");
			userViewCourseIds.stream().forEach(userViewCourseId -> {
				log.info("checking for duplicacy for courseId in final response");
				if(!myHistoryDtos.stream().anyMatch(s -> s.getId().equals(userViewCourseId))) {
					log.info("fetching courses from DB having courseId "+userViewCourseId);
					Course courseFromDB = courseProcessor.getCourseData(userViewCourseId);
					if(!ObjectUtils.isEmpty(courseFromDB)) {
						MyHistoryDto myHistoryDto = new MyHistoryDto();
						myHistoryDto.setId(courseFromDB.getId());
						myHistoryDto.setName(courseFromDB.getName());
						myHistoryDto.setInstituteId(courseFromDB.getInstitute().getId());
						myHistoryDto.setInstituteName(courseFromDB.getInstitute().getName());
						myHistoryDto.setCityName(courseFromDB.getInstitute().getCityName());
						myHistoryDto.setCountryName(courseFromDB.getInstitute().getCountryName());
						myHistoryDto.setStars(Double.valueOf(courseFromDB.getStars()));
						try {
							log.info("start fetching logos from storage service for institute having instituteID "+courseFromDB.getInstitute().getId());
							List<StorageDto> storageDTOList = iStorageService.getStorageInformation(courseFromDB.getInstitute().getId(), 
									ImageCategory.INSTITUTE.toString(), null, "en");
							myHistoryDto.setStorage(storageDTOList);
						} catch (ValidationException e) {
							log.error("Exception in invoking storage service to fetch institute logos "+e);
						}
						myHistoryDtos.add(myHistoryDto);
					}
				}
			});
		}
		return myHistoryDtos;
	}
}
*/