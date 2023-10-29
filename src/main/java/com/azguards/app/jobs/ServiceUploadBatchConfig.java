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

import com.azguards.app.bean.Service;
import com.azguards.app.dto.uploader.ServiceCsvDto;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j(topic = "service_import")
public class ServiceUploadBatchConfig {
	
	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

	private String executionId;
	
	@Autowired
	private LogFileProcessor logFileProcessor;
	
    @Bean
    @StepScope
    public ServiceItemReader serviceItemReader(@Value("#{jobParameters['csv-file']}") String fileName,
    		@Value("#{jobParameters['execution-id']}") String executionId) throws IOException {
    	this.executionId = executionId;
    	String header = "Service Name,Exception";
    	logFileProcessor.appendToLogFile(executionId, Arrays.asList(header));
        return new ServiceItemReader(fileName);
    }

    @Bean
    public ServiceItemProcessor serviceItemProcessor() {
        return new ServiceItemProcessor();
    }


    @Bean
    public JpaItemWriter<Service> serviceWriter(@Autowired EntityManagerFactory emf) {
        JpaItemWriter<Service> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
 
    @Bean("importServiceJob")
    public Job importServiceJob(JobBuilderFactory jobs, @Qualifier("serviceStep1") Step s1, JobExecutionListener serviceJobExecutionListener) {
        return jobs.get("importServiceJob")
                .incrementer(new RunIdIncrementer())
                .listener(serviceJobExecutionListener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean("serviceStep1")
    public Step step1Service(StepBuilderFactory stepBuilderFactory, ServiceItemReader serviceItemReader,
            ItemWriter<Service> serviceWriter, ServiceItemProcessor serviceItemProcessor,
            HibernateTransactionManager hibernateTransactionManager,
            SkipAnyFailureSkipPolicy skipPolicy,
            ItemWriteListener<Service> serviceItemWriteListener) {
        return stepBuilderFactory.get("step1")
                .<ServiceCsvDto, Service> chunk(batchSize)
                .reader(serviceItemReader)
                .faultTolerant().skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class)
                .processor(serviceItemProcessor)
                .writer(serviceWriter)
                .listener(serviceItemWriteListener)
                .transactionManager(hibernateTransactionManager)
                .build();
    }
    
    
    @Bean
    public ItemWriteListener<Service> serviceItemWriteListener() {
    	return new ItemWriteListener<Service>() {
    		
			@Override
			public void beforeWrite(List<? extends Service> items) {
				log.debug("Before writing institute item to db {} ",items.size());
			}

			@Override
			public void afterWrite(List<? extends Service> instituteTypesList) {
				log.debug("instituteTypes imported {}",instituteTypesList.size());
			}

			@Override
			public void onWriteError(Exception exception, List<? extends Service> items) {
				List<String> errors = new ArrayList<>();
				items.stream().forEach(item -> 
					errors.add(String.format("%s,%s",item.getName(), ExceptionUtil.findCauseUsingPlainJava(exception).getMessage()))
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
    public JobExecutionListener serviceJobExecutionListener() {
    	return new JobExecutionListener() {
			
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("Job started to import Institutes");
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("Service import done.");
				logFileProcessor.sendFailureEmail(executionId, "UPLOADER: Service batch upload Failures");
			}
		};
    }
}