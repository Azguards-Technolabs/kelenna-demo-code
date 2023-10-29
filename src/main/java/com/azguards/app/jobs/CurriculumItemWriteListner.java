package com.azguards.app.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.azguards.app.bean.CourseCurriculum;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurriculumItemWriteListner implements ItemWriteListener<CourseCurriculum>{
	
	@Autowired
	private LogFileProcessor logFileProcessor;
	
	@Value("#{jobParameters['execution-id']}")
	private String executionId;
	
	@Override
	public void afterWrite(List<? extends CourseCurriculum> items) {
		log.info("Items written : {}",items.size());
	}

	@Override
	public void beforeWrite(List<? extends CourseCurriculum> items) {
		log.debug("Before writing course item to db {} ",items.size());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends CourseCurriculum> items) {
		List<String> errors = new ArrayList<>();
		String rootCause = ExceptionUtil.findCauseUsingPlainJava(exception).getMessage();
		items.stream().forEach(item -> 
			errors.add(String.format("%s,%s",item.getName(), rootCause))
		);
		try {
			logFileProcessor.appendToLogFile(executionId, errors);
		} catch (IOException e) {
			log.error("Exception from onWriteError Event Listener");
		}
	}
}
