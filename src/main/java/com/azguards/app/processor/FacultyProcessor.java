package com.azguards.app.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.Faculty;
import com.azguards.app.dao.FacultyDao;
import com.azguards.app.util.CDNServerUtil;
import com.azguards.common.lib.dto.institute.FacultyDto;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog

public class FacultyProcessor {

	@Autowired
	private FacultyDao facultyDAO;

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("importFacultyJob")
	private Job job;
	
	@Autowired
	@Qualifier("exportFacultyToElastic")
	private Job exportFacultyJob;
	
	@Transactional
	public void saveFaculty(final FacultyDto facultyDto) {
		log.debug("Inside saveFaculty() method");
		Faculty faculty = new Faculty();
		log.info("saving faculty data in DB having facultyName = "+facultyDto.getName());
		faculty.setName(facultyDto.getName());
		faculty.setIsActive(true);
		faculty.setCreatedBy("API");
		faculty.setCreatedOn(new Date());
		log.info("Calling DAO layer to save faculty data in DB");
		facultyDAO.saveOrUpdateFaculty(faculty);
	}

	@Transactional
	public FacultyDto getFacultyById(final String id) {
		log.debug("Inside getFacultyById() method");
		FacultyDto facultyDto = new FacultyDto();
		log.info("Fetching faculty from DB having facultyId = "+id);
		Faculty faculty = facultyDAO.get(id);
		if (!ObjectUtils.isEmpty(faculty)) {
			log.info("Faculty found in DB hence making response");
			facultyDto.setName(faculty.getName());
			facultyDto.setId(faculty.getId());
			facultyDto.setIcon(CDNServerUtil.getFacultyIconUrl(faculty.getName()));
		}
		return facultyDto;
	}

	@Transactional
	public List<FacultyDto> getAllFaculties() {
		log.debug("Inside getAllFaculties() method");
		List<FacultyDto> facultyDtos = new ArrayList<>();
		log.info("Fetching all faculties from DB");
		List<Faculty> facultiesFromDB = facultyDAO.getAll();
		if(!CollectionUtils.isEmpty(facultiesFromDB)) {
			log.info("FAculties fetched from DB start iterating to make response");
			facultiesFromDB.stream().forEach(faculty -> {
				FacultyDto facultyDto = new FacultyDto();
				facultyDto.setName(faculty.getName());
				facultyDto.setId(faculty.getId());
				facultyDto.setIcon(CDNServerUtil.getFacultyIconUrl(faculty.getName()));
				facultyDtos.add(facultyDto);
			});
		}
		return facultyDtos;
	}
	
	@Transactional
	public FacultyDto getFacultyByFacultyName(String facultyName) {
		log.debug("Inside getFacultyByFacultyName() method");
		FacultyDto facultyDto = new FacultyDto();
		log.info("Fetching faculty from DB having facultyName = "+facultyName);
		Faculty facultyFromDB = facultyDAO.getFacultyByFacultyName(facultyName);
		if(!ObjectUtils.isEmpty(facultyFromDB)) {
			log.info("Faculty coming from DB hence start making response");
			facultyDto.setName(facultyFromDB.getName());
			facultyDto.setId(facultyFromDB.getId());
			try {
				facultyDto.setIcon(CDNServerUtil.getFacultyIconUrl(facultyFromDB.getName()));
			} catch (Exception exception) {
				log.error("Exception while fetching faculty icon URL = "+exception);
			}
		}
		return facultyDto;
	}

	@Transactional
	public List<Faculty> getFacultyListByName(final List<String> facultyNames) {
		return facultyDAO.getFacultyListByFacultyNames(facultyNames);
	}
	
    public void importFaculty(final MultipartFile multipartFile) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    	log.debug("Inside importFaculty() method");
    	log.info("Calling methiod to save faculty data");
		
		File f = File.createTempFile("faculty", ".csv");
		multipartFile.transferTo(f);
		
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("csv-file", f.getAbsolutePath());
		jobParametersBuilder.addString("execution-id", UUID.randomUUID().toString());
		jobLauncher.run(job, jobParametersBuilder.toJobParameters());
	}
    
    public void exportFacultyToElastic() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.debug("Inside exportFacultyToElastic() method");
		jobLauncher.run(exportFacultyJob, new JobParametersBuilder()
                .addLong("time",System.currentTimeMillis(), true).toJobParameters());

	}
}
