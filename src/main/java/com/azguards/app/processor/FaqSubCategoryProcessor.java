package com.azguards.app.processor;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.FaqCategory;
import com.azguards.app.bean.FaqSubCategory;
import com.azguards.app.dao.FaqCategoryDao;
import com.azguards.app.dao.FaqSubCategoryDao;
import com.azguards.app.dto.FaqSubCategoryRequestDto;
import com.azguards.app.dto.FaqSubCategoryResponseDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FaqSubCategoryProcessor {

	@Autowired
	private FaqSubCategoryDao faqSubCategoryDao;

	@Autowired
	private FaqCategoryDao faqCategoryDao;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	public void addFaqSubCategory(final String userId, final FaqSubCategoryRequestDto faqSubCategoryRequestDto)
			throws ValidationException {
		log.info("inside FaqSubCategoryProcessor.addFaqSubCategory");

		FaqSubCategory faqSubCategory = modelMapper.map(faqSubCategoryRequestDto, FaqSubCategory.class);
		setFaqCategoryInFaqSubCategory(faqSubCategory, faqSubCategoryRequestDto);
		faqSubCategory.setCreatedBy(userId);
		faqSubCategory.setUpdatedBy(userId);
		faqSubCategory.setCreatedOn(new Date());
		faqSubCategory.setUpdatedOn(new Date());

		faqSubCategoryDao.saveOrUpdate(faqSubCategory);
	}

	public void updateFaqSubCategory(final String userId, final String faqSubCategoryId,
			final FaqSubCategoryRequestDto faqSubCategoryRequestDto) throws ValidationException {
		log.debug("inside FaqSubCategoryProcessor.updateFaqSubCategory");

		FaqSubCategory existingFaqSubCategory = getFaqSubCategoryById(faqSubCategoryId);
		existingFaqSubCategory.setName(faqSubCategoryRequestDto.getName());
		if (!StringUtils.equals(existingFaqSubCategory.getFaqCategory().getId(),
				faqSubCategoryRequestDto.getFaqCategoryId())) {
			setFaqCategoryInFaqSubCategory(existingFaqSubCategory, faqSubCategoryRequestDto);
		}
		existingFaqSubCategory.setUpdatedBy(userId);
		existingFaqSubCategory.setUpdatedOn(new Date());
		faqSubCategoryDao.saveOrUpdate(existingFaqSubCategory);

	}

	@Transactional
	public FaqSubCategoryResponseDto getFaqSubCategory(final String faqSubCategoryId) throws ValidationException {
		log.debug("inside FaqSubCategoryProcessor.getFaqSubCategory");
		return modelMapper.map(getFaqSubCategoryById(faqSubCategoryId), FaqSubCategoryResponseDto.class);
	}

	@Transactional
	public PaginationResponseDto getFaqSubCategories(final String faqCategoryId, final Integer pageNumber,
			final Integer pageSize) {
		log.debug("inside FaqSubCategoryProcessor.getFaqSubCategories");
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

		log.info("calling db to fetch faq caregories for pageNumber: {} and pageSize: {}", pageNumber - 1, pageSize);
		Page<FaqSubCategory> faqSubCategoriesPage = faqSubCategoryDao.findByCategoryId(faqCategoryId, pageable);

		int totalRecords = ((Long) faqSubCategoriesPage.getTotalElements()).intValue();

		List<FaqSubCategoryResponseDto> faqSubCategoryDtos = faqSubCategoriesPage.getContent().stream()
				.map(e -> modelMapper.map(e, FaqSubCategoryResponseDto.class)).collect(Collectors.toList());

		return PaginationUtil.calculatePaginationAndPrepareResponse(PaginationUtil.getStartIndex(pageNumber, pageSize),
				pageSize, totalRecords, faqSubCategoryDtos);
	}

	public void deleteFaqSubCategory(final String faqSubCategoryId) {
		faqSubCategoryDao.deleteById(faqSubCategoryId);
	}

	private void setFaqCategoryInFaqSubCategory(FaqSubCategory faqSubCategory,
			FaqSubCategoryRequestDto faqSubCategoryRequestDto) throws ValidationException {
		FaqCategory faqCategory = getFaqCategoryById(faqSubCategoryRequestDto.getFaqCategoryId());
		faqSubCategory.setFaqCategory(faqCategory);
	}

	private FaqCategory getFaqCategoryById(String faqCategoryId) throws ValidationException {
		Optional<FaqCategory> faqCategoryOptional = faqCategoryDao.getById(faqCategoryId);
		if (!faqCategoryOptional.isPresent()) {
			log.error(messageTranslator.toLocale("faq.id.notfound",faqCategoryId,Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq.id.notfound",faqCategoryId));
		}
		return faqCategoryOptional.get();
	}

	private FaqSubCategory getFaqSubCategoryById(String faqSubCategoryId) throws ValidationException {
		Optional<FaqSubCategory> faqSubCategoryOptional = faqSubCategoryDao.getById(faqSubCategoryId);
		if (!faqSubCategoryOptional.isPresent()) {
			log.error(messageTranslator.toLocale("faq.sub.category.id.notfound",faqSubCategoryId,Locale.US));
			throw new ValidationException(messageTranslator.toLocale("faq.sub.category.id.notfound",faqSubCategoryId));
		}
		return faqSubCategoryOptional.get();
	}
}
