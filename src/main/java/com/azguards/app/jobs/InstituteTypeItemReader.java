package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemReader;

import com.azguards.app.dto.InstituteTypeDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstituteTypeItemReader implements ItemReader<InstituteTypeDto> {
	
	private int nextInstituteIndex;
	List<InstituteTypeDto> instituteTypeList;
	
	public InstituteTypeItemReader(String fileName) {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	private void loadFile(String fileName) {
		InputStream inputStream = null;
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			log.info("Start mapping columns to bean variables");
			columnMapping.put("Type_of_Institution", "instituteTypeName");
			columnMapping.put("Country", "countryName");

			HeaderColumnNameTranslateMappingStrategy<InstituteTypeDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(InstituteTypeDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<InstituteTypeDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.instituteTypeList = csvToBean.parse(beanStrategy, reader);
			this.instituteTypeList = this.instituteTypeList.parallelStream().distinct().collect(Collectors.toList());
		}catch (Exception e) {
			log.error("Exception while running batch job");
		}
	}

	@Override
	public InstituteTypeDto read() {
		InstituteTypeDto nextInstitute = null;
		 
        if (nextInstituteIndex < this.instituteTypeList.size()) {
        	nextInstitute = this.instituteTypeList.get(nextInstituteIndex);
        	nextInstituteIndex++;
        }
        else {
        	nextInstituteIndex = 0;
        }
        return nextInstitute;
	}
	
}
