package com.azguards.app.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.azguards.app.bean.Faculty;
import com.azguards.common.lib.dto.institute.FacultyDto;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.common.lib.processor.LogFileProcessor;
import com.azguards.common.lib.util.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FacultyItemWriteListner implements ItemWriteListener<Faculty> {

	@Autowired
	private LogFileProcessor logFileProcessor;

	@Value("#{jobParameters['execution-id']}")
	private String executionId;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;

	@Override
	public void afterWrite(List<? extends Faculty> items) {
		log.debug("After writing faculty item to db {} ", items.size());
		publishSystemEventHandler.syncFaculties(
				items.stream().map(e -> modelMapper.map(e, FacultyDto.class)).collect(Collectors.toList()));
	}

	@Override
	public void beforeWrite(List<? extends Faculty> items) {
		log.debug("Before writting faculty item to db {}", items.size());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Faculty> items) {
		List<String> errors = new ArrayList<>();
		items.stream().forEach(item -> errors.add(
				String.format("%s,%s", item.getName(), ExceptionUtil.findCauseUsingPlainJava(exception).getMessage())));
		try {
			logFileProcessor.appendToLogFile(executionId, errors);
		} catch (IOException e) {
			log.error("Exception from onWriteError Event Listener");
		}

	}

}
