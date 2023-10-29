package com.azguards.app.jobs;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolationException;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import com.azguards.app.bean.Faculty;
import com.azguards.app.dto.uploader.FacultyCSVDto;
import com.azguards.common.lib.exception.UploaderException;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j(topic = "faculty_import")
public class FacultyUploadBatchConfig {

	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

	@Bean("facultyItemReader")
	@StepScope
	public FacultyItemReader reader(@Value("#{jobParameters['csv-file']}") String fileName)
			throws IOException, com.azguards.common.lib.exception.IOException, UploaderException {
		return new FacultyItemReader(fileName);
	}

	@Bean("facultyItemProcessor")
	public ItemProcessor<FacultyCSVDto, Faculty> processor() {
		return new FacultyItemProcessor();
	}

	@Bean("facultyItemWriter")
	@StepScope
	public JpaItemWriter<Faculty> writer(@Autowired EntityManagerFactory emf) {
		JpaItemWriter<Faculty> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}

	@Bean("facultyStep")
	public Step facultyStep(StepBuilderFactory stepBuilderFactory,
			@Qualifier("facultyItemReader") ItemReader<FacultyCSVDto> reader,
			@Qualifier("facultyItemWriter") ItemWriter<Faculty> writer,
			@Qualifier("facultyItemProcessor") ItemProcessor<FacultyCSVDto, Faculty> processor,@Qualifier("facultyItemWriteListner") ItemWriteListener<Faculty> listner,
			HibernateTransactionManager hibernateTransactionManager, SkipAnyFailureSkipPolicy skipPolicy) {
		return stepBuilderFactory.get("facultyStep").<FacultyCSVDto, Faculty>chunk(batchSize).reader(reader).faultTolerant()
				.skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class).processor(processor).writer(writer).listener(listner)
				.transactionManager(hibernateTransactionManager).build();
	}
	
	@Bean("importFacultyJob")
	public Job importFacultyJob(JobBuilderFactory jobs, @Qualifier("facultyStep") Step facultyStep, @Qualifier("facultyJobExecutionListner") JobExecutionListener facultyJobExecutionListener ) {
		return jobs.get("importFacultyJob").incrementer(new RunIdIncrementer()).listener(facultyJobExecutionListener).flow(facultyStep)
				.end().build();
	}
	
	@Bean("facultyJobExecutionListner")
	public JobExecutionListener facultyJobExecutionListner () {
		return new FacultyJobExecutionListner();
	}
	
	@Bean("facultyItemWriteListner")
	@StepScope
	public ItemWriteListener<Faculty> facultyItemWriteListner() {
		return new FacultyItemWriteListner();
	}
}
