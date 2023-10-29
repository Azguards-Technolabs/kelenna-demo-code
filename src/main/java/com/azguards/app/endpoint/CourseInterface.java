package com.azguards.app.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseFilterDto;
import com.azguards.app.dto.CourseMobileDto;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.common.lib.exception.ForbiddenException;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping(path = "/api/v1")
public interface CourseInterface {

	@PostMapping("/institute/{instituteId}/course")
	public ResponseEntity<?> save(@RequestHeader(required = true) final String userId, @PathVariable String instituteId,
			@Valid @RequestBody final CourseRequest course)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException;

	@PutMapping("/institute/{instituteId}/course/{id}")
	public ResponseEntity<?> update(@RequestHeader(required = true) final String userId,
			@PathVariable String instituteId, @Valid @RequestBody final CourseRequest course,
			@PathVariable final String id)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException;

	@GetMapping("/course/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getAllCourse(@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize)
			throws Exception;

	@GetMapping("/course/autoSearch/{searchKey}/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> autoSearch(@PathVariable final String searchKey, @PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize) throws Exception;

	@DeleteMapping("/course/{id}")
	public ResponseEntity<?> delete(@RequestHeader(required = true) final String userId,
			@Valid @PathVariable final String id,
			@RequestParam(value = "linked_course_ids", required = false) final List<String> linkedCourseIds)
			throws ForbiddenException, NotFoundException;

	@GetMapping("/course/search/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> searchCourse(@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize,
			@RequestParam(required = false) final List<String> countryIds,
			@RequestParam(required = false) final String instituteId,
			@RequestParam(required = false) final List<String> facultyIds,
			@RequestParam(required = false) final List<String> cityIds,
			@RequestParam(required = false) final List<String> levelIds,
			@RequestParam(required = false) final List<String> serviceIds,
			@RequestParam(required = false) final Double minCost, @RequestParam(required = false) final Double maxCost,
			@RequestParam(required = false) final Integer minDuration,
			@RequestParam(required = false) final Integer maxDuration,
			@RequestParam(required = false) final String courseName,
			@RequestParam(required = false) final String currencyCode,
			@RequestParam(required = false) final String searchKeyword,
			@RequestParam(required = false) final String sortBy,
			@RequestParam(required = false) final boolean sortAsscending,
			@RequestHeader(required = true) final String userId, @RequestParam(required = false) final String date)
			throws ValidationException, InvokeException, NotFoundException;

	@PostMapping(value = "/course/search")
	public ResponseEntity<?> searchCourse(@RequestHeader(required = true) final String userId,
			@RequestBody final CourseSearchDto courseSearchDto) throws Exception;

	@PostMapping(value = "/course/advanceSearch")
	public ResponseEntity<?> advanceSearch(@RequestHeader(required = true) final String userId,
			@RequestHeader(required = false) final String language, @RequestBody final AdvanceSearchDto courseSearchDto)
			throws Exception;

	@GetMapping("/course/{id}")
	public ResponseEntity<Object> get(@RequestHeader(required = false) final String userId,
			@Valid @PathVariable final String id,
			@RequestParam(name = "is_readable_id", required = false) boolean isReadableId) throws Exception;

	@PutMapping(value = "/course/institute/{instituteId}")
	public ResponseEntity<?> getAllCourseByInstituteID(@Valid @PathVariable final String instituteId,
			@Valid @RequestBody final CourseSearchDto request) throws Exception;

	@GetMapping(value = "/course/keyword")
	public ResponseEntity<?> searchCourseKeyword(@RequestParam(value = "keyword") final String keyword)
			throws Exception;

	@GetMapping(value = "/course/faculty/{facultyId}")
	public ResponseEntity<?> getCoursesByFacultyId(@Valid @PathVariable final String facultyId) throws Exception;

	@GetMapping(value = "/course/user")
	public ResponseEntity<?> getUserCourses(@RequestBody final List<String> courseIds,
			@RequestParam(required = false) final String sortBy,
			@RequestParam(required = false) final String sortAsscending) throws ValidationException;

	@GetMapping(value = "/course/filter/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> courseFilter(@RequestHeader(required = true) final String userId,
			@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize,
			@RequestBody final CourseFilterDto courseFilter)
			throws Exception;

	@GetMapping(value = "/course/autoSearch/{searchKey}")
	public ResponseEntity<?> autoSearchByCharacter(@PathVariable final String searchKey) throws Exception;

	@GetMapping(value = "/course/noResult/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<Object> getCourseNoResultRecommendation(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize, @RequestParam(required = true) final String facultyId,
			@RequestParam(required = true) final String countryId,
			@RequestParam(required = true) final String userCountry)
			throws ValidationException, InvokeException, NotFoundException;

	@GetMapping(value = "/course/keyword/recommendatation/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<Object> getCourseKeywordRecommendation(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize, @RequestParam(required = true) final String facultyId,
			@RequestParam(required = true) final String countryId, @RequestParam(required = true) final String levelId)
			throws ValidationException;

	@GetMapping(value = "/course/cheapest/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<Object> getCheapestCourse(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize, @RequestParam(required = true) final String facultyId,
			@RequestParam(required = true) final String countryId, @RequestParam(required = true) final String levelId,
			@RequestParam(required = true) final String cityId) throws ValidationException;

	@GetMapping(value = "/course/getCourseCountByLevel")
	public ResponseEntity<Object> getCourseCountByLevel();

	@GetMapping(value = "/course/names/distinct/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getDistinctCourses(@PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize, @RequestParam(required = false) final String name);

	@PostMapping(value = "/course/mobile/{instituteId}")
	public ResponseEntity<?> addCourseViaMobile(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId, @Valid @RequestBody CourseMobileDto courseMobileDto)
			throws Exception;

	@PutMapping(value = "/course/mobile/{courseId}")
	public ResponseEntity<?> updateCourseViaMobile(@RequestHeader("userId") final String userId,
			@PathVariable final String courseId, @Valid @RequestBody CourseMobileDto courseMobileDto) throws Exception;

	@GetMapping(value = "/course/mobile/{instituteId}")
	public ResponseEntity<?> getCourseViaMobile(@RequestHeader("userId") final String userId,
			@PathVariable final String instituteId,
			@RequestParam(name = "faculty_id", required = true) final String facultyId,
			@RequestParam(name = "status", required = true) final boolean status) throws Exception;

	@GetMapping(value = "/course/public/mobile/{instituteId}")
	public ResponseEntity<?> getCourseViaMobile(@PathVariable final String instituteId,
			@RequestParam(name = "faculty_id", required = true) final String facultyId) throws Exception;

	@PutMapping(value = "/course/status/{courseId}")
	public ResponseEntity<?> changeStatus(@RequestHeader("userId") final String userId,
			@PathVariable final String courseId, @RequestParam(name = "status", required = true) final boolean status)
			throws Exception;

	@GetMapping(value = "/course/institute/pageNumber/{pageNumber}/pageSize/{pageSize}/{instituteId}")
	public ResponseEntity<?> getCourseByInstituteId(@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@PathVariable final String instituteId) throws NotFoundException;

	@PostMapping(value = "/course/nearest", produces = "application/json")
	public ResponseEntity<?> getNearestCourseList(@RequestBody final AdvanceSearchDto courseSearchDto) throws Exception;

	@PostMapping(value = "/course/pageNumber/{pageNumber}/pageSize/{pageSize}/{countryName}")
	public ResponseEntity<?> getCourseByCountryName(@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@PathVariable String countryName) throws NotFoundException;

	@PostMapping("/course/courseIds")
	public ResponseEntity<?> getCourseByIds(@RequestBody List<String> courseIds);

	@GetMapping("/course/recommendation/{courseId}")
	public ResponseEntity<?> getRecommendateCourses(@PathVariable String courseId) throws ValidationException;

	@GetMapping("/course/related/{courseId}")
	public ResponseEntity<?> getRelatedCourses(@PathVariable String courseId) throws ValidationException;

	@GetMapping("/course/count/{instituteId}")
	public ResponseEntity<?> getCourseCountByInstituteId(@PathVariable String instituteId) throws ValidationException;

	// api just according to the form requirements
	@PostMapping("/institute/{instituteId}/course/basic/info")
	ResponseEntity<?> saveBasicCourse(@RequestHeader(value = "userId", required = true) String userId,
			@PathVariable String instituteId, @Valid @RequestBody CourseRequest course)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException;

	// api just according to the form requirements
	@PutMapping("/institute/{instituteId}/course/basic/info/{id}")
	ResponseEntity<?> updateBasicCourse(@RequestHeader(value = "userId", required = true) String userId,
			@PathVariable String instituteId, @Valid @RequestBody CourseRequest course, @PathVariable String id)
			throws ValidationException, NotFoundException, ForbiddenException, InvokeException;

	@PutMapping("/course/procedure_id")
	public ResponseEntity<?> updateProcedureIdInCourse(
			@RequestParam(name = "course_ids", required = true) List<String> courseIds,
			@RequestParam(name = "student_type", required = true) String studentType,
			@RequestParam(name = "procedure_id", required = true) String procedureId);

	@PutMapping("/course/procedure_id/institute_id")
	public ResponseEntity<?> updateProcedureIdInCourseByInstituteId(
			@RequestParam(name = "institute_id", required = true) String instituteId,
			@RequestParam(name = "student_type", required = true) String studentType,
			@RequestParam(name = "procedure_id", required = true) String procedureId);

	@PostMapping(value = "/course/draft/publish/{courseId}")
	public ResponseEntity<?> publishDraftCourse(@RequestHeader(value = "userId", required = true) String userId, @PathVariable final String courseId);

	@GetMapping(value = "/course/draft/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getDraftCourses(@RequestHeader(value = "userId", required = true) String userId,
			@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize,
			@RequestParam(name = "name", required = true) String name, @RequestParam(name = "institute_id", required = true) String instituteId);

	@DeleteMapping(value = "/course/draft/{courseId}")
	public ResponseEntity<?> discardDraftCourse(@RequestHeader(value = "userId", required = true) String userId, @PathVariable final String courseId);
}