package com.azguards.app.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.azguards.app.bean.InstituteType;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dto.InstituteTypeDto;
import com.azguards.common.lib.util.DateUtil;

public class InstituteTypeItemProcessor implements ItemProcessor<InstituteTypeDto, InstituteType> {

	@Autowired
	InstituteDao instituteDao;
	
	@Override
	public InstituteType process(InstituteTypeDto instituteTypeDto) throws Exception {
		InstituteType instituteType = new InstituteType();
		instituteType.setCountryName(instituteTypeDto.getCountryName());
		instituteType.setName(instituteTypeDto.getInstituteTypeName());
		instituteType.setDescription(instituteTypeDto.getInstituteTypeName());
		instituteType.setCreatedBy("AUTO");
		instituteType.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		instituteType.setUpdatedBy("AUTO");
		instituteType.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		return instituteType;
	}
}
