package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.institute.LevelDto;
import com.azguards.common.lib.exception.IOException;
import com.azguards.common.lib.exception.UploaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LevelItemReader implements ItemReader<LevelDto> {
	
	private int nextLevelIndex;
	List<LevelDto> listOfLevelDto;
	
	public  LevelItemReader(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	private void loadFile(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.debug("Inside load level file method");
		InputStream inputStream = null;
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			log.info("Start reading data from inputStream using CSV reader");
			Map<String, String> columnMapping = new HashMap<>();
	        log.info("Start mapping columns to bean variables");
			columnMapping.put("Level_code", "code");
			columnMapping.put("Level_name", "name");
			columnMapping.put("Level_description", "description");
			columnMapping.put("Sequence_no", "sequenceNo");
					
			HeaderColumnNameTranslateMappingStrategy<LevelDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(LevelDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<LevelDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.listOfLevelDto = csvToBean.parse(beanStrategy, reader);
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
	public LevelDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		LevelDto levelDto = null;
		
		if (nextLevelIndex < listOfLevelDto.size()) {
			levelDto = listOfLevelDto.get(nextLevelIndex);
			nextLevelIndex ++ ;
		}
		return levelDto;
	}
}
