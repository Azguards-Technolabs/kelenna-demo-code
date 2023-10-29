package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.institute.CourseCurriculumDto;
import com.azguards.common.lib.exception.IOException;
import com.azguards.common.lib.exception.UploaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurriculumItemReader implements ItemReader<CourseCurriculumDto>  {
	
	private List<CourseCurriculumDto> listOfCourseCurriculumDto;
	private int nextCourseCurriculumIndex;
	
	public CurriculumItemReader(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	@SuppressWarnings("deprecation")
	private void loadFile(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.debug("Inside saveCourseData() method");
		InputStream inputStream = null;
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			log.info("Start reading data from inputStream using CSV reader");
			Map<String, String> columnMapping = new HashMap<>();
	        log.info("Start mapping columns to bean variables");
			columnMapping.put("Curriculum", "name");
			
			HeaderColumnNameTranslateMappingStrategy<CourseCurriculumDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(CourseCurriculumDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<CourseCurriculumDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.listOfCourseCurriculumDto = csvToBean.parse(beanStrategy, reader);
			this.listOfCourseCurriculumDto = this.listOfCourseCurriculumDto.parallelStream().distinct().filter(dto -> !StringUtils.isEmpty(dto.getName())).collect(Collectors.toList());
		} catch (java.io.IOException e) {
			log.error("Exception in Saving Course Data exception {} ", e);
			throw new IOException(e.getMessage());
		} catch (ParseException parseEx) {
			log.error("Exception in Saving Course Data exception {} ", parseEx);
			throw new UploaderException(parseEx.getMessage());
		} finally {
			if (null != reader ) {
				log.info("Closing CSV reader");
				reader.close();
			} 
			
			if (null != inputStream) {
				log.info("Closing input stream");
				inputStream.close();
			}
		}
	}

	@Override
	public CourseCurriculumDto read() {
		CourseCurriculumDto nextCourseCurriculum = null;
        if (nextCourseCurriculumIndex < this.listOfCourseCurriculumDto.size()) {
        	nextCourseCurriculum = this.listOfCourseCurriculumDto.get(nextCourseCurriculumIndex);
        	nextCourseCurriculumIndex++;
        }
        else {
        	nextCourseCurriculumIndex = 0;
        }
        return nextCourseCurriculum;
	}
}
