package com.azguards.app.jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.handler.PublishSystemEventHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElasticCourseExportItemWriter implements ItemWriter<CourseSyncDTO> {

	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;

	@Override
	public void write(List<? extends CourseSyncDTO> items) throws Exception {
		log.info("inside ElasticCourseExportItemWriter.write");
		List<CourseSyncDTO> courseDTOElasticSearchs = new ArrayList<>();
		courseDTOElasticSearchs.addAll(items);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("elasticSearchProcessor.saveCourseOnElasticSearch");
		publishSystemEventHandler.syncCourses(courseDTOElasticSearchs);
		stopWatch.stop();
		log.info("elasticSearchProcessor.saveCourseOnElasticSearch process completed in :::  {}",
				stopWatch.getTotalTimeSeconds());
	}
}
