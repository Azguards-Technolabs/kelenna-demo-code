package com.azguards.app.jobs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolationException;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteType;
import com.azguards.app.dto.InstituteTypeDto;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j(topic = "institute_type_import")
public class InstituteTypeUploadBatchConfig {
	
	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

	private String executionId;
	
	@Autowired
	private LogFileProcessor logFileProcessor;

	
    @Bean
    @StepScope
    public InstituteTypeItemReader instituteTypeItemReader(@Value("#{jobParameters['csv-file']}") String fileName,
    		@Value("#{jobParameters['execution-id']}") String executionId) throws IOException {
    	this.executionId = executionId;
    	String header = "Institute Type,Country Name,Exception";
    	logFileProcessor.appendToLogFile(executionId, Arrays.asList(header));
        return new InstituteTypeItemReader(fileName);
    }

    @Bean
    public InstituteTypeItemProcessor instituteTypeItemProcessor() {
        return new InstituteTypeItemProcessor();
    }


    @Bean
    public JpaItemWriter<InstituteType> instituteTypeWriter(@Autowired EntityManagerFactory emf) {
        JpaItemWriter<InstituteType> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
 
    @Bean("importInstituteTypeJob")
    public Job importInstituteTypeJob(JobBuilderFactory jobs, @Qualifier("instituteTypeStep1") Step s1, JobExecutionListener instituteTypeJobExecutionListener) {
        return jobs.get("importInstituteTypeJob")
                .incrementer(new RunIdIncrementer())
                .listener(instituteTypeJobExecutionListener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean("instituteTypeStep1")
    public Step step1InstituteType(StepBuilderFactory stepBuilderFactory, InstituteTypeItemReader instituteTypeItemReader,
            ItemWriter<InstituteType> instituteTypeWriter, InstituteTypeItemProcessor instituteTypeItemProcessor,
            HibernateTransactionManager hibernateTransactionManager,
            SkipAnyFailureSkipPolicy skipPolicy,
            ItemWriteListener<Institute> instituteTypeItemWriteListener) {
        return stepBuilderFactory.get("step1")
                .<InstituteTypeDto, InstituteType> chunk(batchSize)
                .reader(instituteTypeItemReader)
                .faultTolerant().skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class)
                .processor(instituteTypeItemProcessor)
                .writer(instituteTypeWriter)
                .listener(instituteTypeItemWriteListener)
                .transactionManager(hibernateTransactionManager)
                .build();
    }
    
    
    @Bean
    public ItemWriteListener<InstituteType> instituteTypeItemWriteListener() {
    	return new ItemWriteListener<InstituteType>() {
    		
			@Override
			public void beforeWrite(List<? extends InstituteType> items) {
				log.debug("Before writing institute item to db {} ",items.size());
			}

			@Override
			public void afterWrite(List<? extends InstituteType> instituteTypesList) {
				log.debug("instituteTypes imported {}",instituteTypesList.size());
			}

			@Override
			public void onWriteError(Exception exception, List<? extends InstituteType> items) {
				List<String> errors = new ArrayList<>();
				items.stream().forEach(item -> 
					errors.add(String.format("%s,%s,%s",item.getName(),item.getCountryName(), ExceptionUtil.findCauseUsingPlainJava(exception).getMessage()))
				);
				try {
					logFileProcessor.appendToLogFile(executionId, errors);
				} catch (IOException e) {
					log.error("Exception from onWriteError Event Listener");
				}
			}
		};
    }
    
    @Bean
    public JobExecutionListener instituteTypeJobExecutionListener() {
    	return new JobExecutionListener() {
			
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("Job started to import Institutes");
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("Institute Type import done.");
				logFileProcessor.sendFailureEmail(executionId, "UPLOADER: Institute Type batch upload Failures");
			}
		};
    }
}