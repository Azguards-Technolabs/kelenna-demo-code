package com.azguards.app.jobs;

import org.springframework.batch.item.ItemProcessor;

import com.azguards.app.bean.Faculty;
import com.azguards.app.dto.uploader.FacultyCSVDto;
import com.azguards.common.lib.util.DateUtil;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FacultyItemProcessor implements ItemProcessor<FacultyCSVDto, Faculty> {

	@Override
	public Faculty process(FacultyCSVDto facultyCsvDto) throws Exception {
		log.info("Creating faculty model for faculty name {}", facultyCsvDto.getName());
		return new Faculty(Utils.generateUUID(facultyCsvDto.getName()), facultyCsvDto.getName(), facultyCsvDto.getDescription(), true, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), null, "AUTO", "AUTO", false);
	}

}
