package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.dao.CareerTestDao;
import com.azguards.app.dto.CareerJobSkillDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class JobSkillProcessor {
	
	@Autowired
	private CareerTestDao careerTestDao;
	
	@Autowired
	CareerTestProcessor careerTestProcessor;

	public PaginationResponseDto<List<CareerJobSkillDto>> getJobSkills(String userId, Integer pageNumber,
			Integer pageSize, List<String> jobNames) {
		log.debug("Inside getJobSkills() method");
		List<CareerJobSkillDto> careerJobSkillDtos = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		log.info("Extracting job skills from DB for jobNames ", jobNames);

		Page<CareerJobSkill> jobSkillsFromDB = careerTestDao.getJobSkills(jobNames, pageable);
		if (!CollectionUtils.isEmpty(jobSkillsFromDB.getContent())) {
			log.info("Career JobSkills fetched from DB, start iterating data to make final response");
			jobSkillsFromDB.getContent().stream().forEach(careerJobSkill -> {
				CareerJobSkillDto careerJobSkillDto = new CareerJobSkillDto(careerJobSkill.getId(),
						careerJobSkill.getSkill(), careerJobSkill.getDescription(),
						careerJobSkill.getCareerJobs().getId(),
						false);
				careerJobSkillDtos.add(careerJobSkillDto);
			});
		}
		return PaginationUtil.calculatePaginationAndPrepareResponse(jobSkillsFromDB, careerJobSkillDtos);
	}
}
