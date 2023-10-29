package com.azguards.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteAdditionalInfo;
import com.azguards.app.bean.InstituteFacility;
import com.azguards.app.bean.Todo;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.DayTimingDto;
import com.azguards.app.dto.FacilityDto;
import com.azguards.app.dto.InstituteFacilityDto;
import com.azguards.app.dto.InstituteRequestDto;
import com.azguards.app.dto.LatLongDto;
import com.azguards.app.dto.TodoDto;
import com.azguards.app.dto.ValidList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.yuzee.common.lib.dto.institute.CourseDeliveryModeFundingDto;
import com.yuzee.common.lib.dto.institute.CourseDeliveryModesDto;
import com.yuzee.common.lib.dto.institute.CourseFeesDto;
import com.yuzee.common.lib.dto.institute.TimingDto;
import com.yuzee.common.lib.enumeration.InstituteType;
import com.yuzee.common.lib.exception.ForbiddenException;
import com.yuzee.common.lib.util.DateUtil;
import com.yuzee.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

	private static MessageTranslator staticMessageTranslator;

	@Autowired
	private MessageTranslator messageTranslator;

	@PostConstruct
	private void init() {
		staticMessageTranslator = messageTranslator;
	}
	
	public static InstituteRequestDto convertInstituteBeanToInstituteRequestDto(final Institute institute) {
		InstituteRequestDto instituteRequestDto = new InstituteRequestDto();
		instituteRequestDto.setAvgCostOfLiving(institute.getAvgCostOfLiving());
		instituteRequestDto.setCityName(institute.getCityName());
		instituteRequestDto.setStateName(institute.getState());
		instituteRequestDto.setCountryName(institute.getCountryName());
		instituteRequestDto.setDescription(institute.getDescription());
		instituteRequestDto.setId(institute.getId());
		instituteRequestDto.setWebsite(institute.getWebsite());
		instituteRequestDto.setShowSuggestion(institute.getShowSuggestion());
		if (institute.getInstituteType() != null) {
			instituteRequestDto.setInstituteType(institute.getInstituteType());
		}
		if (institute.getLatitude() != null) {
			instituteRequestDto.setLatitude(institute.getLatitude());
		}
		if (institute.getLongitude() != null) {
			instituteRequestDto.setLongitude(institute.getLongitude());
		}
		instituteRequestDto.setAddress(institute.getAddress());
		instituteRequestDto.setPhoneNumber(institute.getPhoneNumber());
		InstituteAdditionalInfo instituteAdditionalInfo = institute.getInstituteAdditionalInfo();
		if (!ObjectUtils.isEmpty(instituteAdditionalInfo)) {
			instituteRequestDto.setTotalStudent(instituteAdditionalInfo.getNumberOfStudent());
		}
		instituteRequestDto.setWorldRanking(institute.getWorldRanking());
		instituteRequestDto.setName(institute.getName());
		instituteRequestDto.setEmail(institute.getEmail());
		instituteRequestDto.setEnrolmentLink(institute.getEnrolmentLink());
		instituteRequestDto.setScholarshipFinancingAssistance(institute.getScholarshipFinancingAssistance());
		instituteRequestDto.setTuitionFessPaymentPlan(institute.getTuitionFeesPaymentPlan());
		instituteRequestDto.setWhatsNo(institute.getWhatsNo());
		instituteRequestDto.setAboutInfo(institute.getAboutInfo());
		instituteRequestDto.setCourseStart(institute.getCourseStart());
		instituteRequestDto.setDomesticRanking(institute.getDomesticRanking());
		instituteRequestDto.setTagLine(institute.getTagLine());
		instituteRequestDto.setPostalCode(institute.getPostalCode());
		instituteRequestDto.setReadableId(institute.getReadableId());
//		if (StringUtils.hasText(institute.getInstituteType()) && EnumUtils.isValidEnum(InstituteType.class, institute.getInstituteType())) {
//			instituteRequestDto.setInstituteTypeObj(InstituteType.valueOf(institute.getInstituteType()));
//			
//		}
		institute.getInstituteType();
//for(String type:institute.getInstituteType()) {
//	if(EnumUtils.isValidEnum(InstituteType.class, type)) {
//		instituteRequestDto.getInstituteType().add(type);
//	}
//}
		institute.getInstituteType().stream().forEach(type ->{
			if(EnumUtils.isValidEnum(InstituteType.class, type)) {
			instituteRequestDto.getInstituteType().add(type);
		}
		});

		
		return instituteRequestDto;
	}

	public static void copyCourseToCourseRequest(final Course course, CourseRequest courseRequest) {
		courseRequest.setId(course.getId());
		
		if (course.getWorldRanking() != null) {
			courseRequest.setWorldRanking(String.valueOf(course.getWorldRanking()));
		}
		if (course.getDescription() != null) {
			courseRequest.setDescription(course.getDescription());
		} else {
			courseRequest.setDescription(IConstant.COURSE_DEFAULT_DESCRPTION);
		}
		courseRequest.setName(course.getName());
		if (course.getFaculty() != null) {
			courseRequest.setFacultyId(course.getFaculty().getId());
		}
		courseRequest.setCurrency(course.getCurrency());
		courseRequest.setWebsite(course.getWebsite());
		courseRequest.setAvailability(course.getAvailabilty());
		if (course.getInstitute() != null) {
			courseRequest.setInstituteId(course.getInstitute().getId());
			courseRequest.setInstituteName(course.getInstitute().getName());
			courseRequest.setLatitude(course.getInstitute().getLatitude());
			courseRequest.setLongitude(course.getInstitute().getLongitude());
			courseRequest.setInstituteName(course.getInstitute().getName());
			courseRequest.getInstitute().setStateName(course.getInstitute().getState());
			courseRequest.getInstitute()
						.setIntakes(course.getInstitute().getInstituteIntakes().stream().map(e -> e.getIntake()).toList());
		}
		if (course.getLevel() != null) {
			courseRequest.setLevelId(course.getLevel().getId());
		}
		if (course.getCourseIntake() != null) {
			courseRequest.getIntake().setDates(course.getCourseIntake().getDates());
		}
		
		
		courseRequest.setRemarks(course.getRemarks());
		courseRequest.setWebsite(course.getWebsite());
		courseRequest.setPhoneNumber(course.getPhoneNumber());
		courseRequest.setAvailability(course.getAvailabilty());
		courseRequest.setRecognition(course.getRecognition());
		courseRequest.setAbbreviation(course.getAbbreviation());
		courseRequest.setCurrencyTime(course.getCurrencyTime());
		courseRequest.setCurrency(course.getCurrency());
		courseRequest.setRecognitionType(course.getRecognitionType());
		courseRequest.setExaminationBoard(course.getExaminationBoard());
		
		courseRequest.setDomesticApplicationFee(course.getDomesticApplicationFee());
		courseRequest.setInternationalApplicationFee(course.getInternationalApplicationFee());
		courseRequest.setDomesticEnrollmentFee(course.getDomesticEnrollmentFee());
		courseRequest.setInternationalEnrollmentFee(course.getInternationalEnrollmentFee());
		
		courseRequest.setRecDate(course.getRecDate());
		courseRequest.setContent(course.getContent());
		courseRequest.setGlobalGpa(course.getGlobalGpa());
		courseRequest.setEmail(course.getEmail());
		courseRequest.setEntranceExam(course.getEntranceExam());
	}

	public static Map<String, Double> getCurrencyDetails(final String baseCurrency) {
		String currencyResponse = null;
		URL url = null;
		try {
			url = new URL(IConstant.CURRENCY_URL + "latest?access_key=" + IConstant.API_KEY + "&base=" + baseCurrency);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			JsonObject jsonobj = root.getAsJsonObject();
			currencyResponse = jsonobj.toString();
			JsonObject rateObject = jsonobj.get("rates").getAsJsonObject();
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Double> ratesMap = mapper.readValue(rateObject.getAsString(), HashMap.class);
			return /* currencyResponse */ratesMap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return new HashMap<>();

	}

	public static Todo convertTodoDtoIntoTodo(final TodoDto todoDto) {
		Todo todo = new Todo();
		todo.setDescription(todoDto.getDescription());
		todo.setTitle(todoDto.getTitle());
		todo.setUserId(todoDto.getUserId());
		todo.setStatus(todoDto.getStatus());
		todo.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		todo.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		todo.setFolderId(todoDto.getFolderId());
		todo.setCreatedBy(todoDto.getCreatedBy());
		todo.setUpdatedBy(todoDto.getUpdatedBy());
		todo.setIsActive(true);
		if (todoDto.getDueDate() != null) {
			todo.setDueDate(DateUtil.convertStringDateToDate(todoDto.getDueDate()));
		}
		return todo;
	}

	public static TodoDto convertTodoIntoTodoDto(final Todo todo) {
		TodoDto todoDto = new TodoDto();
		todoDto.setId(todo.getId());
		todoDto.setDescription(todo.getDescription());
		todoDto.setTitle(todo.getTitle());
		todoDto.setUserId(todo.getUserId());
		todoDto.setStatus(todo.getStatus());
		todoDto.setFolderId(todo.getFolderId());
		todoDto.setCreatedBy(todo.getCreatedBy());
		todoDto.setUpdatedBy(todo.getUpdatedBy());
		if (todo.getDueDate() != null) {
			todoDto.setDueDate(DateUtil.convertDateToString(todo.getDueDate()));
		}
		return todoDto;
	}

	public static final Map<String, String> currencyNameMap = new HashMap<>();

	public static void setCurrencyNames() {
		currencyNameMap.put("FJD", "Fijian Dollar");
		currencyNameMap.put("MXN", "Mexican Peso");
		currencyNameMap.put("STD", "São Tomé and Príncipe Dobra");
		currencyNameMap.put("LVL", "Latvian Lats");
		currencyNameMap.put("SCR", "Seychellois Rupee");
		currencyNameMap.put("CDF", "Congolese Franc");
		currencyNameMap.put("BBD", "Barbadian Dollar");
		currencyNameMap.put("GTQ", "Guatemalan Quetzal");
		currencyNameMap.put("CLP", "Chilean Peso");
		currencyNameMap.put("HNL", "Honduran Lempira");
		currencyNameMap.put("UGX", "Ugandan Shilling");
		currencyNameMap.put("ZAR", "South African Rand");
		currencyNameMap.put("TND", "Tunisian Dinar");
		currencyNameMap.put("CUC", "Cuban Convertible Peso");
		currencyNameMap.put("BSD", "Bahamian Dollar");
		currencyNameMap.put("SLL", "Sierra Leonean Leone");
		currencyNameMap.put("SDG", "Sudanese Pound");
		currencyNameMap.put("IQD", "Iraqi Dinar");
		currencyNameMap.put("CUP", "Cuban Peso");
		currencyNameMap.put("GMD", "Gambian Dalasi");
		currencyNameMap.put("TWD", "New Taiwan Dollar");
		currencyNameMap.put("RSD", "Serbian Dinar");
		currencyNameMap.put("DOP", "Dominican Peso");
		currencyNameMap.put("KMF", "Comorian Franc");
		currencyNameMap.put("MYR", "Malaysian Ringgit");
		currencyNameMap.put("FKP", "Falkland Islands Pound");
		currencyNameMap.put("XOF", "CFA Franc BCEAO");
		currencyNameMap.put("GEL", "Georgian Lari");
		currencyNameMap.put("BTC", "Bitcoin");
		currencyNameMap.put("UYU", "Uruguayan Peso");
		currencyNameMap.put("MAD", "Moroccan Dirham");
		currencyNameMap.put("CVE", "Cape Verdean Escudo");
		currencyNameMap.put("TOP", "Tongan Paʻanga");
		currencyNameMap.put("AZN", "Azerbaijani Manat");
		currencyNameMap.put("OMR", "Omani Rial");
		currencyNameMap.put("PGK", "Papua New Guinean Kina");
		currencyNameMap.put("KES", "Kenyan Shilling");
		currencyNameMap.put("SEK", "Swedish Krona");
		currencyNameMap.put("BTN", "Bhutanese Ngultrum");
		currencyNameMap.put("UAH", "Ukrainian Hryvnia");
		currencyNameMap.put("GNF", "Guinean Franc");
		currencyNameMap.put("ERN", "Eritrean Nakfa");
		currencyNameMap.put("MZN", "Mozambican Metical");
		currencyNameMap.put("SVC", "Salvadoran Colón");
		currencyNameMap.put("ARS", "Argentine Peso");
		currencyNameMap.put("QAR", "Qatari Rial");
		currencyNameMap.put("IRR", "Iranian Rial");
		currencyNameMap.put("MRO", "Mauritanian Ouguiya");
		currencyNameMap.put("CNY", "Chinese Yuan");
		currencyNameMap.put("THB", "Thai Baht");
		currencyNameMap.put("UZS", "Uzbekistan Som");
		currencyNameMap.put("XPF", "CFP Franc");
		currencyNameMap.put("BDT", "Bangladeshi Taka");
		currencyNameMap.put("LYD", "Libyan Dinar");
		currencyNameMap.put("BMD", "Bermudan Dollar");
		currencyNameMap.put("KWD", "Kuwaiti Dinar");
		currencyNameMap.put("PHP", "Philippine Peso");
		currencyNameMap.put("RUB", "Russian Ruble");
		currencyNameMap.put("PYG", "Paraguayan Guarani");
		currencyNameMap.put("ISK", "Icelandic Króna");
		currencyNameMap.put("JMD", "Jamaican Dollar");
		currencyNameMap.put("COP", "Colombian Peso");
		currencyNameMap.put("MKD", "Macedonian Denar");
		currencyNameMap.put("USD", "United States Dollar");
		currencyNameMap.put("DZD", "Algerian Dinar");
		currencyNameMap.put("PAB", "Panamanian Balboa");
		currencyNameMap.put("GGP", "Guernsey Pound");
		currencyNameMap.put("SGD", "Singapore Dollar");
		currencyNameMap.put("ETB", "Ethiopian Birr");
		currencyNameMap.put("JEP", "Jersey Pound");
		currencyNameMap.put("KGS", "Kyrgystani Som");
		currencyNameMap.put("SOS", "Somali Shilling");
		currencyNameMap.put("VEF", "Venezuelan Bolívar Fuerte");
		currencyNameMap.put("VUV", "Vanuatu Vatu");
		currencyNameMap.put("LAK", "Laotian Kip");
		currencyNameMap.put("BND", "Brunei Dollar");
		currencyNameMap.put("ZMK", "Zambian Kwacha (pre-2013)");
		currencyNameMap.put("XAF", "CFA Franc BEAC");
		currencyNameMap.put("LRD", "Liberian Dollar");
		currencyNameMap.put("XAG", "Silver (troy ounce)");
		currencyNameMap.put("CHF", "Swiss Franc");
		currencyNameMap.put("HRK", "Croatian Kuna");
		currencyNameMap.put("ALL", "Albanian Lek");
		currencyNameMap.put("DJF", "Djiboutian Franc");
		currencyNameMap.put("ZMW", "Zambian Kwacha");
		currencyNameMap.put("TZS", "Tanzanian Shilling");
		currencyNameMap.put("VND", "Vietnamese Dong");
		currencyNameMap.put("XAU", "Gold (troy ounce)");
		currencyNameMap.put("AUD", "Australian Dollar");
		currencyNameMap.put("ILS", "Israeli New Sheqel");
		currencyNameMap.put("GHS", "Ghanaian Cedi");
		currencyNameMap.put("GYD", "Guyanaese Dollar");
		currencyNameMap.put("KPW", "North Korean Won");
		currencyNameMap.put("BOB", "Bolivian Boliviano");
		currencyNameMap.put("KHR", "Cambodian Riel");
		currencyNameMap.put("MDL", "Moldovan Leu");
		currencyNameMap.put("IDR", "Indonesian Rupiah");
		currencyNameMap.put("KYD", "Cayman Islands Dollar");
		currencyNameMap.put("AMD", "Armenian Dram");
		currencyNameMap.put("BWP", "Botswanan Pula");
		currencyNameMap.put("SHP", "Saint Helena Pound");
		currencyNameMap.put("TRY", "Turkish Lira");
		currencyNameMap.put("LBP", "Lebanese Pound");
		currencyNameMap.put("TJS", "Tajikistani Somoni");
		currencyNameMap.put("JOD", "Jordanian Dinar");
		currencyNameMap.put("AED", "United Arab Emirates Dirham");
		currencyNameMap.put("HKD", "Hong Kong Dollar");
		currencyNameMap.put("RWF", "Rwandan Franc");
		currencyNameMap.put("EUR", "Euro");
		currencyNameMap.put("LSL", "Lesotho Loti");
		currencyNameMap.put("DKK", "Danish Krone");
		currencyNameMap.put("CAD", "Canadian Dollar");
		currencyNameMap.put("BGN", "Bulgarian Lev");
		currencyNameMap.put("MMK", "Myanma Kyat");
		currencyNameMap.put("MUR", "Mauritian Rupee");
		currencyNameMap.put("NOK", "Norwegian Krone");
		currencyNameMap.put("SYP", "Syrian Pound");
		currencyNameMap.put("IMP", "Manx pound");
		currencyNameMap.put("ZWL", "Zimbabwean Dollar");
		currencyNameMap.put("GIP", "Gibraltar Pound");
		currencyNameMap.put("RON", "Romanian Leu");
		currencyNameMap.put("LKR", "Sri Lankan Rupee");
		currencyNameMap.put("NGN", "Nigerian Naira");
		currencyNameMap.put("CRC", "Costa Rican Colón");
		currencyNameMap.put("CZK", "Czech Republic Koruna");
		currencyNameMap.put("PKR", "Pakistani Rupee");
		currencyNameMap.put("XCD", "East Caribbean Dollar");
		currencyNameMap.put("ANG", "Netherlands Antillean Guilder");
		currencyNameMap.put("HTG", "Haitian Gourde");
		currencyNameMap.put("BHD", "Bahraini Dinar");
		currencyNameMap.put("KZT", "Kazakhstani Tenge");
		currencyNameMap.put("SRD", "Surinamese Dollar");
		currencyNameMap.put("SZL", "Swazi Lilangeni");
		currencyNameMap.put("LTL", "Lithuanian Litas");
		currencyNameMap.put("SAR", "Saudi Riyal");
		currencyNameMap.put("TTD", "Trinidad and Tobago Dollar");
		currencyNameMap.put("YER", "Yemeni Rial");
		currencyNameMap.put("MVR", "Maldivian Rufiyaa");
		currencyNameMap.put("AFN", "Afghan Afghani");
		currencyNameMap.put("INR", "Indian Rupee");
		currencyNameMap.put("AWG", "Aruban Florin");
		currencyNameMap.put("KRW", "South Korean Won");
		currencyNameMap.put("NPR", "Nepalese Rupee");
		currencyNameMap.put("JPY", "Japanese Yen");
		currencyNameMap.put("MNT", "Mongolian Tugrik");
		currencyNameMap.put("AOA", "Angolan Kwanza");
		currencyNameMap.put("PLN", "Polish Zloty");
		currencyNameMap.put("GBP", "British Pound Sterling");
		currencyNameMap.put("SBD", "Solomon Islands Dollar");
		currencyNameMap.put("BYN", "New Belarusian Ruble");
		currencyNameMap.put("HUF", "Hungarian Forint");
		currencyNameMap.put("BYR", "Belarusian Ruble");
		currencyNameMap.put("BIF", "Burundian Franc");
		currencyNameMap.put("MWK", "Malawian Kwacha");
		currencyNameMap.put("MGA", "Malagasy Ariary");
		currencyNameMap.put("XDR", "Special Drawing Rights");
		currencyNameMap.put("BZD", "Belize Dollar");
		currencyNameMap.put("BAM", "Bosnia-Herzegovina Convertible Mark");
		currencyNameMap.put("EGP", "Egyptian Pound");
		currencyNameMap.put("MOP", "Macanese Pataca");
		currencyNameMap.put("NAD", "Namibian Dollar");
		currencyNameMap.put("NIO", "Nicaraguan Córdoba");
		currencyNameMap.put("PEN", "Peruvian Nuevo Sol");
		currencyNameMap.put("NZD", "New Zealand Dollar");
		currencyNameMap.put("WST", "Samoan Tala");
		currencyNameMap.put("TMT", "Turkmenistani Manat");
		currencyNameMap.put("CLF", "Chilean Unit of Account (UF)");
		currencyNameMap.put("BRL", "Brazilian Real");
	}

	public static LatLongDto getCenterByLatituteAndLongitude(List<LatLongDto> latLongDtos) {
		LatLongDto centerLatLong = new LatLongDto();
		double pi = Math.PI / 180;
		double xpi = 180 / Math.PI;
		double x = 0, y = 0, z = 0;
		for (LatLongDto latLong : latLongDtos) {
			double latitude = latLong.getLatitude() * pi;
			double longitude = latLong.getLongitude() * pi;
			double c1 = Math.cos(latitude);
			x = x + c1 * Math.cos(longitude);
			y = y + c1 * Math.sin(longitude);
			z = z + Math.sin(latitude);
		}
		int total = latLongDtos.size();
		x = x / total;
		y = y / total;
		z = z / total;

		double centralLongitude = Math.atan2(y, x);
		double centralSquareRoot = Math.sqrt(x * x + y * y);
		double centralLatitude = Math.atan2(z, centralSquareRoot);
		centerLatLong.setLatitude(centralLatitude * xpi);
		centerLatLong.setLongitude(centralLongitude * xpi);
		return centerLatLong;
	}
	
	public static double getDistanceFromLatLonInKm(Double userlatitude, Double userLongitude, Double courseLatitude,
			Double coursLongitude) {
		double latitude = (courseLatitude - userlatitude) * (Math.PI / 180);
		double longitude = (coursLongitude - userLongitude) * (Math.PI / 180);
		double angleValue = Math.sin(latitude / 2) * Math.sin(latitude / 2) + Math.cos((userlatitude) * (Math.PI / 180))
				* Math.cos((courseLatitude) * (Math.PI / 180)) * Math.sin(longitude / 2) * Math.sin(longitude / 2);
		double cordinate = 2 * Math.atan2(Math.sqrt(angleValue), Math.sqrt(1 - angleValue));
		double distance = 6371 * cordinate; // Distance in km
		return distance;
	}
	
	public static List<DayTimingDto> convertTimingResponseDtoToDayTimingDto(TimingDto instituteTimingResponseDto) {
		List<DayTimingDto> dayTimingDtos = new ArrayList<>();
		DayTimingDto instituteTimingDto = null;
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getMonday()) && !instituteTimingResponseDto.getMonday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Monday");
			String time[] = instituteTimingResponseDto.getMonday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getTuesday()) && !instituteTimingResponseDto.getTuesday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Tuesday");
			String time[] = instituteTimingResponseDto.getTuesday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getWednesday()) && !instituteTimingResponseDto.getWednesday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Wednesday");
			String time[] = instituteTimingResponseDto.getWednesday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getThursday()) && !instituteTimingResponseDto.getThursday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Thursday");
			String time[] = instituteTimingResponseDto.getThursday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getFriday()) && !instituteTimingResponseDto.getFriday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Friday");
			String time[] = instituteTimingResponseDto.getFriday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getSaturday()) && !instituteTimingResponseDto.getSaturday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Saturday");
			String time[] = instituteTimingResponseDto.getSaturday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		if(!StringUtils.isEmpty(instituteTimingResponseDto.getSunday()) && !instituteTimingResponseDto.getSunday().equalsIgnoreCase("CLOSED")) {
			instituteTimingDto = new DayTimingDto();
			instituteTimingDto.setDay("Sunday");
			String time[] = instituteTimingResponseDto.getSunday().split("-");
			instituteTimingDto.setOpeningFrom(time[0]);
			instituteTimingDto.setOpeningTo(time[1]);
			dayTimingDtos.add(instituteTimingDto);
		}
		return dayTimingDtos;
	}
	
	
	
	public static InstituteFacilityDto createInstituteFacilityResponseDto(
			List<InstituteFacility> listOfInstituteFacility) {
		InstituteFacilityDto instituteFacilityDto = new InstituteFacilityDto();
		listOfInstituteFacility.stream().forEach(instituteFacility -> {
			FacilityDto facilityDto = new FacilityDto(instituteFacility.getId(),
					instituteFacility.getService().getName(), instituteFacility.getService().getId());
			instituteFacilityDto.getFacilities().add(facilityDto);
		});
		return instituteFacilityDto;
	}
	
	public static void validateEditAccess(String userId, Course course) throws ForbiddenException {
		log.info("Inside CommonUtil.validateEditAccess");
		if (!course.getCreatedBy().equals(userId)) {
			log.info(staticMessageTranslator.toLocale("common.no_acces.edit",Locale.US));
			throw new ForbiddenException(staticMessageTranslator.toLocale("common.no_acces.edit"));
		}
	}
}
