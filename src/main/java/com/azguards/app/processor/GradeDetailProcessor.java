package com.azguards.app.processor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.GradeDetails;
import com.azguards.app.dao.EducationSystemDao;
import com.azguards.app.dao.GradeDao;
import com.azguards.app.dto.uploader.GradeCSVDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.dto.institute.GradeDto;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
@Transactional
public class GradeDetailProcessor {

	@Autowired
	private GradeDao gradeDao;

	@Autowired
	private EducationSystemDao educationSystemDAO;

	public Double calculateGrade(final GradeDto gradeDto) {
		log.debug("Inside calculateGrade() method");
		Double averageGpa = 0.0;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			Double gpaGrade = 0.0;
			List<String> gpaGrades = new ArrayList<>();
			for (String grade : gradeDto.getSubjectGrades()) {
				log.info("Fetching grade details from DB having countryName = " + gradeDto.getCountryName()
						+ " and systemId = " + gradeDto.getEducationSystemId() + "and grade = " + grade);
				gpaGrades.add(
						gradeDao.getGradeDetails(gradeDto.getCountryName(), gradeDto.getEducationSystemId(), grade));
			}
			for (String grade : gpaGrades) {
				gpaGrade = gpaGrade + Double.valueOf(grade);
			}
			averageGpa = gpaGrade / gpaGrades.size();
			if (averageGpa != null) {
				averageGpa = Double.valueOf(decimalFormat.format(averageGpa));
			}
		} catch (Exception exception) {
			log.error("Exception while calculating grade having exception = " + exception);
		}
		return averageGpa;
	}

	public void uploadGrade(final MultipartFile multipartFile) {
		log.debug("Inside uploadGrade() method");
		try {
			InputStream inputStream = multipartFile.getInputStream();
			log.info("Start reading data from inputStream using CSV reader");
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			log.info("Start mapping columns to bean variables");
			columnMapping.put("country", "country");
			columnMapping.put("education_system", "educationSystem");
			columnMapping.put("state", "stateName");
			columnMapping.put("grade1", "grade1");
			columnMapping.put("grade2", "grade2");
			columnMapping.put("grade3", "grade3");
			columnMapping.put("grade4", "grade4");
			columnMapping.put("grade5", "grade5");
			columnMapping.put("grade6", "grade6");
			columnMapping.put("grade7", "grade7");
			columnMapping.put("grade8", "grade8");
			columnMapping.put("grade9", "grade9");
			columnMapping.put("grade10", "grade10");
			columnMapping.put("grade11", "grade11");
			columnMapping.put("grade12", "grade12");
			columnMapping.put("grade13", "grade13");
			columnMapping.put("grade14", "grade14");
			columnMapping.put("grade15", "grade15");
			columnMapping.put("grade16", "grade16");

			columnMapping.put("GPA Grades1", "gPaGrades1");
			columnMapping.put("GPA Grades2", "gPaGrades2");
			columnMapping.put("GPA Grades3", "gPaGrades3");
			columnMapping.put("GPA Grades4", "gPaGrades4");
			columnMapping.put("GPA Grades5", "gPaGrades5");
			columnMapping.put("GPA Grades6", "gPaGrades6");
			columnMapping.put("GPA Grades7", "gPaGrades7");
			columnMapping.put("GPA Grades8", "gPaGrades8");
			columnMapping.put("GPA Grades9", "gPaGrades9");
			columnMapping.put("GPA Grades10", "gPaGrades10");
			columnMapping.put("GPA Grades11", "gPaGrades11");
			columnMapping.put("GPA Grades12", "gPaGrades12");
			columnMapping.put("GPA Grades13", "gPaGrades13");
			columnMapping.put("GPA Grades14", "gPaGrades14");
			columnMapping.put("GPA Grades15", "gPaGrades15");
			columnMapping.put("GPA Grades16", "gPaGrades16");

			HeaderColumnNameTranslateMappingStrategy<GradeCSVDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(GradeCSVDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<GradeCSVDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			List<GradeCSVDto> gadeDtoList = csvToBean.parse(beanStrategy, reader);
			if (gadeDtoList != null && gadeDtoList.size() > 0) {
				log.info("if gradeDTO is not null or empty then start adding grades in DB");
				saveGrades(gadeDtoList);
			}
			log.info("Closing CSV reader");
			reader.close();
			log.info("Closing input stream");
			inputStream.close();
		} catch (Exception exception) {
			log.error("Exception in Uploading Grade exception {} ", exception);
		}
	}

	private void saveGrades(List<GradeCSVDto> gradeDtos) {
		List<GradeDetails> gradeDetails = new ArrayList<>();
		for (GradeCSVDto dto : gradeDtos) {
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade1(),
					dto.getGpaGrades1(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade2(),
					dto.getGPaGrades2(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade3(),
					dto.getGPaGrades3(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade4(),
					dto.getGPaGrades4(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade5(),
					dto.getGPaGrades5(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade6(),
					dto.getGPaGrades6(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade7(),
					dto.getGPaGrades7(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade8(),
					dto.getGPaGrades8(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade9(),
					dto.getGPaGrades9(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade10(),
					dto.getGPaGrades10(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade11(),
					dto.getGPaGrades11(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade12(),
					dto.getGPaGrades12(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade13(),
					dto.getGPaGrades13(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade14(),
					dto.getGPaGrades14(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade15(),
					dto.getGPaGrades15(), dto.getStateName()));
			gradeDetails.add(getGradeObject(dto.getCountry(), dto.getEducationSystem(), dto.getGrade16(),
					dto.getGPaGrades16(), dto.getStateName()));
		}
		gradeDao.saveAll(gradeDetails);
	}

	private GradeDetails getGradeObject(String country, String educationSystemName, String grade, String gpaGrade,
			String stateName) {
		EducationSystem educationSystem = educationSystemDAO.findByNameAndCountryNameAndStateName(educationSystemName,
				country, stateName);
		GradeDetails gradeDetail = gradeDao.findByCountryNameAndStateNameAndGradeAndEducationSystem(country,
				stateName, grade, educationSystem);
		if (ObjectUtils.isEmpty(gradeDetail)) {
			gradeDetail = new GradeDetails();
			gradeDetail.setCreatedBy("AUTO");
			gradeDetail.setCreatedOn(new Date());
		}
		gradeDetail.setCountryName(country);
		gradeDetail.setEducationSystem(educationSystem);
		gradeDetail.setStateName(stateName);
		gradeDetail.setUpdatedBy("AUTO");
		gradeDetail.setUpdatedOn(new Date());
		gradeDetail.setGpaGrade(gpaGrade);
		gradeDetail.setGrade(grade);
		return gradeDetail;
	}
}
