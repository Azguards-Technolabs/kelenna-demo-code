package com.azguards.app.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.azguards.app.bean.Institute;
import com.azguards.app.processor.ConversionProcessor;
import com.azguards.common.lib.dto.institute.InstituteSyncDTO;

public class ElasticInstituteExportItemProcessor implements ItemProcessor<Institute, InstituteSyncDTO> {

	@Autowired
	private ConversionProcessor conversionProcessor;

	@Override
	public InstituteSyncDTO process(Institute institute) throws Exception {
		return conversionProcessor.convertToInstituteInstituteSyncDTOSynDataEntity(institute);
	}

}
