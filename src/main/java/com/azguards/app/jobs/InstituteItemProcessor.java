package com.azguards.app.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteAdditionalInfo;
import com.azguards.app.bean.InstituteFacility;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.bean.InstituteService;
import com.azguards.app.bean.Service;
import com.azguards.app.bean.Timing;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dao.InstituteTypeDao;
import com.azguards.app.dao.ServiceDao;
import com.azguards.app.dto.uploader.InstituteCsvDto;
import com.azguards.app.util.IConstant;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.ConstraintVoilationException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.util.DateUtil;
import com.azguards.common.lib.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstituteItemProcessor implements ItemProcessor<InstituteCsvDto, Institute> {

	@Autowired
	InstituteDao instituteDao;
	
	@Autowired
	InstituteTypeDao instituteTypeDao;
	
	Map<String, Service> services = new HashMap<>();

	@Autowired
	ServiceDao serviceDao;
	
	@Override
	public Institute process(InstituteCsvDto instituteDto) throws Exception {
		Institute institute = new Institute();
		try {
			this.services = serviceDao.getAll().stream().collect(Collectors.toMap(Service::getName, service -> service))
					.entrySet().parallelStream().collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(), Map.Entry::getValue));
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			institute.setCreatedOn(dateFormat.parse(dateFormat.format(new Date())));
			if (!StringUtils.isEmpty(instituteDto.getType())) {
				institute.setInstituteCategoryType(instituteDao.getInstituteCategoryType(instituteDto.getType()));
			}
			if(!instituteDto.getInstituteType().isEmpty()
					&& !ObjectUtils.isEmpty(instituteTypeDao.getInstituteTypeByNameAndCountry(instituteDto.getInstituteType(), instituteDto.getCountryName()))) {
//				institute.setInstituteType(instituteDto.getInstituteType());
				institute.setInstituteType(Arrays.asList(instituteDto.getInstituteType()));
			} else {
				log.error("Institute type {} not found for institute {}",instituteDto.getInstituteType(), instituteDto.getName());
				throw new NotFoundException(String.format("Institute type %s not found for institute %s",instituteDto.getInstituteType(), instituteDto.getName())); 
			}
			institute.setId(UUID.randomUUID().toString());
			institute.setCityName(instituteDto.getCityName());
			institute.setCountryName(StringUtils.trim(instituteDto.getCountryName()));
			institute.setName(instituteDto.getName());
			setReadableIdForInsitute(institute);
			institute.setWorldRanking(instituteDto.getWorldRanking());
			institute.setAccreditation(instituteDto.getAccreditation());
			institute.setAddress(instituteDto.getAddress());
			institute.setWebsite(instituteDto.getWebsite());
			institute.setPhoneNumber(instituteDto.getPhoneNumber());
			institute.setEmail(instituteDto.getEmail());
			institute.setAboutInfo(instituteDto.getAboutInfo());
			institute.setTuitionFeesPaymentPlan(instituteDto.getTuitionFeesPaymentPlan());
			institute.setEnrolmentLink(instituteDto.getEnrolmentLink());
			institute.setCourseStart(instituteDto.getCourseStart());
			institute.setWhatsNo(instituteDto.getWhatsNo());
			institute.setScholarshipFinancingAssistance(instituteDto.getScholarshipFinancingAssistance());
			institute.setEnglishPartners(instituteDto.getEnglishPartners());
			institute.setClimate(instituteDto.getClimate());
			institute.setImageCount(instituteDto.getImageCount());
			institute.setYoutubeLink(instituteDto.getYoutubeLink());
			institute.setDomesticPhoneNumber(instituteDto.getDomesticPhoneNumber());
			institute.setInternationalPhoneNumber(instituteDto.getInternationalPhoneNumber());
			if (instituteDto.getLatitude() != null) {
				if (instituteDto.getLatitude().contains(",")) {
					institute.setLatitude(Double.parseDouble(instituteDto.getLatitude().replace(",", "")));
				} else {
					institute.setLatitude(Double.parseDouble(instituteDto.getLatitude()));
				}
			}
			if (instituteDto.getLongitude() != null) {
				if (instituteDto.getLongitude().contains(",")) {
					institute.setLongitude(Double.parseDouble(instituteDto.getLongitude().replace(",", "")));
				} else {
					institute.setLongitude(Double.parseDouble(instituteDto.getLongitude()));
				}
			}
			
			if (!ObjectUtils.isEmpty(instituteDto.getTotalStudent())) {
				institute.setInstituteAdditionalInfo(getInstituteAdditionalInfo(institute, instituteDto));
			}
			institute.setAvgCostOfLiving(instituteDto.getAvgCostOfLiving());
			institute.setCreatedBy("AUTO");
			institute.setIsActive(true);
			institute.setUpdatedBy("AUTO");
			institute.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
					
			if(!ObjectUtils.isEmpty(instituteDto.getWorldRanking())) {
				institute.setWorldRanking(instituteDto.getWorldRanking());
			}
					
			if(!ObjectUtils.isEmpty(instituteDto.getDomesticRanking())) {
				institute.setDomesticRanking(instituteDto.getDomesticRanking());
			}
					
			if(!ObjectUtils.isEmpty(instituteDto.getAdmissionEmail())) {
				institute.setAdmissionEmail(instituteDto.getAdmissionEmail());
			}
					
			if(!ObjectUtils.isEmpty(instituteDto.getBoarding())) {
				institute.setBoarding(instituteDto.getBoarding());
			}
			
			if(!ObjectUtils.isEmpty(instituteDto.getState())) {
				institute.setState(instituteDto.getState());
			}
				
			if(!ObjectUtils.isEmpty(instituteDto.getPostalCode())) {
				institute.setPostalCode(instituteDto.getPostalCode());
			}
				
			if(!ObjectUtils.isEmpty(instituteDto.getBoardingAvailable())) {
				institute.setBoardingAvailable(instituteDto.getBoardingAvailable());
			}
			institute.setInstituteFacilities(getInstituteFacility(institute, instituteDto));
			institute.setInstituteIntakes(getInstituteIntake(institute, instituteDto));
			//TODO removing as we have removed this table
		//	institute.setInstituteTiming(getInstituteTiming(institute, instituteDto));
			institute.setInstituteServices(getInstituteService(institute, instituteDto));
		} finally {
			services.clear();	
		}
		return institute;
	}
	
	private List<InstituteService> getInstituteService(final Institute institute, final InstituteCsvDto instituteDto) {
		List<InstituteService> instituteServiceList = new ArrayList<>();
		//Import Services
		if (!ObjectUtils.isEmpty(instituteDto.getServices())) {
			log.debug("Institute Services");
			String[] instituteService = instituteDto.getServices().split(",");
			if(!ObjectUtils.isEmpty(instituteService)) {
				List<String> instituteServiceStrList =  Arrays.asList(instituteService);
				instituteServiceStrList.stream().forEach(instituteServiceName ->{
					Service service = services.get(StringUtils.trim(instituteServiceName).toLowerCase());
					if(!ObjectUtils.isEmpty(service)) {
						instituteServiceList.add(new InstituteService(institute, service, null, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "AUTO", "AUTO"));						
					}
				});
			}
		}
		return instituteServiceList;
	}
	
	@Transactional(rollbackFor = {ConstraintVoilationException.class,Exception.class})
	private void setReadableIdForInsitute(Institute institute) {
		log.info("going to generate code for institute");
		boolean reGenerateCode = false;
		do {
			reGenerateCode = false;
			String onlyName = Utils.convertToLowerCaseAndRemoveSpace(institute.getName());
			String readableId = Utils.generateReadableId(onlyName);
			List<Institute> sameCodeInsts = instituteDao.findByReadableIdIn(Arrays.asList(onlyName, readableId));
			if (ObjectUtils.isEmpty(sameCodeInsts)) {
				institute.setReadableId(onlyName);
			} else if (sameCodeInsts.size() == 1) {
				institute.setReadableId(readableId);
			} else {
				reGenerateCode = true;
			}
		} while (reGenerateCode);
	}
	
	private List<InstituteFacility> getInstituteFacility(final Institute institute, final InstituteCsvDto instituteDto) {
		List<InstituteFacility> instituteFacilityList = new ArrayList<>();
		//Import Facilities
		if (!ObjectUtils.isEmpty(instituteDto.getFacilities())) {
			log.debug("Institute Facilites");
			String[] facilites = instituteDto.getFacilities().split(",");
			if(!ObjectUtils.isEmpty(facilites)) {
				List<String> facilityList =  Arrays.asList(facilites);
				facilityList.stream().forEach(facility ->{
					if(!StringUtils.isEmpty(facility) && !facility.equals("0")) {
						Service service = services.get(StringUtils.trim(facility).toLowerCase());
						if(!ObjectUtils.isEmpty(service)) {
							instituteFacilityList.add(new InstituteFacility(institute, service, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "AUTO", "AUTO"));						
						}						
					}
				});				
			}
		}
		//Import Sports Facilities
		if (!ObjectUtils.isEmpty(instituteDto.getFacilities())) {
			log.debug("Institute Sport Facilites");
			String[] facilites = instituteDto.getSportFacilities().split(",");
			if(!ObjectUtils.isEmpty(facilites)) {
				List<String> facilityList =  Arrays.asList(facilites);
				facilityList.stream().forEach(facility ->{
					if(!StringUtils.isEmpty(facility) && !facility.equals("0")) {
						Service service = services.get(StringUtils.trim(facility).toLowerCase());
						if(!ObjectUtils.isEmpty(service)) {
							instituteFacilityList.add(new InstituteFacility(institute, service, DateUtil.getUTCdatetimeAsDate(), DateUtil.getUTCdatetimeAsDate(), "AUTO", "AUTO"));						
						}
					}
				});
			}
		}
		return instituteFacilityList;
	}
	
//	private List<InstituteService> getInstituteServices(final Institute institute,final InstituteDto instituteDto) {
//		List<InstituteService> services = new ArrayList<>();
//		if (instituteDto.getPersonalCount() == 1) {
//			services.add(getInstituteService(institute, "Personal Acoun Academy"));
//		}
//	    if (instituteDto.getVisaWorkBenefits() == 1) {
//	    	services.add(getInstituteService(institute, "Visa Work Benefits"));
//		}
//	    if (instituteDto.getEmpCareerDev() == 1) {
//	    	services.add(getInstituteService(institute, "Employment and career development"));
//		}
//	    if (instituteDto.getStudyLibrarySupport() == 1) {
//	    	services.add(getInstituteService(institute, "Study Library Support"));
//		}
//	    if (instituteDto.getHealthServices() == 1) {
//	    	services.add(getInstituteService(institute, "Health services"));
//		}
//	    if (instituteDto.getSwimmingPool() == 1) {
//	    	services.add(getInstituteService(institute, "Swimming pool"));
//		}
//	    if (instituteDto.getDisabilitySupport() == 1) {
//	    	services.add(getInstituteService(institute, "Disability Support"));
//		}
//	    if (instituteDto.getChildcareCentre() == 1) {
//	    	services.add(getInstituteService(institute, "Childcare Centre"));
//		}
//	    if (instituteDto.getCultInclAntiracismPrg() == 1) {
//	    	services.add(getInstituteService(institute, "Cultural inclusion/anti-racism programs"));
//		}
//	    if (instituteDto.getTechServ() == 1) {
//	    	services.add(getInstituteService(institute, "Technology Services"));
//		}
//	    if (instituteDto.getAccommodation() == 1) {
//	    	services.add(getInstituteService(institute, "Accommodation"));
//		}
//	    if (instituteDto.getMedical() == 1) {
//	    	services.add(getInstituteService(institute, "Medical"));
//		}
//	    if (instituteDto.getLegService() == 1) {
//	    	services.add(getInstituteService(institute, "Legal Services"));
//		}
//	    if (instituteDto.getAcctServ() == 1) {
//	    	services.add(getInstituteService(institute, "Accounting Services"));
//		}
//	    if (instituteDto.getBus() == 1) {
//	    	services.add(getInstituteService(institute, "Bus"));
//		}
//	    if (instituteDto.getTrain() == 1) {
//	    	services.add(getInstituteService(institute, "Train"));
//		}
//	    if (instituteDto.getAirportPickup() == 1) {
//	    	services.add(getInstituteService(institute, "Airport Pickup"));
//		}
//	    if (instituteDto.getSportCenter() == 1) {
//	    	services.add(getInstituteService(institute, "Sports Center"));
//		}
//	    if (instituteDto.getSprotTeam() == 1) {
//	    	services.add(getInstituteService(institute, "Sport Teams"));
//		}
//	    if (instituteDto.getHousingServices() == 1) {
//	    	services.add(getInstituteService(institute, "Housing Services"));
//		}
//	    if (instituteDto.getAdminServiceFee() == 1) {
//	    	services.add(getInstituteService(institute, "Admin Service Fee"));
//		}
//	    if (instituteDto.getPaymentPlan() == 1) {
//	    	services.add(getInstituteService(institute, "Payment Plan"));
//		}
//	    if (instituteDto.getGovernmentLoan() == 1) {
//	    	services.add(getInstituteService(institute, "Government Loan"));
//		}
//	    if (instituteDto.getScholarship() == 1) {
//	    	services.add(getInstituteService(institute, "Scholarship"));
//		}
//	    return services;
//	}
	
//	private InstituteService getInstituteService(Institute institute, String serviceName) {
//		InstituteService instituteServiceDetails = new InstituteService();
//		instituteServiceDetails.setInstitute(institute);
//		instituteServiceDetails.setServiceName(serviceName);
//		instituteServiceDetails.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
//		instituteServiceDetails.setIsActive(true);
//		instituteServiceDetails.setCreatedBy("AUTO");
//		instituteServiceDetails.setUpdatedBy("AUTO");
//		instituteServiceDetails.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
//		return instituteServiceDetails;
//	}

	
	private Timing getInstituteTiming(Institute institute, InstituteCsvDto instituteDto) {
		Timing instituteTiming = new Timing();
		instituteTiming.setEntityType(EntityTypeEnum.INSTITUTE);
		instituteTiming.setMonday((instituteDto.getMonday() == null || 
				StringUtils.isEmpty(instituteDto.getMonday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getMonday(), "NULL")) ? "08:00 AM - 06:00 PM" : instituteDto.getMonday());
		
		instituteTiming.setTuesday((instituteDto.getTuesday() == null) || 
				StringUtils.isEmpty(instituteDto.getTuesday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getTuesday(), "NULL") ? "08:00 AM - 06:00 PM" : instituteDto.getTuesday());
		
		instituteTiming.setThursday((instituteDto.getWednesday() == null) || 
				StringUtils.isEmpty(instituteDto.getWednesday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getWednesday(), "NULL") ? "08:00 AM - 06:00 PM" : instituteDto.getWednesday());
		
		instituteTiming.setFriday((instituteDto.getThursday() == null) || 
				StringUtils.isEmpty(instituteDto.getThursday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getThursday(), "NULL") ? "08:00 AM - 06:00 PM" : instituteDto.getThursday());
		
		instituteTiming.setWednesday((instituteDto.getFriday() == null) || 
				StringUtils.isEmpty(instituteDto.getFriday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getFriday(), "NULL") ? "08:00 AM - 06:00 PM" : instituteDto.getFriday());
		
		instituteTiming.setSaturday((instituteDto.getSaturday() == null) || 
				StringUtils.isEmpty(instituteDto.getSaturday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getSaturday(), "NULL") ? "CLOSED" : instituteDto.getSaturday());
		
		instituteTiming.setSunday((instituteDto.getSunday() == null) || 
				StringUtils.isEmpty(instituteDto.getSunday()) || 
				StringUtils.equalsIgnoreCase(instituteDto.getSunday(), "NULL") ? "CLOSED" : instituteDto.getSunday());
		instituteTiming.setCreatedBy("API");
		instituteTiming.setCreatedOn(new Date());
		return instituteTiming;
	}
	
	private List<InstituteIntake> getInstituteIntake(final Institute institute, final InstituteCsvDto dto) {
		Set<String> intakes = new HashSet<>();
		List<InstituteIntake> intakeList = new ArrayList<>();
		intakes.add(dto.getIntake_1());
		intakes.add(dto.getIntake_2());
		intakes.add(dto.getIntake_3());
		intakes.add(dto.getIntake_4());
		intakes.add(dto.getIntake_5());
		intakes.add(dto.getIntake_6());
		intakes.add(dto.getIntake_7());
		intakes.add(dto.getIntake_8());
		intakes.add(dto.getIntake_9());
		intakes.add(dto.getIntake_10());
		intakes.add(dto.getIntake_11());
		intakes.add(dto.getIntake_12());
		intakes.stream().forEach(intake -> {
			if(!StringUtils.equals(intake, "0") && !StringUtils.equals(intake, null)) {
				InstituteIntake instituteIntake = new InstituteIntake();
				instituteIntake.setIntake(intake);
				instituteIntake.setInstitute(institute);
				instituteIntake.setCreatedBy("API");
				instituteIntake.setCreatedOn(new Date());
				intakeList.add(instituteIntake);
			}
		});
		return intakeList;
	}

	private InstituteAdditionalInfo getInstituteAdditionalInfo(Institute institute, InstituteCsvDto dto) {
		InstituteAdditionalInfo instituteAdditionalInfo = new InstituteAdditionalInfo();
		instituteAdditionalInfo.setCreatedBy("API");
		instituteAdditionalInfo.setCreatedOn(new Date());
		instituteAdditionalInfo.setUpdatedBy("API");
		instituteAdditionalInfo.setUpdatedOn(new Date());
		instituteAdditionalInfo.setNumberOfStudent(dto.getTotalStudent());
		instituteAdditionalInfo.setInstitute(institute);
		return instituteAdditionalInfo;
	}

}
