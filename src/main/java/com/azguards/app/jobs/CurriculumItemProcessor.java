package com.azguards.app.jobs;

import org.springframework.batch.item.ItemProcessor;

import com.azguards.app.bean.CourseCurriculum;
import com.azguards.common.lib.dto.institute.CourseCurriculumDto;
import com.azguards.common.lib.util.DateUtil;

public class CurriculumItemProcessor implements ItemProcessor<CourseCurriculumDto, CourseCurriculum> {

	@Override
	public CourseCurriculum process(CourseCurriculumDto courseCurriculumDto) throws Exception {
		CourseCurriculum courseCurriculum = new CourseCurriculum();
		courseCurriculum.setName(courseCurriculumDto.getName());
		courseCurriculum.setIsActive(true);
		courseCurriculum.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		courseCurriculum.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		courseCurriculum.setCreatedBy("AUTO");
		courseCurriculum.setUpdatedBy("AUTO");
		return courseCurriculum;
	}
	
}