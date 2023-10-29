package com.azguards.app.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobLevel;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingActivity;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.Careers;
import com.azguards.app.bean.Level;
import com.azguards.app.bean.RelatedCareer;
import com.azguards.app.dao.CareerUploadDao;
import com.azguards.app.dao.LevelDao;
import com.azguards.app.dto.uploader.CareerCSVDto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CareerUploadProcessor {

	@Autowired
	private CareerUploadDao careerJobDao;
	
	@Autowired
	private LevelDao levelDao;
	
	public void uploadCareerJobs(MultipartFile multipartFile) {
		log.debug("Inside uploadCareers() data");
		try {
			InputStream inputStream = multipartFile.getInputStream();
			log.info("Reading multipart data and start converting CSV data to bean class");
			CsvToBean<CareerCSVDto> csvToBean = new CsvToBeanBuilder<CareerCSVDto>(new InputStreamReader(inputStream))
	                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
	                .withType(CareerCSVDto.class)
	                .build();
			List<CareerCSVDto> careerJobsDtos = csvToBean.parse();
			if(!CollectionUtils.isEmpty(careerJobsDtos)) {
				log.info("Data parsing done, start massaging data to add careers and jobs in DB");
				dataMassagingForCareers(careerJobsDtos);
			}
		} catch (IOException e) {
			log.error("Exception while uploading Careers having exception "+e);
		}
	}

	private void dataMassagingForCareers(List<CareerCSVDto> careerJobsDtos) {
		log.debug("Inside dataMassagingForCareers() data");
		String career = "";
		String jobName = "";
		Integer courseLevel = 0;
		Careers careersToSave = null;
		CareerJob careerJobs = null;
		log.info("Start iterating career jobs data to make data massaging");
		int i = 1;
		for(CareerCSVDto careerJobsDto : careerJobsDtos) {
			log.info("uploaded ======== {}/{}",++i,careerJobsDtos.size() );
			if(!ObjectUtils.isEmpty(careerJobsDto.getCourseLevel())) {
				log.info("assigning value to courseLevel");
				courseLevel = careerJobsDto.getCourseLevel();
			}
			if(!career.equalsIgnoreCase(careerJobsDto.getCareerList()) && !careerJobsDto.getCareerList().equalsIgnoreCase("-")) {
				log.info("Career name is different, adding new career data in DB");
				career = careerJobsDto.getCareerList();
				log.info("Extracting Career from DB for careerName {}",career);
				careersToSave = careerJobDao.getCareer(career);
				if(ObjectUtils.isEmpty(careersToSave)) {
					log.info("Career is not present in DB, adding new Career in DB");
					careersToSave = new Careers();
					careersToSave.setCareer(career);
					careersToSave.setCreatedBy("API");
					careersToSave.setCreatedOn(new Date());
					log.info("Calling DAO layer to add career in DB");
					careerJobDao.saveCareerList(careersToSave);
				}
			}
			if(!ObjectUtils.isEmpty(careersToSave) && !StringUtils.isEmpty(careerJobsDto.getRelatedCareers()) 
					&& !careerJobsDto.getRelatedCareers().equalsIgnoreCase("-")) {
				log.info("Extracting related careers from DB for careerId {} and relatedCareer {}",careersToSave.getId(), 
						careerJobsDto.getRelatedCareers());
				if(ObjectUtils.isEmpty(careerJobDao.getRelatedCareer(careersToSave.getId(), careerJobsDto.getRelatedCareers()))) {
					log.info("Related Career is not present in DB, adding new Related Career in DB");
					RelatedCareer relatedCareer = new RelatedCareer(careerJobsDto.getRelatedCareers(), careersToSave, "API", new Date());
					log.info("Calling DAO layer to add related careers in DB");
					careerJobDao.saveRelatedCareers(relatedCareer);
				}
			}
			if(!jobName.equalsIgnoreCase(careerJobsDto.getJobs()) && !careerJobsDto.getJobs().equalsIgnoreCase("-")) {
				log.info("Job name is different, adding new jobs data in DB");
				jobName = careerJobsDto.getJobs();
				log.info("Extracting Job from DB for jobName {}",jobName);
				careerJobs = careerJobDao.getJob(careerJobsDto.getJobs());
				if(ObjectUtils.isEmpty(careerJobs)) {
					log.info("Jobs data is not present in DB, adding new Job data in DB");
					careerJobs = new CareerJob(careerJobsDto.getJobs(), careerJobsDto.getJobDescription(), careerJobsDto.getJobResponsibility(), careersToSave, courseLevel,
							new Date(), "API");
					log.info("Calling DAO layer to add job in DB");
					careerJobDao.saveJobsData(careerJobs);
				}
			}
			
			// Saving job course search keyword
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getCourseSearchKeywords())) {
				log.info("Massaging data to save Job Course Search Keywords in DB");
				saveCareerJobCourseSearchKeyword(careerJobs, careerJobsDto.getCourseSearchKeywords());
			}
			// Saving job level
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getLevels())) {
				log.info("Massaging data to save Job Levels in DB");
				saveCareerJobLevel(careerJobs, careerJobsDto.getLevels());
			}
			// Saving Job skills
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getSkills())) {
				log.info("Massaging data to save Job Skills in DB");
				saveCareerJobSkills(careerJobs, careerJobsDto.getSkills());
			}
			// Saving Job Subjects
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getSubjects())) {
				log.info("Massaging data to save Job Subjects in DB");
				saveCareerJobSubjects(careerJobs, careerJobsDto.getSubjects());
			}
			// Saving Job types
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getJobTypes())) {
				log.info("Massaging data to save Job Types in DB");
				saveCareerJobTypes(careerJobs, careerJobsDto.getJobTypes());
			}
			// Saving Job Working Activity
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getWorkActivities())) {
				log.info("Massaging data to save Working Activities in DB");
				saveCareerJobWorkingActivities(careerJobs, careerJobsDto.getWorkActivities());
			}
			// Saving Job Working Style
			if(!ObjectUtils.isEmpty(careerJobs) && !StringUtils.isEmpty(careerJobsDto.getWorkStyles())) {
				log.info("Massaging data to save Working Styles in DB");
				saveCareerJobWorkingStyles(careerJobs, careerJobsDto.getWorkStyles());
			}
		}
	}
	
	private void saveCareerJobCourseSearchKeyword(CareerJob careerJobs, String courseSearchKeyword) {
		log.debug("Inside saveCareerJobCourseSearchKeyword() data");
		if(!courseSearchKeyword.equalsIgnoreCase("-")) {
			log.info("Extracting Job Course Search Keyword having jobId {}, courseSearchKeyword{}",careerJobs.getId(), courseSearchKeyword);
			if(ObjectUtils.isEmpty(careerJobDao.getJobCourseSearchKeyword(careerJobs.getId(), courseSearchKeyword))) {
				log.info("Job Course Search Keyword is not present in DB for passed jobId and searchKeyword, adding new Course Search Keywords in DB");
				CareerJobCourseSearchKeyword careerJobCourseSearchKeyword = new CareerJobCourseSearchKeyword();
				careerJobCourseSearchKeyword.setCareerJobs(careerJobs);
				careerJobCourseSearchKeyword.setCourseSearchKeyword(courseSearchKeyword);
				careerJobCourseSearchKeyword.setCreatedOn(new Date());
				careerJobCourseSearchKeyword.setCreatedBy("API");
				log.info("Calling DAO layer to add Jobs Course Search Keyword in DB");
				careerJobDao.saveJobsCourseSearchKeyword(careerJobCourseSearchKeyword);
			}
		}
	}
	
	private void saveCareerJobLevel(CareerJob careerJobs, String levelCode) {
		log.debug("Inside saveCareerJobLevel() data");
		if(!levelCode.equalsIgnoreCase("-")) {
			log.info("Extracting Level from DB having levelCode {} ",levelCode);
			Level levelFromDB = levelDao.getLevelByLevelCode(levelCode);
			if(!ObjectUtils.isEmpty(levelFromDB)) {
				log.info("Level data fetched from DB, extracting existing job level for jobId {} and levelId {}",
						careerJobs.getId(), levelFromDB.getId());
				if(ObjectUtils.isEmpty(careerJobDao.getJobLevel(careerJobs.getId(), levelFromDB.getId()))) {
					log.info("Job Level is not present in DB for passed jobId and levelId, adding new Job Level in DB");
					CareerJobLevel careerJobLevel = new CareerJobLevel();
					careerJobLevel.setCareerJobs(careerJobs);
					careerJobLevel.setLevel(levelFromDB);
					careerJobLevel.setCreatedBy("API");
					careerJobLevel.setCreatedOn(new Date());
					log.info("Calling DAO layer to add new Job Levels in DB");
					careerJobDao.saveJobsLevel(careerJobLevel);
				}
			}
		}
	}
	
	private void saveCareerJobSkills(CareerJob careerJobs, String skillName) {
		log.debug("Inside saveCareerJobSkills() data");
		
		if (!skillName.equalsIgnoreCase("-")) {
			log.info("Extracting existing job skills for jobId {} and skillName {}", careerJobs.getId(), skillName);
			String name = "";
			String description = "";
			int indexOfNameSeperator =  skillName.indexOf("-");
			if (indexOfNameSeperator>0) {
				if (skillName.length()>indexOfNameSeperator) {
					name = skillName.substring(0, indexOfNameSeperator).strip();
					description = skillName.substring(indexOfNameSeperator+1).strip();			
				}
			}
			if (ObjectUtils.isEmpty(careerJobDao.getJobSkill(careerJobs.getId(), name))) {
				log.info("Job Skill is not present in DB for passed jobId and skillName, adding new Job Skill in DB");
				CareerJobSkill careerJobSkill = new CareerJobSkill();
				careerJobSkill.setCareerJobs(careerJobs);
				careerJobSkill.setSkill(name);
				careerJobSkill.setDescription(description);
				careerJobSkill.setCreatedOn(new Date());
				careerJobSkill.setCreatedBy("API");
				log.info("Calling DAO layer to add new Job Skills in DB");
				careerJobDao.saveJobsSkill(careerJobSkill);
			}
		}
	}
	
	private void saveCareerJobSubjects(CareerJob careerJobs, String subjectName) {
		log.debug("Inside saveCareerJobSubjects() data");
		if(!subjectName.equalsIgnoreCase("-")) {
			log.info("Extracting existing job subjects from DB for jobId {} and subjectName {}",
					careerJobs.getId(), subjectName);
			if(ObjectUtils.isEmpty(careerJobDao.getJobSubject(careerJobs.getId(), subjectName))) {
				log.info("Job Subject is not present in DB for passed jobId and subjectName, adding new Job Subject in DB");
				CareerJobSubject careerJobSubject = new CareerJobSubject();
				careerJobSubject.setCareerJobs(careerJobs);
				careerJobSubject.setSubject(subjectName);
				careerJobSubject.setCreatedBy("API");
				careerJobSubject.setCreatedOn(new Date());
				log.info("Calling DAO layer to add new Job Subjects in DB");
				careerJobDao.saveJobSubject(careerJobSubject);
			}
		}
	}
	
	private void saveCareerJobTypes(CareerJob careerJobs, String jobType) {
		log.debug("Inside saveCareerJobTypes() data");
		if(!jobType.equalsIgnoreCase("-")) {
			log.info("Extracting existing job types from DB for jobId {} and jobType {}",
					careerJobs.getId(), jobType);
			if(ObjectUtils.isEmpty(careerJobDao.getJobType(careerJobs.getId(), jobType))) {
				log.info("Job type is not present in DB for passed jobId and jobType, adding new Job Type in DB");
				CareerJobType careerJobType = new CareerJobType();
				careerJobType.setCareerJobs(careerJobs);
				careerJobType.setJobType(jobType);
				careerJobType.setCreatedBy("API");
				careerJobType.setCreatedOn(new Date());
				log.info("Calling DAO layer to add new Job Types in DB");
				careerJobDao.saveJobType(careerJobType);
			}
		}
	}
	
	private void saveCareerJobWorkingActivities(CareerJob careerJobs, String workActivities) {
		log.debug("Inside saveCareerJobWorkingActivities() data");
		if(!workActivities.equalsIgnoreCase("-")) {
			log.info("Extracting existing job working activity from DB for jobId {} and workActivities {}",
					careerJobs.getId(), workActivities);
			if(ObjectUtils.isEmpty(careerJobDao.getJobWorkingActivity(careerJobs.getId(), workActivities))) {
				log.info("Working Activity is not present in DB for passed jobId and workActivity, adding new Job Type in DB");
				CareerJobWorkingActivity careerJobWorkingActivity = new CareerJobWorkingActivity();
				careerJobWorkingActivity.setCareerJobs(careerJobs);
				careerJobWorkingActivity.setWorkActivities(workActivities);
				careerJobWorkingActivity.setCreatedBy("API");
				careerJobWorkingActivity.setCreatedOn(new Date());
				log.info("Calling DAO layer to add new Working Activity in DB");
				careerJobDao.saveJobWorkingActivity(careerJobWorkingActivity);
			}
		}
	}
	
	private void saveCareerJobWorkingStyles(CareerJob careerJobs, String workStyles) {
		log.debug("Inside saveCareerJobWorkingStyles() data");
		if(!workStyles.equalsIgnoreCase("-")) {
			log.info("Extracting existing job working style from DB for jobId {} and workStyles {}",
					careerJobs.getId(), workStyles);
			if(ObjectUtils.isEmpty(careerJobDao.getJobWorkingStyle(careerJobs.getId(), workStyles))) {
				log.info("Working Style is not present in DB for passed jobId and workStyles, adding new Job Type in DB");
				CareerJobWorkingStyle careerJobWorkingStyle = new CareerJobWorkingStyle();
				careerJobWorkingStyle.setCareerJobs(careerJobs);
				careerJobWorkingStyle.setWorkStyle(workStyles);
				careerJobWorkingStyle.setCreatedBy("API");
				careerJobWorkingStyle.setCreatedOn(new Date());
				log.info("Calling DAO layer to add new Working Styles in DB");
				careerJobDao.saveJobWorkingStyle(careerJobWorkingStyle);
			}
		}
	}
}
