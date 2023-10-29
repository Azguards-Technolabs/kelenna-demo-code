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

import com.azguards.app.dto.FaqCategoryDto;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/faq/category")
public interface FaqCategoryEndpoint {

	@PostMapping
	public ResponseEntity<?> addFaqCategory(@RequestHeader("userId") String userId,
			@RequestBody @Valid FaqCategoryDto faqCategory) throws ValidationException;

	@PutMapping("/{faqCategoryId}")
	public ResponseEntity<?> updateFaqCategory(@RequestHeader("userId") String userId,
			@PathVariable String faqCategoryId, @RequestBody @Valid FaqCategoryDto faqCategory)
			throws ValidationException;

	@DeleteMapping("/{faqCategoryId}")
	public ResponseEntity<?> deleteFaqCategory(@PathVariable String faqCategoryId) throws ValidationException;

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<?> getFaqCategoryList(@PathVariable Integer pageNumber, @PathVariable Integer pageSize)
			throws ValidationException;

	@GetMapping("/{faqCategoryId}")
	public ResponseEntity<?> getFaqCategoryDetail(@PathVariable String faqCategoryId) throws ValidationException;
}
