package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.endpoint.JobSkillInterface;
import com.azguards.app.processor.JobSkillProcessor;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;

@RestController("jobSkillController")
public class JobSkillController implements JobSkillInterface {

	@Autowired
	private JobSkillProcessor jobSkillProcessor;
	@Autowired
	private MessageTranslator messageTranslator;
	@Override
	public ResponseEntity<?> getJobSkills(String userId, Integer pageNumber, Integer pageSize,List<String> jobNames) {
		return new GenericResponseHandlers.Builder()
				.setData(jobSkillProcessor.getJobSkills(userId, pageNumber,pageSize, jobNames))
				.setStatus(HttpStatus.OK)
				.setMessage(messageTranslator.toLocale("job_skills.list.retrieved")).create();
	}
}
