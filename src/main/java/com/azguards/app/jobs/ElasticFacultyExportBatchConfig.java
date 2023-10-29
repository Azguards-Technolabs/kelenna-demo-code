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

import com.azguards.app.bean.Faculty;
import com.azguards.common.lib.dto.institute.FacultyDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;

@Configuration
@EnableBatchProcessing
public class ElasticFacultyExportBatchConfig {
	
	@StepScope
	@Bean("facultyJpaItemReader")
	public JpaPagingItemReader<Faculty> facultyJpaItemReader(@Autowired EntityManagerFactory emf) {
	    JpaPagingItemReader<Faculty> itemReader = new JpaPagingItemReader<>();
	    itemReader.setEntityManagerFactory(emf);
	    itemReader.setQueryString("SELECT F FROM Faculty F"); 
	    itemReader.setPageSize(1);
	    return itemReader;
	}
	
	@Bean
	public ElasticFacultyExportItemProcessor elasticFacultyExportItemProcessor() {
		return new ElasticFacultyExportItemProcessor();
	}

	@Bean("elasticFacultyExportItemWriter")
	public ElasticFacultyExportItemWriter elasticFacultyExportItemWriter() {
		return new ElasticFacultyExportItemWriter();
	}

	@Bean("exportFacultyToElastic")
	public Job exportFacultyToElasticJob(JobBuilderFactory jobs ,@Qualifier("exportFacultyToElasticStep") Step exportFacultyToElasticStep) {
		return jobs.get("exportFacultyToElastic").incrementer(new RunIdIncrementer())
				.start(exportFacultyToElasticStep)
				.build();
	}

	@Bean("exportFacultyToElasticStep")
	public Step exportFacultyToElasticStep(StepBuilderFactory stepBuilderFactory,@Qualifier("facultyJpaItemReader") ItemReader<Faculty> facultyJpaItemReader,
			@Qualifier("elasticFacultyExportItemWriter")  ElasticFacultyExportItemWriter elasticFacultyExportItemWriter, ElasticFacultyExportItemProcessor elasticFacultyExportItemProcessor,
			 SkipAnyFailureSkipPolicy skipPolicy) {
		return stepBuilderFactory.get("exportFacultyToElastic").<Faculty, FacultyDto>chunk(1).reader(facultyJpaItemReader)
				.faultTolerant().skipPolicy(skipPolicy).noRollback(InvokeException.class)
				.processor(elasticFacultyExportItemProcessor).writer(elasticFacultyExportItemWriter).build();
	}

}
