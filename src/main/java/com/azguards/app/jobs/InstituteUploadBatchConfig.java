package com.azguards.app.jobs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.dto.uploader.InstituteCsvDto;
import com.azguards.common.lib.dto.institute.InstituteSyncDTO;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.common.lib.job.SkipAnyFailureSkipPolicy;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class InstituteUploadBatchConfig {
	
	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

	private String executionId;
	
	@Autowired
	private LogFileProcessor logFileProcessor;

    @Bean
    @StepScope
    public InstituteItemReader instituteItemReader(@Value("#{jobParameters['csv-file']}") String fileName,
    		@Value("#{jobParameters['execution-id']}") String executionId) throws IOException {
    	this.executionId = executionId;
    	String header = "Institute Name,City Name,Country Name,Campus Name,Exception";
    	logFileProcessor.appendToLogFile(executionId, Arrays.asList(header));
        return new InstituteItemReader(fileName);
    }

    @Bean
    public InstituteItemProcessor instituteItemProcessor() {
        return new InstituteItemProcessor();
    }


    @Bean
    public JpaItemWriter<Institute> instituteWriter(@Autowired EntityManagerFactory emf) {
        JpaItemWriter<Institute> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
 
    @Bean("importInstituteJob")
    public Job importInstituteJob(JobBuilderFactory jobs, 
    			@Qualifier("serviceStep1") Step serviceStep,
    			@Qualifier("instituteTypeStep1") Step instituteTypeStep,
    			@Qualifier("instituteStep") Step instituteStep,
    			JobExecutionListener instituteJobExecutionListener) {
        return jobs.get("importInstituteJob")
                .incrementer(new RunIdIncrementer())
                .listener(instituteJobExecutionListener)
                .start(instituteTypeStep).next(serviceStep).next(instituteStep)
                .build();
    }

    @Bean("instituteStep")
    public Step instituteStep(StepBuilderFactory stepBuilderFactory, InstituteItemReader instituteItemReader,
            ItemWriter<Institute> instituteWriter, InstituteItemProcessor instituteItemProcessor,
            HibernateTransactionManager hibernateTransactionManager,
            SkipAnyFailureSkipPolicy skipPolicy,
            ItemWriteListener<Institute> instituteItemWriteListener) {
        return stepBuilderFactory.get("step1")
                .<InstituteCsvDto, Institute> chunk(batchSize)
                .reader(instituteItemReader)
                .faultTolerant().skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class)
                .processor(instituteItemProcessor)
                .writer(instituteWriter)
                .listener(instituteItemWriteListener)
                .transactionManager(hibernateTransactionManager)
                .build();
    }
    
    
    @Bean
    public ItemWriteListener<Institute> instituteItemWriteListener() {
    	return new ItemWriteListener<Institute>() {
    		
    		@Autowired
    		private PublishSystemEventHandler publishSystemEventHandler;
    		
			@Override
			public void beforeWrite(List<? extends Institute> items) {
				log.debug("Before writing institute item to db {} ",items.size());
			}

			@Override
			public void afterWrite(List<? extends Institute> institutesList) {
				List<InstituteSyncDTO> instituteElasticSearchDTOList = new ArrayList<>();
				for (Institute instituteObj : institutesList) {
					InstituteSyncDTO instituteElasticSearchDto = new InstituteSyncDTO();
					BeanUtils.copyProperties(instituteObj,instituteElasticSearchDto);
					instituteElasticSearchDto.setId(instituteObj.getId());
					instituteElasticSearchDto.setCountryName(instituteObj.getCountryName());
					instituteElasticSearchDto.setCityName(instituteObj.getCityName());
					if(!CollectionUtils.isEmpty(instituteObj.getInstituteIntakes())) {
						instituteElasticSearchDto.setInstituteIntakes(instituteObj.getInstituteIntakes().stream().map(InstituteIntake::getIntake).collect(Collectors.toList()));
					}
					if(!StringUtils.isEmpty(instituteObj.getInstituteType())) {
						instituteElasticSearchDto.setInstituteType(instituteObj.getInstituteType());
					}
					instituteElasticSearchDTOList.add(instituteElasticSearchDto);
				}
				log.info("Calling elastic search service having entityType  :{}",EntityTypeEnum.INSTITUTE);
				publishSystemEventHandler.syncInstitutes(instituteElasticSearchDTOList);
			}

			@Override
			public void onWriteError(Exception exception, List<? extends Institute> items) {
				List<String> errors = new ArrayList<>();
				items.stream().forEach(item -> 
					errors.add(String.format("%s,%s,%s,%s,%s",item.getName(),item.getCityName(),item.getCountryName(), ExceptionUtil.findCauseUsingPlainJava(exception).getMessage()))
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
    public JobExecutionListener instituteJobExecutionListener() {
    	return new JobExecutionListener() {
			
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("Job started to import Institutes");
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("Institute import done.");
				logFileProcessor.sendFailureEmail(executionId, "UPLOADER: Institute batch upload Failures");
			}
		};
    }
}