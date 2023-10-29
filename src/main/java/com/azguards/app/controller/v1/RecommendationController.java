package com.azguards.app.controller.v1;
/*package com.seeka.app.controller.v1;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seeka.app.bean.Course;
import com.seeka.app.controller.handler.GenericResponseHandlers;
import com.seeka.app.dto.ArticleResposeDto;
import com.seeka.app.dto.CourseResponseDto;
import com.seeka.app.dto.InstituteResponseDto;
import com.seeka.app.dto.MyHistoryDto;
import com.seeka.app.dto.ScholarshipDto;
import com.seeka.app.exception.NotFoundException;
import com.seeka.app.exception.ValidationException;
import com.seeka.app.message.MessageByLocaleService;
import com.seeka.app.service.IRecommendationService;
import com.seeka.app.util.IConstant;

@RestController("recommendationControllerV1")
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {

	@Autowired
	private IRecommendationService iRecommendationService;

	@Autowired
	private MessageByLocaleService messageByLocalService;

	@GetMapping("/institute")
	public ResponseEntity<?> getRecommendedInstitutes(@RequestHeader(value = "userId") final String userId,
			@RequestHeader(value = "language") final String language
																	 * , @RequestParam(value = "startIndex", required = false) Long startIndex,
																	 *
																	 * @RequestParam(value = "pageSize") Long pageSize, @RequestParam(value =
																	 * "pageNumber", required=false) Long pageNumber
																	 ) throws ValidationException, NotFoundException {

		// paginationValidations(language, startIndex, pageSize, pageNumber);
		List<InstituteResponseDto> instituteResponseList = iRecommendationService.getRecommendedInstitutes(userId,
				 startIndex, pageSize, pageNumber, language);

		return new GenericResponseHandlers.Builder().setData(instituteResponseList)
				.setMessage(messageByLocalService.getMessage("list.display.successfully", new Object[] { IConstant.INSTITUTE }, language))
				.setStatus(HttpStatus.OK).create();
	}

	@GetMapping("/otherPeopleSearch")
	public ResponseEntity<?> getOtherPeopleSearch() {
		return null;
	}

	@GetMapping("/otherPeopleInstituteSearch")
	public ResponseEntity<?> getInsituteBasedOnOtherPeopleSearch(@RequestHeader(value = "userId") final String userId,
			@RequestHeader(value = "language", required = false) final String language) {
		List<InstituteResponseDto> institutesBasedOnOtherPeopleSearch = iRecommendationService.getinstitutesBasedOnOtherPeopleSearch(userId);
		return new GenericResponseHandlers.Builder().setData(institutesBasedOnOtherPeopleSearch)
				.setMessage(messageByLocalService.getMessage("list.display.successfully", new Object[] { IConstant.INSTITUTE }, language))
				.setStatus(HttpStatus.OK).create();
	}

	@GetMapping("/courses")
	public ResponseEntity<?> getRecommendedCourses(@RequestHeader(value = "userId") final String userId,
			@RequestHeader(value = "language", required = false) final String language) throws ValidationException {
		List<CourseResponseDto> recomendedCourses = iRecommendationService.getRecommendedCourses(userId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(recomendedCourses)
				.setMessage(messageByLocalService.getMessage("list.display.successfully", new Object[] { IConstant.COURSE }, language)).create();
	}

	@GetMapping("/articles")
	public ResponseEntity<?> getRecommendedArticles(@RequestHeader(value = "userId") final String userId,
			@RequestHeader(value = "language", required = false) final String language) throws ValidationException {
		List<ArticleResposeDto> recomendedArticles = iRecommendationService.getRecommendedArticles(userId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(recomendedArticles)
				.setMessage(messageByLocalService.getMessage("list.display.successfully", new Object[] { IConstant.ARTICLE }, language)).create();
	}

	@GetMapping("/topSearchedCourses/{facultyId}")
	public ResponseEntity<?> getTopSearchedCourse(@RequestHeader(value = "userId") final String userId, @PathVariable final String facultyId) {
		List<Course> topSearchedCourses = iRecommendationService.getTopSearchedCoursesForFaculty(facultyId, userId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(topSearchedCourses)
				.setMessage("Recommended Course Displayed Successfully").create();
	}

	@GetMapping("/userCourseRelatedToSearch")
	public ResponseEntity<?> displayRelatedCourseAsPerUserPastSearch(@RequestHeader(value = "userId") final String userId) throws ValidationException {
		Set<Course> listOfRelatedCourses = iRecommendationService.displayRelatedCourseAsPerUserPastSearch(userId);
		return new GenericResponseHandlers.Builder().setData(listOfRelatedCourses).setMessage("Related Courses Display Successfully").setStatus(HttpStatus.OK)
				.create();
	}

	@GetMapping("/scholarship")
	public ResponseEntity<?> getRecommendedScholarship(@RequestHeader(value = "userId") final String userId,
			@RequestHeader(value = "language", required = false) final String language) throws ValidationException, NotFoundException {
		List<ScholarshipDto> scholarshipDtoList = iRecommendationService.getRecommendedScholarships(userId, language);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(scholarshipDtoList)
				.setMessage(messageByLocalService.getMessage("list.display.successfully", new Object[] { IConstant.SCHOLARSHIP }, language)).create();
	}

	private void paginationValidations(final String language, final Long startIndex, final Long pageSize, final Long pageNumber) throws ValidationException {
		if (pageSize == null) {
			throw new ValidationException(messageByLocalService.getMessage("page.size.null", new Object[] {}, language));
		}

		if (startIndex == null && pageNumber == null) {
			throw new ValidationException(messageByLocalService.getMessage("start.index.or.page.number.mandatory", new Object[] {}, language));
		}
	}
	
	@GetMapping("/myHistory/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getRecommendedMyCourse(@PathVariable Integer pageNumber, @PathVariable Integer pageSize, @RequestHeader(value = "userId") String userId) {
		List<MyHistoryDto> historyDtos = iRecommendationService.getRecommendedMyHistory(userId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(historyDtos)
				.setMessage("Courses Displayed successfully").create();
	}
}
*/