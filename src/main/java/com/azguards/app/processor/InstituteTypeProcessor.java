package com.azguards.app.processor;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.InstituteType;
import com.azguards.app.dao.InstituteTypeDao;
import com.azguards.app.dto.InstituteTypeDto;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
@Transactional
public class InstituteTypeProcessor {

	@Autowired
	private InstituteTypeDao iInstituteTypeDAO;

	@Autowired
	@Qualifier("importInstituteTypeJob")
	Job job;

	@Autowired
	JobLauncher jobLauncher;	
	
	@Value("${s3.url}")
	private String s3BaseUrl;
	
	@Value("${s3.institute-type.image.location}")
	private String instituteTypeImageLocation;

	public void addUpdateInstituteType(InstituteTypeDto instituteTypeDto) {
		log.debug("Inside getInstituteTypeByCountryName() method");
		InstituteType instituteType = new InstituteType();
		log.info("Copying bean class to DTO class");
		BeanUtils.copyProperties(instituteTypeDto, instituteType);
		Date today = new Date();
		instituteType.setCreatedOn(today);
		instituteType.setUpdatedOn(today);
		instituteType.setCreatedBy("API");
		instituteType.setUpdatedBy("API");
		instituteType.setIsActive(true);
		log.info("Calling DAO layer to save instituteType in DB");
		iInstituteTypeDAO.save(instituteType);
	}

	public InstituteType getInstituteType(String id) {
		return iInstituteTypeDAO.get(id);
	}

	public List<InstituteTypeDto> getAllInstituteType() {
		return iInstituteTypeDAO.getAll();
	}

	public List<InstituteTypeDto> getInstituteTypeByCountryName(String countryName) {
		log.debug("Inside getInstituteTypeByCountryName() method");
		List<InstituteTypeDto> listOfInstituteDto = new ArrayList<>();
		log.info("Fetching InstituteType from DB having countryName = " + countryName);
		List<InstituteType> listOfInstituteType = iInstituteTypeDAO.getByCountryName(countryName);
		if (!CollectionUtils.isEmpty(listOfInstituteType)) {
			log.info("InstituteType is not null then start iterating instituteType");
			listOfInstituteType.stream().forEach(instituteType -> {
				InstituteTypeDto instituteTypeDto = new InstituteTypeDto();
				instituteTypeDto.setInstituteTypeId(instituteType.getId());
				instituteTypeDto.setCountryName(instituteType.getCountryName());
				instituteTypeDto.setDescription(instituteType.getDescription());
				instituteTypeDto.setInstituteTypeName(instituteType.getName());
				log.info("Adding instiuteType in list");
				listOfInstituteDto.add(instituteTypeDto);
			});
		}
		log.info("returning instituteType in final response");
		return listOfInstituteDto;
	}

	public void importInstituteType(final MultipartFile multipartFile) throws IOException, ParseException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.debug("Inside importInstitute() method");
		log.info("Calling methiod to save instituteData");

		File f = File.createTempFile("InstituteTypes", ".csv");
		multipartFile.transferTo(f);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("csv-file", f.getAbsolutePath());
		jobParametersBuilder.addString("execution-id", "InstituteTypeUploader-" + UUID.randomUUID().toString());
		jobLauncher.run(job, jobParametersBuilder.toJobParameters());
	}

	public List<com.azguards.common.lib.enumeration.InstituteType> getInstituteTypes() {
		return Arrays.asList(com.azguards.common.lib.enumeration.InstituteType.values()).stream().map(e -> {
			if (!e.isInitialized()) {
				e.setIcon(createInstituteTypeImageURL(e.getIcon()));
				e.setInitialized(true);
			}
			return e;
		}).collect(Collectors.toList());
	}
	
	private String createInstituteTypeImageURL(String name) {
		return new StringBuffer().append(s3BaseUrl).append(instituteTypeImageLocation).append(name).toString();
	}

}
