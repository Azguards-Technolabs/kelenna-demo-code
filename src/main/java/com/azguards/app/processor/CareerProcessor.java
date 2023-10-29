package com.azguards.app.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.Careers;
import com.azguards.app.dao.CareerDao;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.dto.institute.CareerDto;
import com.azguards.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CareerProcessor {

	@Autowired
	private CareerDao careerDao;

	@Autowired
	private ModelMapper modelMapper;

	public PaginationResponseDto findCareerByName(String name, Integer pageNumber, Integer pageSize) {
		log.info("Inside CareerProcessor.findCareerByName() method searching name: {}, pageNumber:{}, pageSize:{}",
				name, pageNumber, pageSize);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("going to db to fetch careeres");
		Page<Careers> careerPage = careerDao.findByNameContainingIgnoreCase(name, pageable);
		Long totalCount = careerPage.getTotalElements();
		log.debug("returned from db getting {} records.", careerPage.getNumberOfElements());
		List<CareerDto> careerDtos = careerPage.getContent().stream().map(e -> modelMapper.map(e, CareerDto.class))
				.collect(Collectors.toList());

		log.info("Calculating pagination based on startIndex, pageSize and pageNumber");
		Long startIndex = PaginationUtil.getStartIndex(pageNumber, pageSize);
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, pageSize, totalCount);
		return new PaginationResponseDto(careerDtos, totalCount, paginationUtilDto.getPageNumber(),
				paginationUtilDto.isHasPreviousPage(), paginationUtilDto.isHasNextPage(),
				paginationUtilDto.getTotalPages());
	}
}
