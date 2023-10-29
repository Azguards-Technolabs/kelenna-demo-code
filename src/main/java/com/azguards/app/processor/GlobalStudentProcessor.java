package com.azguards.app.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.GlobalData;
import com.azguards.app.dao.GlobalStudentDataDAO;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@Service
public class GlobalStudentProcessor {

	public static final Logger LOGGER = LoggerFactory.getLogger(GlobalStudentProcessor.class);
	
	@Autowired
	private GlobalStudentDataDAO globalStudentDao;
	
	public void uploadGlobalStudentData(MultipartFile multipartFile) throws IOException {
		LOGGER.debug("uploading global student data start()");
        InputStream inputStream = multipartFile.getInputStream();
        LOGGER.info("Start reading data from inputStream using CSV reader through inputStream");
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        Map<String, String> columnMapping = new HashMap<String, String>();
        columnMapping.put("Destination Country", "destinationCountry");
        columnMapping.put("Source Country", "sourceCountry");
        columnMapping.put("Total Students", "totalNumberOfStudent");
        LOGGER.info("Start mapping columns to bean variables");
        HeaderColumnNameTranslateMappingStrategy<GlobalData> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<GlobalData>();
        beanStrategy.setType(GlobalData.class);
        beanStrategy.setColumnMapping(columnMapping);
        CsvToBean<GlobalData> csvToBean = new CsvToBean<GlobalData>();
        LOGGER.info("Start parsing CSV to bean");
        List<GlobalData> globalDataList = csvToBean.parse(beanStrategy, reader);
        LOGGER.info("checking data is already exits or not in DB");
        List<GlobalData> dbGlobalData = globalStudentDao.findAll();
        if(!CollectionUtils.isEmpty(dbGlobalData)) {
        	List<GlobalData> nonExistingGlobalDataList = checkIfAlreadyExists(globalDataList, globalStudentDao.findAll());
            if (!CollectionUtils.isEmpty(nonExistingGlobalDataList)) {
            	LOGGER.info("if data is not present in DB then start adding data in DB");
            	globalStudentDao.saveAll(nonExistingGlobalDataList);
            }
        } else {
        	LOGGER.info("no global data present in DB hence start saving data directly");
        	globalStudentDao.saveAll(globalDataList);
        }
	}
	
	public List<GlobalData> checkIfAlreadyExists( List<GlobalData> globalDataList, List<GlobalData> existingGlobalDataList) {
		LOGGER.debug("Inside checkIfAlreadyExists() method");
        List<GlobalData> pendingListToSave = new ArrayList<GlobalData>();
        Map<String, GlobalData> map = new HashMap<>();
        Map<String, GlobalData> globalStudentyMap = new HashMap<>();
        List<String> existingGlobalStudentList = new ArrayList<String>();
		
        LOGGER.info("start making map of alreayd existing global student data");
        existingGlobalDataList.stream().forEach(dbGlobalDatas -> {
        	if (!StringUtils.isNotEmpty(dbGlobalDatas.getSourceCountry())) {
        		LOGGER.info("making map with key as sourceCountry {} and destinationCountry {} and value as globalData {}"
        				,dbGlobalDatas.getSourceCountry(),dbGlobalDatas.getDestinationCountry());
        		globalStudentyMap.put(dbGlobalDatas.getSourceCountry() + "~" + dbGlobalDatas.getDestinationCountry(), dbGlobalDatas);
        		LOGGER.info("adding values in exitsing golbal data list");
           		existingGlobalStudentList.add(dbGlobalDatas.getSourceCountry().trim() + "~" + dbGlobalDatas.getDestinationCountry());
           	}
        });
        
        LOGGER.info("comparing existing global data with csv global data");
        globalDataList.stream().forEach(csvGlobalData -> {
        	 if (!StringUtils.isNotEmpty(csvGlobalData.getSourceCountry())) {
        		LOGGER.info("checking if there is any existing global student present is DB or not");
                if (!checkExistingGlobalData(existingGlobalStudentList, csvGlobalData.getSourceCountry() + "~" + csvGlobalData.getDestinationCountry())) {
                	LOGGER.info("if data is not presnent in DB then going to add into map");
                	map.put(csvGlobalData.getSourceCountry().trim(), csvGlobalData);
                }
             }
        });
        
        LOGGER.info("iterating the oringinal csv data after comparing with existing global data");
       	map.values().stream().forEach(globalData -> {
       		LOGGER.info("extracting global student data from map and add to list to save into DB");
       		pendingListToSave.add(globalData);	
       	});
            
		return pendingListToSave;
	}
	
	private boolean checkExistingGlobalData(List<String> existingGlobalDataList, String name) {
    	LOGGER.debug("Inside checkExistingGlobalData() method");
        boolean status = false;
        LOGGER.info("Start iterating for checkExistingGlobalData having name : {}",name);
        for (String str : existingGlobalDataList) {
        	LOGGER.info("matching existing student data with pending student data");
            if ((str.trim()).equalsIgnoreCase(name)) {
                status = true;
                break;
            }
        }
        return status;
    }

}
