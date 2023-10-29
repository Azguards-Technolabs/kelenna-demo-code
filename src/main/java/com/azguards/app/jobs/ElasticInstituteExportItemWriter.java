package com.azguards.app.jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.azguards.common.lib.dto.institute.InstituteSyncDTO;
import com.azguards.common.lib.handler.PublishSystemEventHandler;

public class ElasticInstituteExportItemWriter implements ItemWriter<InstituteSyncDTO> {
	
	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;

	@Override
	public void write(List<? extends InstituteSyncDTO> items) throws Exception {
		List<InstituteSyncDTO> instituteElasticDtos = new ArrayList<>();
		instituteElasticDtos.addAll(items);
		publishSystemEventHandler.syncInstitutes(instituteElasticDtos);
	}

	

}
