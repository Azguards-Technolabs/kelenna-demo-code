package com.azguards.app.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.azguards.app.processor.CourseProcessor;
import com.azguards.app.util.CommonUtil;
import com.azguards.app.util.IConstant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.azguards.common.lib.dto.common.CurrencyRateDto;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.handler.CommonHandler;
import com.azguards.common.lib.handler.PublishSystemEventHandler;
import com.azguards.common.lib.util.DateUtil;

@Component
public class CurrencyConversionRateUtil {

	private static final Logger log = LoggerFactory.getLogger(CurrencyConversionRateUtil.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CourseProcessor courseProcessor;
	
	@Autowired
	private PublishSystemEventHandler publishSystemEventHandler;
	
	@Autowired
	private CommonHandler commonHandler;
	
	private static List<String> failedRecordsInElasticSearch = new ArrayList<>();

	// @Scheduled(fixedRate = 86400000, initialDelay = 10000)
	@Scheduled(cron = "0 30 0 * * ?")
	public void curencySchedulerMethod() {
		log.info("CurrencyConversionRateUtil -- Start: The time is now {}", dateFormat.format(new Date()));
		System.out.println("CurrencyConversionRateUtil -- Start: The time is now {}" + dateFormat.format(new Date()));
        run();
		updateCourses();
		log.info("CurrencyConversionRateUtil -- End: The time is now {}", dateFormat.format(new Date()));
		System.out.println("CurrencyConversionRateUtil -- End: The time is now {}" + dateFormat.format(new Date()));
		
		log.info("Update COurses In Elastic Search Started -- Start: The time is now {}", dateFormat.format(new Date()));
		System.out.println("Update COurses In Elastic Search Started -- Start: The time is now {}" + dateFormat.format(new Date()));
		updateCoursesInElasticSearch();
		log.info("Update COurses In Elastic Search End -- End: The time is now {}", dateFormat.format(new Date()));
		System.out.println("Update COurses In Elastic Search End -- End: The time is now {}" + dateFormat.format(new Date()));
	}

	public void run() {
		System.out.println("CurrencyConversionRateUtil: Job Started: " + new Date());
		try {
			RestTemplate restTemplate = new RestTemplate();

			String result = restTemplate.getForObject(IConstant.CURRENCY_URL + "latest?access_key=" + IConstant.API_KEY + "&base=" + IConstant.USD_CODE,
					String.class);

			JsonParser jp = new JsonParser();
			JsonElement jsonElement = jp.parse(result);
			JsonObject obj = jsonElement.getAsJsonObject().get("rates").getAsJsonObject();
			Map<String, Double> map = objectMapper.readValue(obj.toString(), HashMap.class);

			if(CommonUtil.currencyNameMap.isEmpty()) {
				CommonUtil.setCurrencyNames();
			}
			for (Map.Entry<String, Double> currency : map.entrySet()) {
				String currencyCode = currency.getKey();
				CurrencyRateDto currencyRate = commonHandler.getCurrencyRateByCurrencyCode(currencyCode);
				if(currencyRate == null) {
					CurrencyRateDto currRate = new CurrencyRateDto();
					currRate.setFromCurrencyCode(IConstant.USD_CODE);
					currRate.setToCurrencyCode(currencyCode);
					currRate.setConversionRate(Double.parseDouble(String.valueOf(currency.getValue())));
					currRate.setToCurrencyName(CommonUtil.currencyNameMap.get(currency.getKey()));
					currRate.setFromCurrencyName(CommonUtil.currencyNameMap.get(IConstant.USD_CODE));
					currRate.setHasChanged(false);
					commonHandler.saveCurrencyRate(currRate);
					currencyRate = currRate;
				}
				Double oldRate = currencyRate.getConversionRate();
				currencyRate.setConversionRate(Double.parseDouble(String.valueOf(currency.getValue())));
				Integer thresholdValue = IConstant.CURRENCY_THRESHOLD;
				if (thresholdValue != null && thresholdValue != 0) {
					boolean isGreaterThanThreshold = checkForDifferenceGreaterThanThreshold(oldRate, Double.valueOf(String.valueOf(currency.getValue())),
							thresholdValue);
					if (isGreaterThanThreshold) {
						currencyRate.setHasChanged(true);
						commonHandler.saveCurrencyRate(currencyRate);
					}
				} else if (thresholdValue <= 0) {
					currencyRate.setHasChanged(true);
					commonHandler.saveCurrencyRate(currencyRate);
				}
			}
		} catch (JsonParseException e) {
			log.error("JsonParseException Error in currency rate scheduler " + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException Error in currency rate scheduler " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException Error in currency rate scheduler " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("CurrencyConversionRateUtil: Job Completed: " + new Date());
	}

	private boolean checkForDifferenceGreaterThanThreshold(final Double oldRate, final Double newRate, final Integer thresholdValue) {
		Double thresholdAmt = oldRate * thresholdValue / 100;
		if (oldRate - thresholdAmt < newRate && oldRate + thresholdAmt > newRate) {
			return false;
		} else {
			return true;
		}
	}

	public void updateCourses() {
		List<CurrencyRateDto> currencyRateList = commonHandler.getChangedCurrencyRate(true);
		for (CurrencyRateDto currencyRate : currencyRateList) {
			courseProcessor.updateCourseForCurrency(currencyRate);
		}
	}
	
	public void updateCoursesInElasticSearch() {
		Integer totalUpdatedCourses = courseProcessor.getCountOfTotalUpdatedCourses(DateUtil.getUTCdatetimeAsOnlyDate());
		System.out.println("Total COurses to be updated --- "+totalUpdatedCourses);
		
		for (int i = 0; i < totalUpdatedCourses; i=i+IConstant.COURSES_PER_SCHEDULER_LOOP) {
			List<CourseSyncDTO> courseDtoElasticSearchList =  courseProcessor.getUpdatedCourses(DateUtil.getUTCdatetimeAsOnlyDate(), i, IConstant.COURSES_PER_SCHEDULER_LOOP);
			// List<CourseDTOElasticSearch> courseDtoElasticSearchList = new ArrayList<>();
//			for (Course course : courseList) {
//				CourseDTOElasticSearch courseDtoElasticSearch = new CourseDTOElasticSearch();
//				BeanUtils.copyProperties(course, courseDtoElasticSearch);
//				courseDtoElasticSearch.setFacultyName(course.getFaculty()!=null?course.getFaculty().getName():null);
//				courseDtoElasticSearch.setCountryName(course.getCountry()!=null?course.getCountry().getName():null);
//				courseDtoElasticSearch.setCityName(course.getCity()!=null?course.getCity().getName():null);
//				courseDtoElasticSearch.setInstituteName(course.getInstitute()!=null?course.getInstitute().getName():null);
//				courseDtoElasticSearch.setLevelCode(course.getLevel()!=null?course.getLevel().getCode():null);
//				courseDtoElasticSearch.setLevelName(course.getLevel()!=null?course.getLevel().getName():null);
//				courseDtoElasticSearchList.add(courseDtoElasticSearch);
//			}
//			Map<String, List<String>> courseUpdateStatus = elasticHandler.updateCourseOnElasticSearch(IConstant.ELASTIC_SEARCH_INDEX_COURSE, EntityTypeEnum.COURSE.name().toLowerCase(), courseDtoElasticSearchList, IConstant.ELASTIC_SEARCH);
//			failedRecordsInElasticSearch.addAll(courseUpdateStatus.get("failed"));
		}
	}
	
	// @Scheduled(fixedRate = 86400000, initialDelay = 10000)
	@Scheduled(cron = "0 30 3 * * ?")
	public void retryForFailedIds() {
		Integer totalCourseToBeRetried = failedRecordsInElasticSearch.size();
		
		for (int i = 0; i < totalCourseToBeRetried; i=i+IConstant.COURSES_PER_SCHEDULER_LOOP) {
			List<String> courseIds = failedRecordsInElasticSearch.subList(i, i+IConstant.COURSES_PER_SCHEDULER_LOOP < totalCourseToBeRetried ? i+IConstant.COURSES_PER_SCHEDULER_LOOP: totalCourseToBeRetried);
			List<CourseSyncDTO> courseDtoElasticSearchList =  courseProcessor.getCoursesToBeRetriedForElasticSearch(courseIds, i, IConstant.COURSES_PER_SCHEDULER_LOOP);
			publishSystemEventHandler.syncCourses(courseDtoElasticSearchList);
		}
		
		failedRecordsInElasticSearch.clear();
	}
	
}
