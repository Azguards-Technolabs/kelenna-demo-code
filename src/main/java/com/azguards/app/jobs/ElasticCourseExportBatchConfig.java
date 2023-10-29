package com.azguards.app.jobs;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azguards.app.bean.Course;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class ElasticCourseExportBatchConfig {
	
	@StepScope
	@Bean("courseJpaItemReader")
	public JpaPagingItemReader<Course> courseJpaItemReader(@Autowired EntityManagerFactory emf) {
		log.info("inside ElasticCourseExportBatchConfig.courseJpaItemReader");
	    JpaPagingItemReader<Course> courseItemReader = new JpaPagingItemReader<>();
	    courseItemReader.setEntityManagerFactory(emf);
	    courseItemReader.setQueryString("SELECT C FROM Course C");
	    courseItemReader.setPageSize(50);
	    return courseItemReader;
	}
	
	@Bean
	public ElasticCourseExportItemProcessor elasticCourseExportItemProcessor() {
		return new ElasticCourseExportItemProcessor();
	}

	@Bean("elasticCourseExportItemWriter")
	public ElasticCourseExportItemWriter elasticCourseExportItemWriter() {
		log.info("inside ElasticCourseExportBatchConfig.elasticCourseExportItemWriter");
		return new ElasticCourseExportItemWriter();
	}

	@Bean("exportCourseToElastic")
	public Job exportCourseToElasticJob(JobBuilderFactory jobs ,@Qualifier("exportCourseToElasticStep") Step exportCourseToElasticStep) {
		log.info("inside ElasticCourseExportBatchConfig.exportCourseToElasticJob");
		return jobs.get("exportCourseToElastic").incrementer(new RunIdIncrementer())
				.start(exportCourseToElasticStep)
				.build();
	}

	@Bean("exportCourseToElasticStep")
	public Step exportCourseToElasticStep(StepBuilderFactory stepBuilderFactory,@Qualifier("courseJpaItemReader") ItemReader<Course> courseJpaItemReader,
			@Qualifier("elasticCourseExportItemWriter")  ElasticCourseExportItemWriter elasticCourseExportItemWriter, ElasticCourseExportItemProcessor elasticCourseExportItemProcessor,
			 SkipAnyFailureSkipPolicy skipPolicy) {
		
		log.info("inside ElasticCourseExportBatchConfig.exportCourseToElasticStep");
		return stepBuilderFactory.get("exportCourseToElasticStep").<Course, CourseSyncDTO>chunk(50).reader(courseJpaItemReader)
				.faultTolerant().skipPolicy(skipPolicy).noRollback(InvokeException.class)
				.processor(elasticCourseExportItemProcessor).writer(elasticCourseExportItemWriter).build();
	}

}
