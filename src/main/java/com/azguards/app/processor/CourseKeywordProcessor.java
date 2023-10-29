package com.azguards.app.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.CourseKeywords;
import com.azguards.app.dao.CourseKeywordDao;
import com.azguards.app.util.Util;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CourseKeywordProcessor {

	@Autowired
	private CourseKeywordDao courseKeywordDao;

	public void save(CourseKeywords courseKeywords) {
		courseKeywordDao.save(courseKeywords);
	}

	public void update(CourseKeywords courseKeywords) {
		courseKeywordDao.update(courseKeywords);
	}

	public List<CourseKeywords> getAll() {
		return courseKeywordDao.getAll();
	}

	public List<CourseKeywords> searchCourseKeyword(String keyword) {
		return courseKeywordDao.searchCourseKeyword(keyword);
	}
	
	@SuppressWarnings("deprecation")
	public void saveCourseKeyword(final MultipartFile multipartFile) throws IOException, ParseException {
		log.debug("Inside saveCourseKeyword() method");
		InputStream inputStream = multipartFile.getInputStream();
		CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
		log.info("Start reading data from inputStream using CSV reader");
		Map<String, String> columnMapping = new HashMap<>();
		 log.info("Start mapping columns to bean variables");
		columnMapping.put("keyword", "keyword");
		columnMapping.put("k_desc", "KDesc");

		HeaderColumnNameTranslateMappingStrategy<CourseKeywords> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
		beanStrategy.setType(CourseKeywords.class);
		beanStrategy.setColumnMapping(columnMapping);
		CsvToBean<CourseKeywords> csvToBean = new CsvToBean<>();
		log.info("Start parsing CSV to bean");
		List<CourseKeywords> courseKeywords = csvToBean.parse(beanStrategy, reader);
		log.info("Going to check in existing courseKeyword is present in DB or not");
		List<CourseKeywords> notExistingCourseKeywordList = checkIfAlreadyExistsCourseKeyword(courseKeywords, courseKeywordDao.getAll());
		if (notExistingCourseKeywordList != null && notExistingCourseKeywordList.size() > 0) {
			log.info("if existing country is not null or empty then start saving cousreKeyword data in DB");
			courseKeywordDao.saveAll(notExistingCourseKeywordList);
		}
		log.info("Closing input stream");
		inputStream.close();
		log.info("Closing CSV reader");
		reader.close();
	}
	
	private List<CourseKeywords> checkIfAlreadyExistsCourseKeyword(final List<CourseKeywords> courseKeywords,
			final List<CourseKeywords> existingCourseKeywords) {
		log.debug("Inside checkIfAlreadyExistsCourseKeyword() method");
		List<CourseKeywords> pendingListToSave = new ArrayList<>();
		Map<String, CourseKeywords> map = new HashMap<>();
		Map<String, CourseKeywords> countryMap = new HashMap<>();
		List<String> existingCountryList = new ArrayList<>();
		for (CourseKeywords dbCourseKeyword : existingCourseKeywords) {
			if (dbCourseKeyword.getKeyword() != null) {
				log.info("if keyword in not null");
				countryMap.put(dbCourseKeyword.getKeyword(), dbCourseKeyword);
				existingCountryList.add(dbCourseKeyword.getKeyword());
			}
		}

		for (CourseKeywords csvCourseKeyword : courseKeywords) {
			if (!Util.containsIgnoreCase(existingCountryList, csvCourseKeyword.getKeyword())) {
				map.put(csvCourseKeyword.getKeyword(), csvCourseKeyword);
			}
		}
		for (CourseKeywords jobSites : map.values()) {
			pendingListToSave.add(jobSites);
		}
		return pendingListToSave;
	}
}
