package com.azguards.app.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.processor.CommonProcessor;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseItemWriteListner implements ItemWriteListener<Course>{
	
	@Autowired
	private CommonProcessor commonProcessor;
	
	@Autowired
	private LogFileProcessor logFileProcessor;
	
	@Value("#{jobParameters['execution-id']}")
	private String executionId;
	
	@Override
	public void afterWrite(List<? extends Course> items) {
		log.info("Calling elastic search to add course");
		commonProcessor.saveElasticCourses((List<Course>)items);
	}

	@Override
	public void beforeWrite(List<? extends Course> items) {
		log.debug("Before writing course item to db {} ",items.size());
	}

	@Transactional
	@Override
	public void onWriteError(Exception exception, List<? extends Course> items) {
		List<String> errors = new ArrayList<>();
		String rootCause = ExceptionUtil.findCauseUsingPlainJava(exception).getMessage();
		items.stream().forEach(item -> 
			errors.add(String.format("%s,%s,%s,%s,%s",item.getName(),(ObjectUtils.isEmpty(item.getInstitute()))?"No Institute Found":item.getInstitute().getName()+"~"+item.getInstitute().getCityName()+"~"+item.getInstitute().getCountryName(),(ObjectUtils.isEmpty(item.getLevel()))?"No Level Found":item.getLevel().getName(),(ObjectUtils.isEmpty(item.getFaculty()))?"No Faculty Found":item.getFaculty().getName(), rootCause))
		);
		try {
			logFileProcessor.appendToLogFile(executionId, errors);
		} catch (IOException e) {
			log.error("Exception from onWriteError Event Listener");
		}
	}
}
