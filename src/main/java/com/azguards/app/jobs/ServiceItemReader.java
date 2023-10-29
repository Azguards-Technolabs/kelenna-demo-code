package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.util.CollectionUtils;

import com.azguards.app.dto.uploader.InstituteServiceDto;
import com.azguards.app.dto.uploader.ServiceCsvDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceItemReader implements ItemReader<ServiceCsvDto> {
	
	private int nextServiceIndex;
	List<InstituteServiceDto> serviceList;
	List<ServiceCsvDto> expandedServiceList;
	
	public ServiceItemReader(String fileName) {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	@SuppressWarnings("deprecation")
	private void loadFile(String fileName) {
		InputStream inputStream = null;
		expandedServiceList = new ArrayList<>();
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			log.info("Start mapping columns to bean variables");
			columnMapping.put("Service Name", "serviceName");
			columnMapping.put("Services", "serviceCsv");
			columnMapping.put("facilities", "facilityCsv");
			columnMapping.put("sport_facilities", "sportFacilitiesCsv");

			HeaderColumnNameTranslateMappingStrategy<InstituteServiceDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(InstituteServiceDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<InstituteServiceDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.serviceList = csvToBean.parse(beanStrategy, reader);
			this.serviceList.parallelStream().forEach(service -> {
				//expand service list
				if(!StringUtils.isEmpty(service.getServiceCsv())) {
					List<ServiceCsvDto> services = Arrays.asList(service.getServiceCsv().split(","))
					.parallelStream()
					.map(name -> new ServiceCsvDto(StringUtils.trim(name),StringUtils.trim(name)))
					.collect(Collectors.toList());
					if(!CollectionUtils.isEmpty(services)) {
						this.expandedServiceList.addAll(services);						
					}
				}
				//expand facility list
				if(!StringUtils.isEmpty(service.getFacilityCsv())) {
					List<ServiceCsvDto> services = Arrays.asList(service.getFacilityCsv().split(","))
					.parallelStream()
					.map(name -> new ServiceCsvDto(StringUtils.trim(name),StringUtils.trim(name)))
					.collect(Collectors.toList());
					if(!CollectionUtils.isEmpty(services)) {
						this.expandedServiceList.addAll(services);						
					}
				}
				//expand sport facility list
				if(!StringUtils.isEmpty(service.getSportFacilitiesCsv())) {
					List<ServiceCsvDto> services = Arrays.asList(service.getSportFacilitiesCsv().split(","))
					.parallelStream()
					.map(name -> new ServiceCsvDto(StringUtils.trim(name),StringUtils.trim(name)))
					.collect(Collectors.toList());
					if(!CollectionUtils.isEmpty(services)) {
						this.expandedServiceList.addAll(services);						
					}
				}
				//expand by service name
				if(!StringUtils.isEmpty(service.getServiceName())) {
					this.expandedServiceList.add(new ServiceCsvDto(service.getServiceName(),service.getServiceName()));
				}
			});
			
			this.expandedServiceList = this.expandedServiceList.parallelStream().distinct().collect(Collectors.toList());
		}catch (Exception e) {
			log.error("Exception while running batch job");
		}
	}

	@Override
	public ServiceCsvDto read() {
		ServiceCsvDto nextService = null;
		 
        if (nextServiceIndex < this.expandedServiceList.size()) {
        	nextService = this.expandedServiceList.get(nextServiceIndex);
        	nextServiceIndex++;
        }
        else {
        	nextServiceIndex = 0;
        }
        return nextService;
	}
	
}
