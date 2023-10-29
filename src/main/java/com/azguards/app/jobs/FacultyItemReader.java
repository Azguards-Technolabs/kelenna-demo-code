package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.azguards.app.dto.uploader.FacultyCSVDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.exception.IOException;
import com.azguards.common.lib.exception.UploaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FacultyItemReader implements ItemReader<FacultyCSVDto> {
	
	private int nextFacultyIndex;
	private List<FacultyCSVDto> listDistinctOfFacultyCsvDto;
	
	public FacultyItemReader(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	
	private void loadFile(String fileName) throws IOException, UploaderException, java.io.IOException {
		List<FacultyCSVDto> listOfFacultyCsvDto = new ArrayList<FacultyCSVDto>();
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
			columnMapping.put("Faculty", "name");
			columnMapping.put("Faculty_Description", "description");
					
			HeaderColumnNameTranslateMappingStrategy<FacultyCSVDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(FacultyCSVDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<FacultyCSVDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			listOfFacultyCsvDto = csvToBean.parse(beanStrategy, reader);
			this.listDistinctOfFacultyCsvDto = listOfFacultyCsvDto.stream().filter(distinctByKey(facultyCsvDto -> facultyCsvDto.getName())).collect(Collectors.toList());
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
	public FacultyCSVDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		FacultyCSVDto facultyCsvDto = null;
		
		if (nextFacultyIndex < listDistinctOfFacultyCsvDto.size()) {
			facultyCsvDto = listDistinctOfFacultyCsvDto.get(nextFacultyIndex);
			nextFacultyIndex ++ ;
		}
		return facultyCsvDto;
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
	    Map<Object, Boolean> map = new ConcurrentHashMap<>();
	    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
