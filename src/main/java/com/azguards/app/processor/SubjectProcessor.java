package com.azguards.app.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.Subject;
import com.azguards.app.dao.EducationSystemDao;
import com.azguards.app.dao.SubjectDao;
import com.azguards.app.dto.uploader.SubjectCsvDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.institute.SubjectDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.util.PaginationUtil;

@Service
@Transactional
public class SubjectProcessor {

	public static final Logger LOGGER = LoggerFactory.getLogger(SubjectProcessor.class);

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private EducationSystemDao educationSystemDao;

	@Autowired
	private ModelMapper modelMapper;

	public void importSubject(MultipartFile multipartFile) {
		try {
			InputStream inputStream = multipartFile.getInputStream();
			LOGGER.info("Start reading data from inputStream using CSV reader");
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			LOGGER.info("Start mapping columns to bean variables");

			columnMapping.put("Country", "countryName");
			columnMapping.put("State", "stateName");
			columnMapping.put("Education System", "educationSystemName");
			columnMapping.put("Subject Code", "code");
			columnMapping.put("Subject Name", "name");

			HeaderColumnNameTranslateMappingStrategy<SubjectCsvDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(SubjectCsvDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<SubjectCsvDto> csvToBean = new CsvToBean<>();
			LOGGER.info("Start parsing CSV to bean");
			List<SubjectCsvDto> subjectDtoList = csvToBean.parse(beanStrategy, reader);

			LOGGER.info("Going to getAll educationSystem in DB");
			List<EducationSystem> educationSystems = educationSystemDao.getAll();
			if (educationSystems != null) {
				LOGGER.info("Going to save subjects in DB");
				subjectDao.saveAll(dtoToModel(subjectDtoList));
			}
		} catch (IOException e) {
			LOGGER.error("Exception in uploading subjects", e);
			e.printStackTrace();
		}
	}

	private List<Subject> dtoToModel(List<SubjectCsvDto> subjectDtoList) {
		List<Subject> subjects = new ArrayList<>();
		subjectDtoList.stream().forEach(dto->{
			EducationSystem educationSystem = educationSystemDao.findByNameAndCountryNameAndStateName(dto.getEducationSystemName(), dto.getCountryName(), dto.getStateName());
			if (!ObjectUtils.isEmpty(educationSystem)) {
				Subject subject = subjectDao.findByNameAndEducationSystemId(dto.getName(), educationSystem.getId());
				if (ObjectUtils.isEmpty(subject)) {
					subject = new Subject();
					subject.setCreatedBy("API");
					subject.setCreatedOn(new Date());
				}
				subject.setUpdatedBy("API");
				subject.setUpdatedOn(new Date());
				subject.setCode(dto.getCode());
				subject.setName(dto.getName());
				subject.setEducationSystem(educationSystem);
				subjects.add(subject);
			}
		});
		return subjects;
	}

	public PaginationResponseDto<List<SubjectDto>> getAllSubjects(final Integer pageNumber, final Integer pageSize,
			String name, String educationSystemId) throws NotFoundException, InvokeException {
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<Subject> subjectPage = subjectDao.findByNameContainingIgnoreCaseAndEducationSystemId(name,
				educationSystemId, pageable);
		List<SubjectDto> subjectDtos = subjectPage.getContent().stream().map(e -> modelMapper.map(e, SubjectDto.class))
				.collect(Collectors.toList());
		return PaginationUtil.calculatePaginationAndPrepareResponse(subjectPage, subjectDtos);
	}

}
