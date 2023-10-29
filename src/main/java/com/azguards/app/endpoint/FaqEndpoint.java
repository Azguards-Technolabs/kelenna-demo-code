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

import com.azguards.app.dto.FaqRequestDto;
import com.azguards.common.lib.exception.ValidationException;

@RequestMapping("/api/v1/faq")
public interface FaqEndpoint {

	@PostMapping
	public ResponseEntity<Object> addFaq(@RequestHeader("userId") String userId,
			@RequestBody @Valid FaqRequestDto faqRequestDto) throws ValidationException;

	@PutMapping("/{faqId}")
	public ResponseEntity<Object> updateFaq(@RequestHeader("userId") String userId, @PathVariable String faqId,
			@RequestBody @Valid FaqRequestDto faqRequestDto) throws ValidationException;

	@DeleteMapping("/{faqId}")
	public ResponseEntity<Object> deleteFaq(@PathVariable String faqId) throws ValidationException;

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<Object> getFaqList(@PathVariable Integer pageNumber, @PathVariable Integer pageSize,
			@RequestParam("entity_id") String entityId,
			@RequestParam(value = "faq_category_id", required = false) String faqCategoryId,
			@RequestParam(value = "faq_sub_category_id", required = false) String faqSubCategoryId,
			@RequestParam(value = "search_keyword", required = false) String searchKeyword) throws ValidationException;

	@GetMapping("/{faqId}")
	public ResponseEntity<Object> getFaqDetail(@PathVariable String faqId) throws ValidationException;
	
	@GetMapping("/count/entityType/{entityType}/entityId/{entityId}")
	public ResponseEntity<Object> getFaqCountByEntityId(@PathVariable String entityId,@PathVariable String entityType) throws ValidationException;

}
