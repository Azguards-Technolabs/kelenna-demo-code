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

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseCurriculum;
import com.azguards.app.dto.uploader.CourseCsvDto;
import com.azguards.common.lib.dto.institute.CourseCurriculumDto;
import com.azguards.common.lib.exception.UploaderException;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j(topic = "course_import")
public class CourseUploadBatchConfig {

	@Value("${spring.jdbc.batch_size}")
	private int batchSize;

    @Bean("courseItemReader")
    @StepScope
    public CourseItemReader reader(@Value("#{jobParameters['csv-file']}") String fileName) throws IOException, UploaderException, com.azguards.common.lib.exception.IOException {
        return new CourseItemReader(fileName);
    }

    @Bean("courseItemProcessor")
    public ItemProcessor<CourseCsvDto, Course> processor() {
        return new CourseItemProcessor();
    }
    
    @Bean("curriculumItemReader")
    @StepScope
    public ItemReader<CourseCurriculumDto> curriculumReader(@Value("#{jobParameters['csv-file']}") String fileName) throws UploaderException, IOException, com.azguards.common.lib.exception.IOException {
    	return new CurriculumItemReader(fileName);
    }
    
    @Bean("curriculumItemProcessor")
    public ItemProcessor<CourseCurriculumDto, CourseCurriculum> curriculumProcessor() {
        return new CurriculumItemProcessor();
    }

    @Bean("courseItemWriter")
    @StepScope
    public JpaItemWriter<Course> writer(@Autowired EntityManagerFactory emf) {
        JpaItemWriter<Course> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
    
    @Bean("curriculumItemWriter")
    @StepScope
    public JpaItemWriter<CourseCurriculum> curriculumItemWriter(@Autowired EntityManagerFactory emf) {
        JpaItemWriter<CourseCurriculum> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
	
    @Bean("importCourseJob")
    public Job importCourseJob(JobBuilderFactory jobs,@Qualifier("facultyStep") Step facultyStep, @Qualifier ("courseStep") Step courseStep,
    		@Qualifier("curriculumStep") Step curriculumStep,
    		@Qualifier("courseJobExecutionListner") JobExecutionListener courseJobExecutionListner) {
        return jobs.get("importCourseJob")
                .incrementer(new RunIdIncrementer()).listener(courseJobExecutionListner)
                .start(facultyStep).next(curriculumStep).next(courseStep)
                .build();
    }
    
    @Bean("curriculumStep")
    public Step curriculumStep(StepBuilderFactory stepBuilderFactory, @Qualifier ("curriculumItemReader") ItemReader<CourseCurriculumDto> reader,
    		@Qualifier ("curriculumItemWriter") ItemWriter<CourseCurriculum> writer,@Qualifier ("curriculumItemProcessor") ItemProcessor<CourseCurriculumDto, CourseCurriculum> processor,
            HibernateTransactionManager hibernateTransactionManager,
            CourseImportSkipPolicy skipPolicy,@Qualifier("curriculumItemWriterListner") ItemWriteListener<CourseCurriculum> courseItemWriterListner) {
    	return stepBuilderFactory.get("courseStep")
                .<CourseCurriculumDto, CourseCurriculum> chunk(batchSize)
                .reader(reader)
                .faultTolerant().skipPolicy(skipPolicy).noRollback(ConstraintViolationException.class)
                .processor(processor)
                .writer(writer).listener(courseItemWriterListner)
                .transactionManager(hibernateTransactionManager)
                .build();
    }
    
    @Bean("courseStep")
    public Step courseStep(StepBuilderFactory stepBuilderFactory, @Qualifier ("courseItemReader") ItemReader<CourseCsvDto> reader,
    		@Qualifier ("courseItemWriter") ItemWriter<Course> writer,@Qualifier ("courseItemProcessor") ItemProcessor<CourseCsvDto, Course> processor,
            HibernateTransactionManager hibernateTransactionManager,
            CourseImportSkipPolicy skipPolicy,@Qualifier("courseItemWriterListner") ItemWriteListener<Course> courseItemWriterListner) {
        return stepBuilderFactory.get("courseStep")
                .<CourseCsvDto, Course> chunk(1)
                .reader(reader)
                .faultTolerant().skipPolicy(skipPolicy)
                .processor(processor)
                .writer(writer).listener(courseItemWriterListner)
                .transactionManager(hibernateTransactionManager)
                .build();
    }
	
    @Bean("courseItemWriterListner")
    @StepScope
    public ItemWriteListener<Course> courseItemListner() {
    	return new CourseItemWriteListner();
    }

    
    @Bean("curriculumItemWriterListner")
    @StepScope
    public ItemWriteListener<CourseCurriculum> curriculumItemListner() {
    	return new CurriculumItemWriteListner();
    }

    @Bean("courseJobExecutionListner")
    public JobExecutionListener courseJobExecutionListner () {
    	return new CourseJobExecutionListner();
    }

}