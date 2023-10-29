package com.azguards.app.controller.v1;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.FaqSubCategoryRequestDto;
import com.azguards.app.endpoint.FaqSubCategoryEndpoint;
import com.azguards.app.processor.FaqSubCategoryProcessor;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("faqSubCategroyControllerV1")
@Slf4j
public class FaqSubCategoryController implements FaqSubCategoryEndpoint {

	@Autowired
	private FaqSubCategoryProcessor faqSubCategoryProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	

	@Override
	public ResponseEntity<?> addFaqSubCategory(String userId, FaqSubCategoryRequestDto faqSubCategoryRequestDto)
			throws ValidationException {
		log.info("inside FaqSubCategoryController.addFaqSubCategory");
		faqSubCategoryProcessor.addFaqSubCategory(userId, faqSubCategoryRequestDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_sub_categroy.added")).create();
	}

	@Override
	public ResponseEntity<?> updateFaqSubCategory(String userId, String faqSubCategoryId,
			@Valid FaqSubCategoryRequestDto faqSubCategoryRequestDto) throws ValidationException {
		log.info("inside FaqSubCategoryController.updateFaqSubCategory");
		faqSubCategoryProcessor.updateFaqSubCategory(userId, faqSubCategoryId, faqSubCategoryRequestDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_sub_categroy.updated")).create();
	}

	@Override
	public ResponseEntity<?> deleteFaqSubCategory(String faqSubCategoryId) throws ValidationException {
		log.info("inside FaqSubCategoryController.deleteFaqSubCategory");
		faqSubCategoryProcessor.deleteFaqSubCategory(faqSubCategoryId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_sub_categroy.deleted")).create();
	}

	@Override
	public ResponseEntity<?> getFaqSubCategoryList(Integer pageNumber, Integer pageSize, String faqCategoryId)
			throws ValidationException {
		log.info("inside FaqSubCategoryController.getFaqSubCategoryList");
		if (pageNumber < 1) {
			log.error(messageTranslator.toLocale("page_number.not_zero",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("page_number.not_zero"));
		}

		if (pageSize < 1) {
			log.error(messageTranslator.toLocale("page_size.not_zero"),Locale.US);
			throw new ValidationException(messageTranslator.toLocale("page_size.not_zero"));
		}
		return new GenericResponseHandlers.Builder()
				.setData(faqSubCategoryProcessor.getFaqSubCategories(faqCategoryId, pageNumber, pageSize))
				.setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faq_sub_categroy.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getFaqSubCategoryDetail(String faqSubCategoryId) throws ValidationException {
		log.info("inside FaqSubCategoryController.getFaqSubCategoryDetail");
		return new GenericResponseHandlers.Builder()
				.setData(faqSubCategoryProcessor.getFaqSubCategory(faqSubCategoryId)).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_sub_categroy.retrieved")).create();
	}
}
