package com.azguards.app.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.GradeDetails;
import com.azguards.app.bean.Level;
import com.azguards.app.bean.Subject;
import com.azguards.app.dao.EducationSystemDao;
import com.azguards.app.dao.GradeDao;
import com.azguards.app.dao.LevelDao;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.institute.EducationSystemDto;
import com.azguards.common.lib.dto.institute.GradeDto;
import com.azguards.common.lib.dto.institute.LevelDto;
import com.azguards.common.lib.dto.institute.SubjectDto;
import com.azguards.common.lib.enumeration.GradeType;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class EducationSystemProcessor {

	@Autowired
	private EducationSystemDao educationSystemDAO;

	@Autowired
	private LevelDao levelDao;
	
	@Autowired
	private GradeDao gradeDao;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageTranslator messageTranslator;

	@Transactional
	public List<EducationSystemDto> getEducationSystemsByCountryName(final String countryName) {
		log.debug("Inside getEducationSystemsByCountryId() method");
		List<EducationSystemDto> educationSystemDtos = new ArrayList<>();
		List<SubjectDto> subjectDtos = new ArrayList<>();
		log.info("Fetching education system from DB based on countryName = " + countryName);
		List<EducationSystem> educationSystems = educationSystemDAO.getEducationSystemsByCountryName(countryName);
		if (!CollectionUtils.isEmpty(educationSystems)) {
			log.info("Education system fetched from DB based on countryName");
			educationSystems.stream().forEach(educationSystem -> {
				log.info("Fetching education subject from DB based on educationSystemIn = "+educationSystem.getId());
				List<Subject> subjects = educationSystemDAO.getSubjectByEducationSystem(educationSystem.getId());
				if(!CollectionUtils.isEmpty(subjects)) {
					log.info("Subjects fetched from DB start iterating data");
					subjects.stream().forEach(subject -> {
						SubjectDto subjectDto = new SubjectDto(subject.getId(), subject.getName());
						subjectDtos.add(subjectDto);
					});
				}
				log.info("Adding values in esducation system DTO");
				EducationSystemDto educationSystemDto = new EducationSystemDto(educationSystem.getId(),
						educationSystem.getCountryName(), educationSystem.getName(), educationSystem.getCode(),
						educationSystem.getDescription(), educationSystem.getStateName(), subjectDtos, null, null, null, null,null,
						modelMapper.map(educationSystem.getLevel(), LevelDto.class));
				educationSystemDtos.add(educationSystemDto);

			});
		}
		return educationSystemDtos;
	}

	public void saveEducationSystems(final EducationSystemDto educationSystem) throws ValidationException, NotFoundException {
		log.debug("Inside saveEducationSystems() method");
		Level level = levelDao.getLevel(educationSystem.getLevelId());
		if (ObjectUtils.isEmpty(level)) {
			log.info(messageTranslator.toLocale("system.level.id.notfound",educationSystem.getLevelId(),Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("system.level.id.notfound",educationSystem.getLevelId()));
		}
		if (!ObjectUtils.isEmpty(educationSystem) && !StringUtils.isEmpty(educationSystem.getId())) {
			log.info("Education system Id found in request, hence Fetching education system from"
					+ "	DB based on educationSystemId = " + educationSystem.getId());
			EducationSystem educationSystemFromDB = educationSystemDAO.get(educationSystem.getId());
			if (educationSystemFromDB != null) {
				log.info("Education system fetched from DB, going to update existing educationSystem");
				if (!StringUtils.isEmpty(educationSystem.getCountryName())) {
					educationSystemFromDB.setUpdatedBy("API");
					educationSystemFromDB.setUpdatedOn(new Date());
					educationSystemFromDB.setName(educationSystem.getName());
					educationSystemFromDB.setDescription(educationSystem.getDescription());
					educationSystemFromDB.setCode(educationSystem.getCode());
					educationSystemFromDB.setLevel(level);
					educationSystemFromDB.setCountryName(educationSystem.getCountryName());
					educationSystemDAO.save(educationSystemFromDB);
				} else {
					log.error(messageTranslator.toLocale("system.country.name.required",Locale.US));
					throw new ValidationException(messageTranslator.toLocale("system.country.name.required"));
				}
			} else {
				log.error(messageTranslator.toLocale("system.id.notfound",educationSystem.getId(),Locale.US));
				throw new NotFoundException(
						messageTranslator.toLocale("system.id.notfound",educationSystem.getId()));
			}
		} else {
			log.info("Education system Id not found in request, hencce going to save new education system in DB");
			if (!StringUtils.isEmpty(educationSystem.getCountryName())) {
				log.info("Start adding new educationSystem in DB");
				EducationSystem system = new EducationSystem();
				system.setCode(educationSystem.getCode());
				system.setCountryName(educationSystem.getCountryName());
				system.setCreatedBy("API");
				system.setCreatedOn(new Date());
				system.setDescription(educationSystem.getDescription());
				system.setIsActive(true);
				system.setName(educationSystem.getName());
				system.setLevel(level);
				educationSystemDAO.save(system);
			} else {
				log.error(messageTranslator.toLocale("system.country.name.required",Locale.US));
				throw new ValidationException(messageTranslator.toLocale("system.country.name.required"));
			}
		}
	}

	public Double calculateGrade(final GradeDto gradeDto) {
		log.debug("Inside calculateGrade() method");
		Double averageGpa = 0.0;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			Double gpaGrade = 0.0;
			List<String> gpaGrades = new ArrayList<>();
			for (String grade : gradeDto.getSubjectGrades()) {
				log.info("Fetching grade details from DB having countryName = "+ gradeDto.getCountryName() +
						" and systemId = " + gradeDto.getEducationSystemId() + "and grade = "+grade);
				gpaGrades.add(gradeDao.getGradeDetails(gradeDto.getCountryName(), gradeDto.getEducationSystemId(), grade));
			}
			for (String grade : gpaGrades) {
				gpaGrade = gpaGrade + Double.valueOf(grade);
			}
			averageGpa = gpaGrade / gpaGrades.size();
			if (averageGpa != null) {
				averageGpa = Double.valueOf(decimalFormat.format(averageGpa));
			}
		} catch (Exception exception) {
			log.error("Exception while calculating grade having exception = "+exception);
		}
		return averageGpa;
	}

	public List<GradeDto> getGrades(final String countryName, final String systemId) {
		log.debug("Inside getGrades() method");
		List<GradeDto> gradeDtos = new ArrayList<>();
		log.info("Fetching Grade details from DB having countryName =" + countryName + " and systemId ="+systemId);
		List<GradeDetails> grades = gradeDao.getGrades(countryName, systemId);
		 if(!CollectionUtils.isEmpty(grades)) {
			 log.info("Grade details fetched from DB, start iterating data to make final response");
			 grades.stream().forEach(grade -> {
				 GradeDto gradeDto = new GradeDto(grade.getId(), grade.getCountryName(), systemId, null ,
						 grade.getGrade(), grade.getGpaGrade());
				 gradeDtos.add(gradeDto);
			 });
		 }
		return gradeDtos;
	}
	
	public List<EducationSystemDto> getEducationSystemByCountryNameAndStateName(String countryName, String stateName) {
		return educationSystemDAO.getEducationSystemByCountryNameAndStateName(countryName, stateName);
	}
	
	@Transactional
	public EducationSystemDto getEducationSystemById(String educationSystemId) {
		EducationSystemDto educationSystemDto = new EducationSystemDto();
		EducationSystem educationSystem =  educationSystemDAO.get(educationSystemId);
		if (!ObjectUtils.isEmpty(educationSystem)) {
			educationSystemDto = new EducationSystemDto(educationSystem.getId(),
					educationSystem.getCountryName(), educationSystem.getName(), educationSystem.getCode(),
					educationSystem.getDescription(), educationSystem.getStateName(), null, null, null, null, null,null,
					modelMapper.map(educationSystem.getLevel(), LevelDto.class));
		}
		return educationSystemDto;
	}
	
	public void importEducationSystem(final MultipartFile multipartFile) {
		try {
			InputStream inputStream = multipartFile.getInputStream();
			log.info("Start reading data from inputStream using CSV reader");
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			log.info("Start mapping columns to bean variables");
			columnMapping.put("country", "countryName");
			columnMapping.put("education_system", "name");
			columnMapping.put("state", "stateName");
			columnMapping.put("Grades_Display", "gradeTypeCode");
			columnMapping.put("Student_TYPE", "levelCode");
			
			HeaderColumnNameTranslateMappingStrategy<EducationSystemDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(EducationSystemDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<EducationSystemDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			List<EducationSystemDto> educationSystemList = csvToBean.parse(beanStrategy, reader);
			if (educationSystemList != null && educationSystemList.size() > 0) {
				log.info("if educationSystemList is not null or empty then start adding educationSystem in DB");
				saveEducationSystems(educationSystemList);
			}
			log.info("Closing CSV reader");
			reader.close();
			log.info("Closing input stream");
			inputStream.close();
		} catch (IOException e) {
			log.error("Exception in importEducationSystem {}",e);
		}
	}
	
	private void saveEducationSystems(List<EducationSystemDto> educationSystemDtos) {
		List<EducationSystem> educationSystems = educationSystemDtos.stream().map(dto -> {
			Level level = levelDao.getLevelByLevelCode(dto.getLevelCode());
			EducationSystem system = educationSystemDAO.findByNameAndCountryNameAndStateName(dto.getName(), dto.getCountryName(), dto.getStateName());
			if (ObjectUtils.isEmpty(system)) {
				system = new EducationSystem();
				system.setCreatedBy("API");
				system.setCreatedOn(new Date());
			}
			system.setCode(dto.getName()); // its intentional to store code and description similiar to name
			system.setGradeType(GradeType.valueOf(dto.getGradeTypeCode()));
			system.setStateName(dto.getStateName());
			system.setCountryName(dto.getCountryName());
			system.setUpdatedBy("API");
			system.setUpdatedOn(new Date());
			system.setDescription(dto.getName());
			system.setIsActive(true);
			system.setName(dto.getName());
			system.setLevel(level);
			return system;
		}).collect(Collectors.toList());
		educationSystemDAO.saveAll(educationSystems);
	}
}
