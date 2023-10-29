package com.azguards.app.controller.v1;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.FaqCategoryDto;
import com.azguards.app.endpoint.FaqCategoryEndpoint;
import com.azguards.app.processor.FaqCategoryProcessor;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("faqCategroyControllerV1")
@Slf4j
public class FaqCategroyController implements FaqCategoryEndpoint {

	@Autowired
	private FaqCategoryProcessor faqCategoryProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> addFaqCategory(String userId, FaqCategoryDto faqCategory) throws ValidationException {
		log.info("inside FaqCategroyController.addFaqCategory");
		faqCategoryProcessor.addFaqCategory(userId, faqCategory);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_categroy.added")).create();
	}

	@Override
	public ResponseEntity<?> updateFaqCategory(String userId, String faqCategoryId, @Valid FaqCategoryDto faqCategory)
			throws ValidationException {
		log.info("inside FaqCategroyController.updateFaqCategory");
		faqCategoryProcessor.updateFaqCategory(userId, faqCategoryId, faqCategory);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_categroy.updated")).create();
	}

	@Override
	public ResponseEntity<?> deleteFaqCategory(String faqCategoryId) throws ValidationException {
		log.info("inside FaqCategroyController.deleteFaqCategory");
		faqCategoryProcessor.deleteFaqCategory(faqCategoryId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_categroy.deleted")).create();
	}

	@Override
	public ResponseEntity<?> getFaqCategoryList(Integer pageNumber, Integer pageSize) throws ValidationException {
		log.info("inside FaqCategroyController.getFaqCategoryList");
		if (pageNumber < 1) {
			log.error(messageTranslator.toLocale("faq-category.not_zero.page_number"),Locale.US);
			throw new ValidationException(messageTranslator.toLocale("faq-category.not_zero.page_number"));
		}

		if (pageSize < 1) {
			log.error(messageTranslator.toLocale("faq-catagory.not_zero.page_size",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq-catagory.not_zero.page_size"));
		}
		return new GenericResponseHandlers.Builder()
				.setData(faqCategoryProcessor.getFaqCategories(pageNumber, pageSize)).setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("faq_categroy.retrieved")).create();
	}

	@Override
	public ResponseEntity<?> getFaqCategoryDetail(String faqCategoryId) throws ValidationException {
		log.info("inside FaqCategroyController.getFaqCategoryDetail");
		return new GenericResponseHandlers.Builder().setData(faqCategoryProcessor.getFaqCategory(faqCategoryId))
				.setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faq_categroy.retrieved")).create();
	}
}
