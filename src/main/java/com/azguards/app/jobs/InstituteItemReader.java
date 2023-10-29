package com.azguards.app.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.util.ObjectUtils;

import com.azguards.app.dto.uploader.InstituteCsvDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstituteItemReader implements ItemReader<InstituteCsvDto> {
	
	private int nextInstituteIndex;
	List<InstituteCsvDto> instituteList;
	
	public InstituteItemReader(String fileName) throws IOException {
		log.info("Read from file {}",fileName);
		loadFile(fileName);
	}
	
	private void loadFile(String fileName) throws IOException {
		InputStream inputStream = null;
		CSVReader reader = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			log.info("Start reading data from inputStream using CSV reader");
			reader = new CSVReader(new InputStreamReader(inputStream));
			Map<String, String> columnMapping = new HashMap<>();
			log.info("Start mapping columns to bean variables");
			columnMapping.put("Type_of_Institution", "instituteType");
			columnMapping.put("Institution", "name");
			columnMapping.put("Country", "countryName");
			columnMapping.put("City", "cityName");
			columnMapping.put("Accredited", "accreditation");
			columnMapping.put("Int_Ph_num", "phoneNumber");
			columnMapping.put("Int_Emails", "email");
			columnMapping.put("Website", "website");
			columnMapping.put("T_num_of_stu", "totalStudent");
			columnMapping.put("Latitude", "latitude");
			columnMapping.put("Longitude", "longitude");
			columnMapping.put("Address", "address");
			columnMapping.put("Type", "type");
			columnMapping.put("Ttion_fees_p_plan", "tuitionFessPaymentPlan");
			columnMapping.put("course_start", "courseStart");

			columnMapping.put("Campus", "campusName");
			columnMapping.put("Course_Type", "levelCode");
			columnMapping.put("Faculty", "facultyName");
			columnMapping.put("avg_cost_of_living", "avgCostOfLiving");

			columnMapping.put("institute_description", "description");
			columnMapping.put("Enrolment_Link", "enrolmentLink");
			columnMapping.put("About_Us_Info", "aboutInfo");
			columnMapping.put("whatsapp_no", "whatsNo");
			columnMapping.put("Opening_hour", "openingFrom");
			columnMapping.put("Closing_hour", "openingTo");
			columnMapping.put("Schlr_finan_asst", "scholarshipFinancingAssistance");

			columnMapping.put("World_Ranking", "worldRanking");
			columnMapping.put("Domestic_Ranking", "domesticRanking");
			columnMapping.put("admission_email", "admissionEmail");
			columnMapping.put("boarding_available", "boardingAvailable");
			columnMapping.put("boarding", "boarding");
			columnMapping.put("state", "state");
			columnMapping.put("postal_code", "postalCode");
			columnMapping.put("facilities", "facilities");
			columnMapping.put("sport_facilities", "sportFacilities");
			columnMapping.put("Services", "services");

			columnMapping.put("english_partners", "englishPartners");
			columnMapping.put("IMG_COUNT", "imageCount");
			columnMapping.put("climate_2", "climate");
			columnMapping.put("YOUTUBE_Link", "youtubeLink");
			columnMapping.put("International_Phone_Number", "internationalPhoneNumber");
			columnMapping.put("Domestic_Phone_Number", "domesticPhoneNumber");
			
			columnMapping.put("Intake_1", "intake_1");
			columnMapping.put("Intake_2", "intake_2");
			columnMapping.put("Intake_3", "intake_3");
			columnMapping.put("Intake_4", "intake_4");
			columnMapping.put("Intake_5", "intake_5");
			columnMapping.put("Intake_6", "intake_6");
			columnMapping.put("Intake_7", "intake_7");
			columnMapping.put("Intake_8", "intake_8");
			columnMapping.put("Intake_9", "intake_9");
			columnMapping.put("Intake_10", "intake_10");
			columnMapping.put("Intake_11", "intake_11");
			columnMapping.put("Intake_12", "intake_12");
			
			columnMapping.put("Monday", "monday");
			columnMapping.put("Tuesday", "tuesday");
			columnMapping.put("Wednesday", "wednesday");
			columnMapping.put("Thursday", "thursday");
			columnMapping.put("Friday", "friday");
			columnMapping.put("Saturday", "saturday");
			columnMapping.put("Sunday", "sunday");

			//Service specific data
			columnMapping.put("Personal_Coun_acad", "personalCount");
			columnMapping.put("Visa_Work_Benefits", "visaWorkBenefits");
			columnMapping.put("Emp_career_dev", "empCareerDev");
			columnMapping.put("Study_Library_Support", "studyLibrarySupport");
			columnMapping.put("Health_services", "healthServices");
			columnMapping.put("Disability_Support", "disabilitySupport");
			columnMapping.put("Childcare_Centre", "childcareCentre");
			columnMapping.put("Cult_incl_antiracism_prg", "cultInclAntiracismPrg");
			columnMapping.put("Tech_Serv", "techServ");
			columnMapping.put("Accommodation", "accommodation");
			columnMapping.put("Medical", "medical");
			columnMapping.put("Leg_Service", "legService");
			columnMapping.put("Acct_Serv", "acctServ");
			columnMapping.put("Bus", "bus");
			columnMapping.put("Train", "train");
			columnMapping.put("Airport_Pickup", "airportPickup");
			columnMapping.put("Swimming_pool", "swimmingPool");
			columnMapping.put("Sports_Center", "sportCenter");
			columnMapping.put("Sport_Teams", "sprotTeam");
			columnMapping.put("Housing_Services", "housingServices");
			columnMapping.put("admin_service_fee", "adminServiceFee");
			columnMapping.put("paymt_plan", "paymentPlan");
			columnMapping.put("govt_loan", "governmentLoan");
			columnMapping.put("scholarship", "scholarship");

			HeaderColumnNameTranslateMappingStrategy<InstituteCsvDto> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
			beanStrategy.setType(InstituteCsvDto.class);
			beanStrategy.setColumnMapping(columnMapping);
			CsvToBean<InstituteCsvDto> csvToBean = new CsvToBean<>();
			log.info("Start parsing CSV to bean");
			csvToBean.setCsvReader(reader);
			csvToBean.setMappingStrategy(beanStrategy);
			this.instituteList = csvToBean.parse(beanStrategy, reader);
		}catch (Exception e) {
			log.error("Exception while running batch job");
		} finally {
			if (null !=inputStream) {
				inputStream.close();
			}
			if (null != reader) {
				reader.close();
			}
			
		}
	}

	@Override
	public InstituteCsvDto read() {
		InstituteCsvDto nextInstitute = null;
		if(ObjectUtils.isEmpty(this.instituteList)) {
			 return nextInstitute;
		}
        if (nextInstituteIndex < this.instituteList.size()) {
        	nextInstitute = this.instituteList.get(nextInstituteIndex);
        	nextInstituteIndex++;
        }
        else {
        	nextInstituteIndex = 0;
        }
        return nextInstitute;
	}
	
}
