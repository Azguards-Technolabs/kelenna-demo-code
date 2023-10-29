package com.azguards.app.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.Level;
import com.azguards.app.dao.LevelDao;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.institute.LevelDto;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class LevelProcessor {

    @Autowired
    private LevelDao levelDao;

    @Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("importlevelJob")
	private Job job;
	
	@Transactional
    public void addUpdateLevel(LevelDto levelDto) {
    	Level level = new Level(null, levelDto.getName(), levelDto.getCode(), null,levelDto.getSequenceNo(), true, new Date(), null, null, "API", null, null);
    	levelDao.addUpdateLevel(level);
    }

	@Transactional
	public Level getLevel(String id) {
        return levelDao.getLevel(id);
    }
    
	@Transactional
    public List<LevelDto> getAllLevels() {
    	log.debug("Inside getAllLevels() method");
    	List<LevelDto> levelDtos = new ArrayList<>();
    	log.info("Fetching all levels from DB");
    	List<Level> levelsFromDB = levelDao.getAll();
    	if(!CollectionUtils.isEmpty(levelsFromDB)) {
    		log.info("Levels fetched from DB, start iterating data to make response");
    		levelsFromDB.stream().forEach(level -> {
    			LevelDto levelDto = new LevelDto(level.getId(), level.getName(), level.getCode(), level.getCategory(),level.getDescription(), level.getSequenceNo());
    			levelDtos.add(levelDto);
    		});
    	}
        return levelDtos;
    }
    
	@Transactional
    public List<Level> getCourseTypeByCountryId(String countryID) {
        return levelDao.getCourseTypeByCountryId(countryID);
    }

	@Transactional
    public List<Level> getLevelByCountryId(String countryId) {
        return levelDao.getLevelByCountryId(countryId);
    }

	@Transactional
    public List<Level> getAllLevelByCountry() {
        return levelDao.getAllLevelByCountry();
    }

	@Transactional
    public Map<String, Object> getCountryLevel(String countryId) {
        Map<String, Object> response = new HashMap<>();
        List<LevelDto> levels = new ArrayList<LevelDto>();
        try {
            levels = levelDao.getCountryLevel(countryId);
            if (levels != null && !levels.isEmpty()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Level fetched successfully");
                response.put("courses", levels);
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Level not found");
            }
        } catch (Exception exception) {
            response.put("message", exception.getCause());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

	@Transactional
	public List<String> getAllLevelNameByInstituteId(String instituteId) {
		return levelDao.getAllLevelNamesByInstituteId(instituteId);
	}

	@CacheEvict(cacheNames = {"cacheLevelMap"}, allEntries = true)
	public void importLevel(final MultipartFile multipartFile) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.debug("Inside importLevel() method");
		log.info("Calling methiod to save level data");
		
		File f = File.createTempFile("levels", ".csv");
		multipartFile.transferTo(f);
		
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("csv-file", f.getAbsolutePath());
		jobParametersBuilder.addString("execution-id", UUID.randomUUID().toString());
		jobLauncher.run(job, jobParametersBuilder.toJobParameters());
	}

	@Transactional
	public LevelDto getLevelById(String levelId) {
		LevelDto levelDto = null;
		Level level = getLevel(levelId);
		if (!ObjectUtils.isEmpty(level)) {
			levelDto = new LevelDto(level.getId(), level.getName(), level.getCode(), level.getCategory(),level.getDescription(), level.getSequenceNo());
		}
		return levelDto;
		
	}
	
	@Transactional
	public void updateLevelCategory (final MultipartFile multipartFile) throws IOException {
		File f = null ;
		try {
			List<Level> levels = new ArrayList<Level>();
			
			f = File.createTempFile("levels", ".csv");
			multipartFile.transferTo(f);
			InputStream inputStream = new FileInputStream(f.getAbsoluteFile());
			log.info("Start reading data from inputStream using CSV reader");
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			log.info("Start reading data from inputStream using CSV reader");
			Map<String, String> columnMapping = new HashMap<>();
	        log.info("Start mapping columns to bean variables");
	        columnMapping.put("Level Type", "code");
			columnMapping.put("Level Category", "category");
			
			HeaderColumnNameTranslateMappingStrategy<LevelDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(LevelDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<LevelDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			List<LevelDto> listOfLevelDto = csvToBean.parse(beanStrategy, reader);
			
			listOfLevelDto.stream().forEach(levelDto -> {
				Level level = levelDao.getLevelByLevelCode(levelDto.getCode());
				if (!ObjectUtils.isEmpty(level)) {
					level.setCategory(levelDto.getCategory());
					levels.add(level);
				}
			});
			
			levelDao.addUpdateLevels(levels);
		} finally {
			f.delete();
		}
		
		
		
	}
}
