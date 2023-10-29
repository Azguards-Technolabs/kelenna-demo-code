package com.azguards.app.controller.v1;

import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.constant.FaqEntityType;
import com.azguards.app.dto.FaqRequestDto;
import com.azguards.app.dto.FaqResponseDto;
import com.azguards.app.endpoint.FaqEndpoint;
import com.azguards.app.processor.FaqProcessor;
import com.azguards.common.lib.dto.CountDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.Utils;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@RestController("faqControllerV1")
@Slf4j
public class FaqController implements FaqEndpoint {

	@Autowired
	private FaqProcessor faqProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<Object> addFaq(String userId, @Valid FaqRequestDto faqRequestDto) throws ValidationException {
		log.debug("inside FaqController.addFaq");
		validateFaqEntityType(faqRequestDto.getEntityType());
		faqProcessor.addFaq(userId, faqRequestDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faq.added"))
				.create();
	}

	@Override
	public ResponseEntity<Object> updateFaq(String userId, String faqId, @Valid FaqRequestDto faqRequestDto)
			throws ValidationException {
		log.debug("inside FaqController.updateFaq");
		validateFaqEntityType(faqRequestDto.getEntityType());
		faqProcessor.updateFaq(userId, faqId, faqRequestDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faq.updated"))
				.create();
	}

	@DeleteMapping("/{faqId}")
	public ResponseEntity<Object> deleteFaq(@PathVariable String faqId) throws ValidationException {
		log.debug("inside FaqController.deleteFaq");
		faqProcessor.deleteFaq(faqId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setMessage(messageTranslator.toLocale("faq.deleted"))
				.create();
	}

	@Override
	public ResponseEntity<Object> getFaqList(Integer pageNumber, Integer pageSize, String entityId,
			String faqCategoryId, String faqSubCategoryId, String searchKeyword) throws ValidationException {

		log.debug("inside FaqController.getFaqList");
		if (pageNumber < 1) {
			log.error(messageTranslator.toLocale("faq.not_zero.page_number",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq.not_zero.page_number"));
		}

		if (pageSize < 1) {
			log.error(messageTranslator.toLocale("faq.not_zero.page_size"),Locale.US);
			throw new ValidationException(messageTranslator.toLocale("faq.not_zero.page_size"));
		}
		PaginationResponseDto faqPaginationResponseDto = faqProcessor.getFaqList(entityId, pageNumber, pageSize,
				faqCategoryId, faqSubCategoryId, searchKeyword);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(faqPaginationResponseDto)
				.setMessage(messageTranslator.toLocale("faq.retrieved")).create();
	}

	@GetMapping("/{faqId}")
	public ResponseEntity<Object> getFaqDetail(@PathVariable String faqId) throws ValidationException {
		log.debug("inside FaqController.getFaqDetail");
		FaqResponseDto faqResponseDto = faqProcessor.getFaqDetail(faqId);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(faqResponseDto)
				.setMessage(messageTranslator.toLocale("faq.retrieved")).create();
	}

	private void validateFaqEntityType(String entityType) throws ValidationException {
		if (!EnumUtils.isValidEnum(FaqEntityType.class, entityType)) {
			log.error(messageTranslator.toLocale("faq.entity.type", Utils.getEnumNamesAsString(FaqEntityType.class),Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq.entity.type", Utils.getEnumNamesAsString(FaqEntityType.class)));
		}
	}

	@Override
	public ResponseEntity<Object> getFaqCountByEntityId(String entityId, String entityType) throws ValidationException {
		log.debug("inside FaqController.getFaqCountByEntityId");
		CountDto countDto = faqProcessor.getFaqCountByEntityId(entityId,entityType);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(countDto)
				.setMessage(messageTranslator.toLocale("faq.retrieved")).create();
	}

	
}
