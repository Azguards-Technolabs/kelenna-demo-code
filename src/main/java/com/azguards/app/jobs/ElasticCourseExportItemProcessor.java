package com.azguards.app.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.azguards.app.bean.Course;
import com.azguards.app.processor.ConversionProcessor;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElasticCourseExportItemProcessor implements ItemProcessor<Course, CourseSyncDTO> {

	@Autowired
	private ConversionProcessor conversionProcessor;

	@Override
	public CourseSyncDTO process(Course course) throws Exception {
		log.info("inside ElasticCourseExportBatchConfig.exportCourseToElasticStep");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("DTOUtills.convertToCourseDTOElasticSearchEntity");
		try {
			return conversionProcessor.convertToCourseSyncDTOSyncDataEntity(course);
		} finally {
			stopWatch.stop();
			log.info("DTOUtills.convertToCourseDTOElasticSearchEntity process completed in ::: {}",
					stopWatch.getTotalTimeSeconds());
		}

	}
}