package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.common.lib.exception.ValidationException;
import com.azguards.app.dto.HelpAnswerDto;
import com.azguards.app.dto.HelpCategoryDto;
import com.azguards.app.dto.HelpDto;
import com.azguards.app.dto.HelpSubCategoryDto;
import com.azguards.common.lib.exception.NotFoundException;

@RequestMapping("/api/v1/help")
public interface HelpInterface {

	@GetMapping(value = "/pageNumber/{pageNumber}/pageSize/{pageSize}", produces = "application/json")
	public ResponseEntity<?> getAll(@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize)
			throws Exception;

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> save(@Valid @RequestBody final HelpDto helpDto, @RequestHeader final String userId)
			throws Exception;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable final String id) throws Exception;

	@GetMapping(value = "/by/category/{id}", produces = "application/json")
	public ResponseEntity<?> getHelpByCategoryId(@PathVariable final String id) throws Exception;

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable final String id, @RequestBody final HelpDto helpDto,
			@RequestHeader final String userId);

	@PostMapping(value = "/category", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> saveCategory(@Valid @RequestBody final HelpCategoryDto categoryDto) throws Exception;

	@GetMapping(value = "/category/{id}", produces = "application/json")
	public ResponseEntity<?> getCatgeory(@PathVariable final String id) throws Exception;

	@PostMapping(value = "/subCategory", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> saveSubCategory(@Valid @RequestBody final HelpSubCategoryDto subCategoryDto)
			throws Exception;

	@GetMapping(value = "/subCategory/{id}", produces = "application/json")
	public ResponseEntity<?> getSubCatgeory(@PathVariable final String id) throws Exception;

	@GetMapping(value = "/category/{categoryId}/subCategory/pageNumber/{pageNumber}/pageSize/{pageSize}", produces = "application/json")
	public ResponseEntity<?> getSubCategoryByCategory(@PathVariable final String categoryId,
			@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize) throws Exception;

	@GetMapping(value = "/subCategory/count", produces = "application/json")
	public ResponseEntity<?> getSubCategoryCount() throws Exception;

	@PostMapping("/answer")
	public ResponseEntity<?> saveAnswer(@RequestParam(name = "file", required = false) final MultipartFile file,
			@ModelAttribute final HelpAnswerDto helpAnswerDto) throws Exception;

	@GetMapping(value = "/answer/{helpId}", produces = "application/json")
	public ResponseEntity<?> getAnswerByHelpId(@PathVariable final String helpId) throws Exception;

	@GetMapping(value = "/category/pageNumber/{pageNumber}/pageSize/{pageSize}", produces = "application/json")
	public ResponseEntity<?> getCategory(@PathVariable final Integer pageNumber, @PathVariable final Integer pageSize)
			throws Exception;

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@Valid @PathVariable final String id) throws Exception;

	@GetMapping(value = "/status/{id}", produces = "application/json")
	public ResponseEntity<?> updateStatus(@PathVariable final String id,
			@RequestHeader(required = false) final String userId, @RequestParam final String status,
			@RequestParam(required = false) final String assignedUserId) throws Exception;

	@GetMapping(value = "/filter", produces = "application/json")
	public ResponseEntity<?> filter(@RequestParam(required = false) final String status,
			@RequestParam(required = false) final String mostRecent, @RequestParam final String categoryId)
			throws Exception;

	@GetMapping(value = "/user/pageNumber/{pageNumber}/pageSize/{pageSize}", produces = "application/json")
	public ResponseEntity<?> getUserHelpList(@RequestHeader final String userId, @PathVariable final Integer pageNumber,
			@PathVariable final Integer pageSize,
			@RequestParam(name = "isArchive", required = false) final boolean isArchive) throws Exception;

	@PutMapping("/{id}/isFavourite/{isFavourite}")
	public ResponseEntity<?> setIsFavourite(@RequestHeader(value = "userId") final String userId,
			@PathVariable(value = "id") final String id, @PathVariable(value = "isFavourite") final boolean isFavourite)
			throws NotFoundException;

	@PostMapping("/relatedQuestions")
	public ResponseEntity<?> getOptionOnUserSearch(@RequestHeader(value = "userId") final String userId,
			@RequestBody(required = true) final String searchString) throws ValidationException;
}
