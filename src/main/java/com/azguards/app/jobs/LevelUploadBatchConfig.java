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

import com.azguards.app.bean.Level;
import com.azguards.common.lib.dto.institute.LevelDto;
import com.azguards.common.lib.exception.UploaderException;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j(topic = "level_import")
public class LevelUploadBatchConfig {
	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

	@Bean("levelItemReader")
	@StepScope
	public LevelItemReader reader(@Value("#{jobParameters['csv-file']}") String fileName)
			throws IOException, UploaderException, com.azguards.common.lib.exception.IOException {
		return new LevelItemReader(fileName);
	}

	@Bean("levelItemProcessor")
	public ItemProcessor<LevelDto, Level> processor() {
		return new LevelItemProcessor();
	}

	@Bean("levelItemWriter")
	@StepScope
	public JpaItemWriter<Level> writer(@Autowired EntityManagerFactory emf) {
		JpaItemWriter<Level> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}

	@Bean("importlevelJob")
	public Job importLevelJob(JobBuilderFactory jobs, @Qualifier("levelStep") Step levelStep , @Qualifier("levelJobExecutionListener") JobExecutionListener levelJobExecutionListener) {
		return jobs.get("importlevelJob").incrementer(new RunIdIncrementer()).listener(levelJobExecutionListener).flow(levelStep)
				.end().build();
	}

	@Bean("levelStep")
	public Step levelStep(StepBuilderFactory stepBuilderFactory,
			@Qualifier("levelItemReader") ItemReader<LevelDto> reader,
			@Qualifier("levelItemWriter") ItemWriter<Level> writer,
			@Qualifier("levelItemProcessor") ItemProcessor<LevelDto, Level> processor,@Qualifier("levelItemWriterListner") ItemWriteListener<Level> levelItemWriteListner,
			HibernateTransactionManager hibernateTransactionManager, SkipAnyFailureSkipPolicy skipPolicy) {
		return stepBuilderFactory.get("levelStep").<LevelDto, Level>chunk(batchSize).reader(reader).faultTolerant()
				.skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class).processor(processor).writer(writer).listener(levelItemWriteListner)
				.transactionManager(hibernateTransactionManager).build();
	}
	
	@Bean("levelItemWriterListner")
	@StepScope
	public ItemWriteListener<Level> levelItemWriteListner() {
		return new LevelItemWriteListner();
	}
	
	@Bean("levelJobExecutionListener")
	public JobExecutionListener levelJobExecutionListener () {
		return new LevelJobExecutionListner();
	}
}
