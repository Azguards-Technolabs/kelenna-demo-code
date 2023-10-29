package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.azguards.app.dto.uploader.CourseCsvDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.azguards.common.lib.exception.IOException;
import com.azguards.common.lib.exception.UploaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseItemReader implements ItemReader<CourseCsvDto>  {
	
	private List<CourseCsvDto> listOfCourseDto;
	private int nextCourseIndex;
	
	public CourseItemReader(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	@SuppressWarnings("deprecation")
	private void loadFile(String fileName) throws IOException, UploaderException, java.io.IOException {
		log.debug("Inside saveCourseData() method");
		InputStream inputStream = null;
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			log.info("Start reading data from inputStream using CSV reader");
			Map<String, String> columnMapping = new HashMap<>();
	        log.info("Start mapping columns to bean variables");
			columnMapping.put("Courses", "name");
			columnMapping.put("World_Ranking", "worldRanking");
			// this will now be course Ranking as per conversation with Mr. Ado
			columnMapping.put("Remarks_", "remarks");
			columnMapping.put("stars", "stars");
			columnMapping.put("course_lang", "language");
			columnMapping.put("description", "description");
			columnMapping.put("abbreviation", "abbreviation");
			columnMapping.put("Country", "countryName");
			columnMapping.put("City", "cityName");
			columnMapping.put("Faculty", "facultyName");
			columnMapping.put("Institution", "instituteName");
			columnMapping.put("Recognition", "recognition");
			columnMapping.put("Recognition_Type", "recognitionType");
			columnMapping.put("Level", "levelCode");
			columnMapping.put("Curriculum", "curriculum");

			columnMapping.put("availbilty", "availabilty");
			columnMapping.put("Website", "website");
			columnMapping.put("Currency_Time", "currencyTime");
			columnMapping.put("Currency", "currency");

			columnMapping.put("all_text", "content");
			columnMapping.put("all_text2", "content2");
			columnMapping.put("all_text3", "content3");
			columnMapping.put("all_text4", "content4");
			columnMapping.put("all_text5", "content5");
			columnMapping.put("all_text6", "content6");
			columnMapping.put("all_text7", "content7");
			columnMapping.put("all_text8", "content8");
			columnMapping.put("all_text9", "content9");
			columnMapping.put("all_text10", "content10");
			columnMapping.put("all_text11", "content11");
			columnMapping.put("all_text12", "content12");
			columnMapping.put("all_text13", "content13");
			columnMapping.put("all_text14", "content14");
			columnMapping.put("all_text15", "content15");
			columnMapping.put("all_text16", "content16");
			columnMapping.put("all_text17", "content17");
			columnMapping.put("all_text18", "content18");
			columnMapping.put("all_text19", "content19");
			columnMapping.put("all_text20", "content20");
			columnMapping.put("all_text21", "content21");
			columnMapping.put("all_text22", "content22");
			columnMapping.put("all_text23", "content23");
			columnMapping.put("all_text24", "content24");
			columnMapping.put("all_text25", "content25");
			
			columnMapping.put("Examination_Board", "examinationBoard");
			columnMapping.put("Domestic_Application_Fee", "domesticApplicationFee");
			columnMapping.put("International_Application_Fee", "internationalApplicationFee");
			columnMapping.put("Domestic_Enrollment_Fee", "domesticEnrollmentFee");
			columnMapping.put("International_Enrollment_Fee", "internationalEnrollmentFee");
			columnMapping.put("Entrance_Exam", "entranceExam");
			columnMapping.put("GLOB_GPA", "globalGpa");
			
		    columnMapping.put("Duration", "duration");
			columnMapping.put("Duration_Time", "durationTime");
			columnMapping.put("Int_Fees", "internationalFee");
			columnMapping.put("Local_Fees", "domesticFee");
			columnMapping.put("Classroom", "classroom");
			columnMapping.put("Online", "online");
			columnMapping.put("Blended", "blended");
			columnMapping.put("Part Time", "partTime");
			columnMapping.put("Full Time", "fullTime");
		
			
			columnMapping.put("IELTS_Overall_Score", "ieltsOverall");
			columnMapping.put("IELTS_Reading", "ieltsReading");
			columnMapping.put("IELTS_Writing", "ieltsWriting");
			columnMapping.put("IELTS_Listening", "ieltsListening");
			columnMapping.put("IELTS_Speaking", "ieltsSpeaking");
			columnMapping.put("TOFEL_Reading", "toflReading");
			columnMapping.put("TOFEL_Writing", "toflWriting");
			columnMapping.put("TOFEL_Listening", "toflListening");
			columnMapping.put("TOFEL_Speaking", "toflSpeaking");
			columnMapping.put("TOFEL_Overall_Score", "toflOverall");
			
			// TODO ignore it for now 
		/*	columnMapping.put("Prerequisite_1_certificate", "coursePreRequisiteCertificate01");
			columnMapping.put("Prerequisite_2_certificate", "coursePreRequisiteCertificate02");
			columnMapping.put("Prerequisite_3_certificate", "coursePreRequisiteCertificate03");
			
			
			columnMapping.put("Prerequiste_1", "preRequisiteSubject01");
			columnMapping.put("Prerequisite_2", "preRequisiteSubject02");
			columnMapping.put("Prerequisite_3", "preRequisiteSubject03");
			columnMapping.put("Prerequisite_4", "preRequisiteSubject04");
			columnMapping.put("Prerequisite_5", "preRequisiteSubject05");
			columnMapping.put("Prerequisite_6", "preRequisiteSubject06");
			columnMapping.put("Prerequisite_7", "preRequisiteSubject07");
			columnMapping.put("Prerequisite_8", "preRequisiteSubject08");
			columnMapping.put("Prerequisite_9", "preRequisiteSubject09");
			columnMapping.put("Prerequisite_10", "preRequisiteSubject10");
			columnMapping.put("Prerequisite_11", "preRequisiteSubject11");
			columnMapping.put("Prerequisite_12", "preRequisiteSubject12");
			columnMapping.put("Prerequisite_13", "preRequisiteSubject13");
			columnMapping.put("Prerequisite_14", "preRequisiteSubject14");
			
			columnMapping.put("Prequisite_1_grade", "preRequisiteSubjectGrade01");
			columnMapping.put("Prerequisite_2_grade", "preRequisiteSubjectGrade02");
			columnMapping.put("Prerequisite_3_grade", "preRequisiteSubjectGrade03");
			columnMapping.put("Prerequisite_4_grade", "preRequisiteSubjectGrade04");
			columnMapping.put("Prerequisite_5_grade", "preRequisiteSubjectGrade05");
			columnMapping.put("Prerequisite_6_grade", "preRequisiteSubjectGrade06");
			columnMapping.put("Prerequisite_7_grade", "preRequisiteSubjectGrade07");
			columnMapping.put("Prerequisite_8_grade", "preRequisiteSubjectGrade08");
			columnMapping.put("Prerequisite_9_grade", "preRequisiteSubjectGrade09");
			columnMapping.put("Prerequisite_10_grade", "preRequisiteSubjectGrade10");
			columnMapping.put("Prerequisite_11_grade", "preRequisiteSubjectGrade11");
			columnMapping.put("Prerequisite_12_grade", "preRequisiteSubjectGrade12");
			columnMapping.put("Prerequisite_13_grade", "preRequisiteSubjectGrade13");
			columnMapping.put("Prerequisite_14_grade", "preRequisiteSubjectGrade14"); */
			
					
			HeaderColumnNameTranslateMappingStrategy<CourseCsvDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(CourseCsvDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<CourseCsvDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.listOfCourseDto = csvToBean.parse(beanStrategy, reader);
		} catch (java.io.IOException e) {
			log.error("Exception in Saving Course Data exception {} ", e);
			throw new IOException(e.getMessage());
		} catch (ParseException parseEx) {
			log.error("Exception in Saving Course Data exception {} ", parseEx);
			throw new UploaderException(parseEx.getMessage());
		} finally {
			if (null != reader ) {
				log.info("Closing CSV reader");
				reader.close();
			} 
			
			if (null != inputStream) {
				log.info("Closing input stream");
				inputStream.close();
			}
		}
	}

	@Override
	public CourseCsvDto read() throws  UnexpectedInputException, ParseException, NonTransientResourceException {
		CourseCsvDto nextCourse = null;
        if (nextCourseIndex < this.listOfCourseDto.size()) {
        	nextCourse = this.listOfCourseDto.get(nextCourseIndex);
        	nextCourseIndex++;
        }
        else {
        	nextCourseIndex = 0;
        }
        return nextCourse;
	}
}
