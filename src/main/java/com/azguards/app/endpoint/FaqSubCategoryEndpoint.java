package com.azguards.app.endpoint;

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

import com.azguards.app.dto.FaqSubCategoryRequestDto;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/faq/sub/category")
public interface FaqSubCategoryEndpoint {

	@PostMapping
	public ResponseEntity<?> addFaqSubCategory(@RequestHeader("userId") String userId,
			@RequestBody @Valid FaqSubCategoryRequestDto faqSubCategoryRequestDto) throws ValidationException;

	@PutMapping("/{faqSubCategoryId}")
	public ResponseEntity<?> updateFaqSubCategory(@RequestHeader("userId") String userId,
			@PathVariable String faqSubCategoryId,
			@RequestBody @Valid FaqSubCategoryRequestDto faqSubCategoryRequestDto) throws ValidationException;

	@DeleteMapping("/{faqSubCategoryId}")
	public ResponseEntity<?> deleteFaqSubCategory(@PathVariable String faqSubCategoryId) throws ValidationException;

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getFaqSubCategoryList(@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@RequestParam("faq_category_id") String faqCategoryId) throws ValidationException;

	@GetMapping("/{faqSubCategoryId}")
	public ResponseEntity<?> getFaqSubCategoryDetail(@PathVariable String faqSubCategoryId) throws ValidationException;
}
