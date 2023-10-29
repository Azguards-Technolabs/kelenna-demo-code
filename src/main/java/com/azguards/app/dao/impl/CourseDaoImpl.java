package com.azguards.app.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseIntake;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.Faculty;
import com.azguards.app.bean.Institute;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseDto;
import com.azguards.app.dto.CourseRequest;
import com.azguards.app.dto.CourseResponseDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.CourseSearchFilterDto;
import com.azguards.app.dto.GlobalFilterSearchDto;
import com.azguards.app.dto.UserDto;
import com.azguards.app.enumeration.CourseSortBy;
import com.azguards.app.repository.CourseRepository;
import com.azguards.app.repository.document.CourseDocumentRepository;
import com.azguards.app.specification.CourseSpecification;
import com.azguards.common.lib.dto.common.CurrencyRateDto;
import com.azguards.common.lib.dto.institute.CourseDeliveryModesDto;
import com.azguards.common.lib.dto.institute.CourseSyncDTO;
import com.azguards.common.lib.dto.institute.FacultyDto;
import com.azguards.common.lib.dto.institute.InstituteSyncDTO;
import com.azguards.common.lib.dto.institute.LevelDto;
import com.azguards.common.lib.enumeration.SortingOnEnum;
import com.azguards.common.lib.enumeration.SortingTypeEnum;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
@Slf4j
public class CourseDaoImpl implements CourseDao {

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private CourseDocumentRepository courseDocumentRepository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Value("${s3.url}")
	private String s3URL;
	
	Function<String,String> addQuotes =  s -> "\'" + s + "\'";

	@Override
	public Course addUpdateCourse(final Course course) throws ValidationException {
		try {
			return courseRepository.save(course);
		}
		catch(DataIntegrityViolationException ex) {
			log.error(ex.getMessage());
			throw new ValidationException(ex.getMessage());
		}
	}

	@Override
	public List<Course> saveAll(final List<Course> courses) throws ValidationException {
			return courseRepository.saveAll(courses);
	}

	@Override
	public Course get(final String courseId) {
		Optional<Course> optionalCourse = courseRepository.findById(courseId);
		if (optionalCourse.isPresent()) {
			return optionalCourse.get();	
		}
		return null;
	}

	@Override
	public int getCountforNormalCourse(final CourseSearchDto courseSearchDto, final String searchKeyword, List<String> entityIds) {
		Session session = sessionFactory.getCurrentSession();

		String sqlQuery = "select count(*) from course crs inner join institute inst "
				+ " on crs.institute_id = inst.id"
				+ " where 1=1 and crs.is_active=1";
		
		if(!CollectionUtils.isEmpty(entityIds)) {
			sqlQuery += " and crs.id NOT IN (" +entityIds.stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}
		
		if (null != courseSearchDto.getInstituteId()) {
			sqlQuery += " and inst.id ='" + courseSearchDto.getInstituteId() + "'";
		}

		if (null != courseSearchDto.getCountryNames() && !courseSearchDto.getCountryNames().isEmpty()) {
			sqlQuery += " and inst.country_name in ('" + courseSearchDto.getCountryNames().stream().map(String::valueOf).collect(Collectors.joining(",")) + "')";
		}

		if (null != courseSearchDto.getCityNames() && !courseSearchDto.getCityNames().isEmpty()) {
			sqlQuery += " and inst.city_name in ('" + courseSearchDto.getCityNames().stream().map(String::valueOf).collect(Collectors.joining(",")) + "')";
		}

		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and crs.level_id in ('" + courseSearchDto.getLevelIds().stream().map(String::valueOf).collect(Collectors.joining(",")) + "')";
		}

		if (null != courseSearchDto.getFacultyIds() && !courseSearchDto.getFacultyIds().isEmpty()) {
			sqlQuery += " and crs.faculty_id in ('" + courseSearchDto.getFacultyIds().stream().map(String::valueOf).collect(Collectors.joining(",")) + "')";
		}

		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			sqlQuery += " and crs.name in (" + courseSearchDto.getCourseKeys().stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
		}
		if (null != courseSearchDto.getCourseName() && !courseSearchDto.getCourseName().isEmpty()) {
			sqlQuery += " and crs.name like '%" + courseSearchDto.getCourseName().trim() + "%'";
		}

		if (searchKeyword != null) {
			sqlQuery += " and ( inst.name like '%" + searchKeyword.trim() + "%'";
			sqlQuery += " or inst.country_name like '%" + searchKeyword.trim() + "%'";
			sqlQuery += " or crs.name like '%" + searchKeyword.trim() + "%' )";
		}
		Query query = session.createSQLQuery(sqlQuery);
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<CourseResponseDto> getAllCoursesByFilter(final CourseSearchDto courseSearchDto, final String searchKeyword, final List<String> courseIds,
			final Integer startIndex, final boolean uniqueCourseName, List<String> entityIds) {
		Session session = sessionFactory.getCurrentSession();

		String sqlQuery = "select distinct crs.id as courseId, crs.name as courseName, inst.id as instId, inst.name as instName,"
				+ " crs.currency, cai.duration, cai.duration_time, crs.world_ranking, crs.stars, crs.recognition,"
				+ " cai.domestic_fee, cai.international_fee, crs.remarks, cai.usd_domestic_fee, cai.usd_international_fee,"
				+ " crs.updated_on, crs.is_active ,inst.latitude as latitude,inst.longitude as longitute,inst.country_name as countryName,"
				+ " inst.city_name as cityName, cai.delivery_type, cai.study_mode,crs.level_id as levelId, crs.faculty_id as facultyId, crs.readable_id as readableId from course crs inner join institute inst  on crs.institute_id = inst.id "
				+ " left join course_delivery_modes cai on cai.course_id = crs.id"
				+ " where 1=1 and crs.is_active=1";

		boolean showIntlCost = false;
		
		if(!CollectionUtils.isEmpty(entityIds)) {
			sqlQuery += " and crs.id NOT IN (" +entityIds.stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}
		
		if (null != courseSearchDto.getInstituteId()) {
			sqlQuery += " and inst.id ='" + courseSearchDto.getInstituteId() +"'";
		}

		if (null != courseSearchDto.getCountryNames() && !courseSearchDto.getCountryNames().isEmpty()) {
			sqlQuery += " and inst.country_name in (" + courseSearchDto.getCountryNames().stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}

		if (null != courseSearchDto.getCityNames() && !courseSearchDto.getCityNames().isEmpty()) {
			sqlQuery += " and inst.city_name in (" + courseSearchDto.getCityNames().stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}

		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and crs.level_id in (" + courseSearchDto.getLevelIds().stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}

		if (null != courseSearchDto.getFacultyIds() && !courseSearchDto.getFacultyIds().isEmpty()) {
			sqlQuery += " and crs.faculty_id in (" + courseSearchDto.getFacultyIds().stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}

		if (null != courseSearchDto.getCourseName() && !courseSearchDto.getCourseName().isEmpty()) {
			sqlQuery += " and crs.name like '%" + courseSearchDto.getCourseName().trim() + "%'";
		}

		if (courseIds != null) {
			sqlQuery += " and crs.id in (" + courseIds.stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}

		if (searchKeyword != null) {
			sqlQuery += " and ( inst.name like '%" + searchKeyword.trim() + "%'";
			sqlQuery += " or inst.country_name like '%" + searchKeyword.trim() + "%'";
			sqlQuery += " or crs.name like '%" + searchKeyword.trim() + "%' )";
		}

		if (uniqueCourseName) {
			sqlQuery += " group by crs.name ";
		}

		sqlQuery += " ";
		String sortingQuery = "";
		String sortTypeValue = "ASC";
		if (!courseSearchDto.getSortAsscending()) {
			sortTypeValue = "DESC";
		}
		if (courseSearchDto.getSortBy() != null && !courseSearchDto.getSortBy().isEmpty()) {
			if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.DURATION.toString())) {
				sortingQuery = sortingQuery + " ORDER BY cai.duration " + sortTypeValue + " ";
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.RECOGNITION.toString())) {
				sortingQuery = sortingQuery + " ORDER BY crs.recognition " + sortTypeValue + " ";
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.LOCATION.toString())) {
				sortingQuery = sortingQuery + " ORDER BY inst.country_name " + sortTypeValue + " ";
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.PRICE.toString())) {
				sortingQuery = sortingQuery + " ORDER BY IF(crs.currency='" + courseSearchDto.getCurrencyCode()
						+ "', cai.usd_domestic_fee, cai.usd_international_fee) " + sortTypeValue + " ";
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase("instituteName")) {
				sortingQuery = " order by inst.name " + sortTypeValue.toLowerCase();
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase("countryName")) {
				sortingQuery = " order by inst.country_name " + sortTypeValue.toLowerCase();
			} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.NAME.name())) {
				sortingQuery = " order by crs.name " + sortTypeValue.toLowerCase();
			}
		} else {
			sortingQuery = " order by cai.international_fee " + sortTypeValue.toLowerCase();

		}

		if (startIndex != null && courseSearchDto.getMaxSizePerPage() != null) {
			sqlQuery += sortingQuery + " LIMIT " + startIndex + " ," + courseSearchDto.getMaxSizePerPage();
		} else {
			sqlQuery += sortingQuery;
		}
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();

		List<CourseResponseDto> list = new ArrayList<>();
		List<CourseDeliveryModesDto> additionalInfoDtos = new ArrayList<>(); 
		CourseResponseDto courseResponseDto = null;
		Long localFees = 0l, intlFees = 0l;
		String newCurrencyCode = "";
		for (Object[] row : rows) {
			try {
				CourseDeliveryModesDto additionalInfoDto = new CourseDeliveryModesDto();
				Double localFeesD = null;
				Double intlFeesD = null;
				if (row[11] != null) {
					localFeesD = Double.valueOf(String.valueOf(row[10]));
				}
				if (row[12] != null) {
					intlFeesD = Double.valueOf(String.valueOf(row[11]));
				}
				newCurrencyCode = String.valueOf(row[4]);
				if (localFeesD != null) {
					localFees = Math.round(localFeesD);
				}
				if (intlFeesD != null) {
					intlFees = Math.round(intlFeesD);
				}
				courseResponseDto = new CourseResponseDto();
				if (showIntlCost) {
					courseResponseDto.setCost(intlFees + " " + newCurrencyCode);
				} else {
					courseResponseDto.setCost(localFees + " " + newCurrencyCode);
				}
				courseResponseDto.setLatitude((Double) row[17]);
				courseResponseDto.setLongitude((Double) row[18]);
				courseResponseDto.setCountryName(String.valueOf(row[19]));
				courseResponseDto.setCityName(String.valueOf(row[20]));
				courseResponseDto.setId(String.valueOf(row[0]));
				courseResponseDto.setName(String.valueOf(row[1]));
				courseResponseDto.setInstituteId(String.valueOf(row[2]));
				courseResponseDto.setInstituteName(String.valueOf(row[3]));
				additionalInfoDto.setDuration(Double.valueOf(String.valueOf(row[5])));
				additionalInfoDto.setDurationTime(String.valueOf(row[6]));
				courseResponseDto.setLocation(String.valueOf(row[20]) + ", " + String.valueOf(row[19]));

				Integer worldRanking = 0;
				if (null != row[7]) {
					worldRanking = Double.valueOf(String.valueOf(row[7])).intValue();
				}
				courseResponseDto.setCourseRanking(worldRanking);
				courseResponseDto.setStars(Double.valueOf(String.valueOf(row[8])));
				if (courseSearchDto.getCurrencyCode() != null && !courseSearchDto.getCurrencyCode().isEmpty()) {
					courseResponseDto.setCurrencyCode(courseSearchDto.getCurrencyCode());

				}
				if (row[5] != null) {
					courseResponseDto.setCurrencyCode(row[5].toString());
				}
				courseResponseDto.setUpdatedOn((Date) row[15]);
				if (String.valueOf(row[16]) != null && String.valueOf(row[16]).equals("1")) {
					courseResponseDto.setIsActive(true);
				} else {
					courseResponseDto.setIsActive(false);
				}
				additionalInfoDto.setDeliveryType(row[21].toString());
				additionalInfoDto.setStudyMode(row[22].toString());
				additionalInfoDto.setCourseId(String.valueOf(row[0]));

				additionalInfoDtos.add(additionalInfoDto);
				courseResponseDto.setCourseDeliveryModes(additionalInfoDtos);
				courseResponseDto.setLevelId(row[23].toString());
				courseResponseDto.setFacultyId(row[24].toString());
				courseResponseDto.setReadableId(String.valueOf(row[25]));
				list.add(courseResponseDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<CourseResponseDto> getAllCoursesByInstitute(final String instituteId, final CourseSearchDto courseSearchDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select A.*,count(1) over () totalRows from  (select distinct crs.id as courseId,crs.name as courseName,"
				+ " inst.id as instId,inst.name as instName,"
				+ " crs.cost_range, crs.currency, inst.city_name as cityName,"
				+ " inst.country_name as countryName,crs.world_ranking,crs.stars,crs.recognition"
				+ " from course crs  inner join institute inst "
				+ " on crs.institute_id = inst.id "
				+ " inner join faculty f  on f.id = crs.faculty_id "
				+ " inner join course_delivery_modes cai on cai.course_id = crs.id"
				+ " left join institute_service iis  on iis.institute_id = inst.id where crs.institute_id = '" + instituteId + "'";

		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and f.level_id in ('" + StringUtils.join(courseSearchDto.getLevelIds(), ',') + "')";
		}

		if (null != courseSearchDto.getFacultyIds() && !courseSearchDto.getFacultyIds().isEmpty()) {
			sqlQuery += " and crs.faculty_id in ('" + StringUtils.join(courseSearchDto.getFacultyIds(), ',') + "')";
		}

		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			String value = "";
			int i = 0;
			for (String key : courseSearchDto.getCourseKeys()) {
				if (null == key || key.isEmpty()) {
					continue;
				}
				if (i == 0) {
					value = "'" + key.trim() + "'";
				} else {
					value = value + "," + "'" + key.trim() + "'";
				}
				i++;
			}
			sqlQuery += " and crs.name in (" + value + ")";
		}

		if(!StringUtils.isEmpty(courseSearchDto.getUserCountryName())) {
			if(null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0 && 
					null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
				sqlQuery += "and ((inst.country_name = '" + courseSearchDto.getUserCountryName() +"' and cai.usd_domestic_fee >="+ courseSearchDto.getMinCost() +
						" and cai.usd_domestic_fee <= "+ courseSearchDto.getMaxCost() +")" +
						" OR (cai.usd_international_fee >= "+ courseSearchDto.getMinCost() +" and cai.usd_international_fee <= " + courseSearchDto.getMaxCost() +"))";
			} else {
				if(null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0) {
					sqlQuery += "and ((inst.country_name = '" + courseSearchDto.getUserCountryName() + "' and cai.usd_domestic_fee >= "
							 + courseSearchDto.getMinCost() +") OR (inst.country_name != '" + courseSearchDto.getUserCountryName() + "'"
							 + " and cai.usd_international_fee >= "+ courseSearchDto.getMinCost() +"))";
				} else if (null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
					sqlQuery += "and ((inst.country_name = '" + courseSearchDto.getUserCountryName() +"' and cai.usd_domestic_fee <= "
							 + courseSearchDto.getMaxCost() +") OR (inst.country_name != '" + courseSearchDto.getUserCountryName() + "'"
							 + " and cai.usd_international_fee <= "+ courseSearchDto.getMaxCost() +"))";
				}
			}
		}
		if (null != courseSearchDto.getMinDuration() && courseSearchDto.getMinDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) >= " + courseSearchDto.getMinDuration();
		}

		if (null != courseSearchDto.getMaxDuration() && courseSearchDto.getMaxDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) <= " + courseSearchDto.getMaxDuration();
		}

		if (null != courseSearchDto.getSearchKey() && !courseSearchDto.getSearchKey().isEmpty()) {
			sqlQuery += " and crs.name like '%" + courseSearchDto.getSearchKey().trim() + "%'";
		}
		sqlQuery += ") A ";

		String sortingQuery = "";
		if (null != courseSearchDto.getSortingObj()) {
			CourseSearchFilterDto sortingObj = courseSearchDto.getSortingObj();
			if (null != sortingObj.getPrice() && !sortingObj.getPrice().isEmpty()) {
				if (sortingObj.getPrice().equals("ASC")) {
					sortingQuery = " order by cai.usd_domestic_fee asc";
				} else {
					sortingQuery = " order by cai.usd_domestic_fee desc";
				}
			}

			if (null != sortingObj.getLocation() && !sortingObj.getLocation().isEmpty()) {
				if (sortingObj.getLocation().equals("ASC")) {
					sortingQuery = " order by A.countryName, A.cityName asc";
				} else {
					sortingQuery = " order by A.countryName, A.cityName desc";
				}
			}

			if (null != sortingObj.getDuration() && !sortingObj.getDuration().isEmpty()) {
				if (sortingObj.getDuration().equals("ASC")) {
					sortingQuery = " order by cai.duration asc";
				} else {
					sortingQuery = " order by cai.duration desc";
				}
			}

			if (null != sortingObj.getRecognition() && !sortingObj.getRecognition().isEmpty()) {
				if (sortingObj.getRecognition().equals("ASC")) {
					sortingQuery = " order by A.recognition asc";
				} else {
					sortingQuery = " order by A.recognition desc";
				}
			}
		} else {
			sortingQuery = " order by cai.usd_domestic_fee asc";
		}
		sqlQuery += sortingQuery + " OFFSET (" + courseSearchDto.getPageNumber() + "-1)*" + courseSearchDto.getMaxSizePerPage() + " ROWS FETCH NEXT "
				+ courseSearchDto.getMaxSizePerPage() + " ROWS ONLY";

		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<CourseResponseDto> list = new ArrayList<>();
		CourseResponseDto obj = null;
		for (Object[] row : rows) {
			obj = new CourseResponseDto();
			obj.setId(String.valueOf(row[0]));
			obj.setName(String.valueOf(row[1]));
			obj.setCost(String.valueOf(row[4]) + " " + String.valueOf(row[5]));
			Integer worldRanking = 0;
			if (null != row[8]) {
				worldRanking = Double.valueOf(String.valueOf(row[8])).intValue();
			}
			obj.setCourseRanking(Integer.valueOf(worldRanking.toString()));
			obj.setStars(Double.valueOf(String.valueOf(row[9])));
			obj.setTotalCount(Integer.parseInt(String.valueOf(row[11])));
			list.add(obj);
		}
		return list;
	}

	public CourseResponseDto getCourse(final String instituteId, final CourseSearchDto courseSearchDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select A.*,count(1) over () totalRows from  (select distinct crs.id as courseId,crs.name as courseName,"
				+ "inst.id as instId,inst.name as instName,"
				+ " crs.cost_range,crs.currency,crs.duration,crs.duration_time,ci.id as cityId,ctry.id as countryId,ci.name as cityName,"
				+ "ctry.name as countryName,crs.world_ranking,crs.language,crs.stars,crs.recognition,crs.domestic_fee,crs.international_fee "
				+ "from course crs inner join institute inst "
				+ " on crs.institute_id = inst.id inner join country ctry  on ctry.id = inst.country_id inner join "
				+ "city ci  on ci.id = inst.city_id inner join faculty f  on f.id = crs.faculty_id "
				+ "left join institute_service iis  on iis.institute_id = inst.id where crs.institute_id = " + instituteId;

		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and f.level_id in ('" + StringUtils.join(courseSearchDto.getLevelIds(), ',') + "')";
		}

		if (null != courseSearchDto.getFacultyIds() && !courseSearchDto.getFacultyIds().isEmpty()) {
			sqlQuery += " and crs.faculty_id in ('" + StringUtils.join(courseSearchDto.getFacultyIds(), ',') + "')";
		}

		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			String value = "";
			int i = 0;
			for (String key : courseSearchDto.getCourseKeys()) {
				if (null == key || key.isEmpty()) {
					continue;
				}
				if (i == 0) {
					value = "'" + key.trim() + "'";
				} else {
					value = value + "," + "'" + key.trim() + "'";
				}
				i++;
			}
			sqlQuery += " and crs.name in (" + value + ")";
		}

		if (null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0) {
			sqlQuery += " and crs.cost_range >= " + courseSearchDto.getMinCost();
		}

		if (null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
			sqlQuery += " and crs.cost_range <= " + courseSearchDto.getMaxCost();
		}

		if (null != courseSearchDto.getMinDuration() && courseSearchDto.getMinDuration() >= 0) {
			sqlQuery += " and cast(crs.duration as DECIMAL(9,2)) >= " + courseSearchDto.getMinDuration();
		}

		if (null != courseSearchDto.getMaxDuration() && courseSearchDto.getMaxDuration() >= 0) {
			sqlQuery += " and cast(crs.duration as DECIMAL(9,2)) <= " + courseSearchDto.getMaxDuration();
		}

		if (null != courseSearchDto.getSearchKey() && !courseSearchDto.getSearchKey().isEmpty()) {
			sqlQuery += " and crs.name like '%" + courseSearchDto.getSearchKey().trim() + "%'";
		}
		sqlQuery += ") A ";

		String sortingQuery = "";
		if (null != courseSearchDto.getSortingObj()) {
			CourseSearchFilterDto sortingObj = courseSearchDto.getSortingObj();
			if (null != sortingObj.getPrice() && !sortingObj.getPrice().isEmpty()) {
				if (sortingObj.getPrice().equals("ASC")) {
					sortingQuery = " order by A.cost_range asc";
				} else {
					sortingQuery = " order by A.cost_range desc";
				}
			}

			if (null != sortingObj.getLocation() && !sortingObj.getLocation().isEmpty()) {
				if (sortingObj.getLocation().equals("ASC")) {
					sortingQuery = " order by A.countryName, A.cityName asc";
				} else {
					sortingQuery = " order by A.countryName, A.cityName desc";
				}
			}

			if (null != sortingObj.getDuration() && !sortingObj.getDuration().isEmpty()) {
				if (sortingObj.getDuration().equals("ASC")) {
					sortingQuery = " order by A.duration asc";
				} else {
					sortingQuery = " order by A.duration desc";
				}
			}

			if (null != sortingObj.getRecognition() && !sortingObj.getRecognition().isEmpty()) {
				if (sortingObj.getRecognition().equals("ASC")) {
					sortingQuery = " order by A.recognition asc";
				} else {
					sortingQuery = " order by A.recognition desc";
				}
			}
		} else {
			sortingQuery = " order by A.cost_range asc";
		}
		sqlQuery += sortingQuery + " OFFSET (" + courseSearchDto.getPageNumber() + "-1)*" + courseSearchDto.getMaxSizePerPage() + " ROWS FETCH NEXT "
				+ courseSearchDto.getMaxSizePerPage() + " ROWS ONLY";

		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<CourseResponseDto> list = new ArrayList<>();
		CourseResponseDto obj = null;
		for (Object[] row : rows) {
			obj = new CourseResponseDto();
			obj.setId(String.valueOf(row[0]));
			obj.setName(String.valueOf(row[1]));
			obj.setCost(String.valueOf(row[4]) + " " + String.valueOf(row[5]));
			obj.setCourseRanking(Integer.valueOf(String.valueOf(row[12])));
			obj.setStars(Double.valueOf(String.valueOf(row[14])));
			obj.setTotalCount(Integer.parseInt(String.valueOf(row[18])));
			list.add(obj);
		}
		return obj;
	}

	// This is not recommended 
	@Override
	public List<CourseResponseDto> getCouresesByFacultyId(final String facultyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.createAlias("faculty", "faculty");
		crit.add(Restrictions.eq("faculty.id", facultyId));
		crit.addOrder(Order.asc("course.name"));
		List<Course> courses = crit.list();
		List<CourseResponseDto> dtos = new ArrayList<>();
		for (Course course : courses) {
			CourseResponseDto courseObj = new CourseResponseDto();
			courseObj.setId(course.getId());
			courseObj.setStars(Double.valueOf(course.getStars()));
			courseObj.setName(course.getName());
			courseObj.setCurrencyCode(course.getCurrency());
			if(!ObjectUtils.isEmpty(course.getFaculty())) {
				courseObj.setFacultyName(course.getFaculty().getName());
				courseObj.setFacultyId(course.getFaculty().getId());
			}
			courseObj.setCourseRanking(course.getWorldRanking());
			dtos.add(courseObj);
		}
		return dtos;
	}

	@Override
	public int findTotalCount() {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select sa.id from course sa where sa.is_active = 1 and sa.deleted_on IS NULL";
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public Page<Course> findCourseByFilters(Pageable pageable, Boolean isNotDeleted, Boolean isActive, SortingOnEnum sortingOnEnum, SortingTypeEnum sortingTypeEnum,
			 String instituteId, List<String> languages, Integer minRanking, Integer maxRanking, String countryName, String searchKey) {
		return courseRepository.findAll(CourseSpecification.findCourseByFilters(isNotDeleted,  isActive,  sortingOnEnum,  sortingTypeEnum,
				instituteId,  languages,  minRanking,  maxRanking, countryName, searchKey), pageable);
	}

	@Override
	public List<CourseDto> getUserCourse(final List<String> courseIds,final String sortBy, final boolean sortType) 
			throws ValidationException {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select c.id ,c.name, c.world_ranking, c.stars, c.description, "
				+ " c.remarks, i.name as instituteName, cai.domestic_fee, cai.international_fee, cai.usd_domestic_fee, cai.usd_international_fee,"
				+ " cai.delivery_type, cai.study_mode, cai.duration, cai.duration_time FROM course c left join institute i on c.institute_id = i.id"
				+ " left join course_delivery_modes cai on cai.course_id = c.id"
				+ " where c.is_active = 1 and c.id in ("
				+ courseIds.stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) +")";
		
		if (!StringUtils.isEmpty(sortBy) && "institute_name".contentEquals(sortBy)) {
			sqlQuery = sqlQuery + " ORDER BY i.name " + (sortType ? "ASC" : "DESC");
		} else if (!StringUtils.isEmpty(sortBy) && ("domestic_fee".contentEquals(sortBy) || "duration".contentEquals(sortBy))) {
			sqlQuery = sqlQuery + " ORDER BY cai." + sortBy + " " + (sortType ? "ASC" : "DESC");
		} else if (!StringUtils.isEmpty(sortBy)) {
			sqlQuery = sqlQuery + " ORDER BY c." + sortBy + " " + (sortType ? "ASC" : "DESC");
		} else {
			sqlQuery = sqlQuery + " ORDER BY c.created_on DESC";
		}
		Query query = session.createSQLQuery(sqlQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		List<CourseDto> courses = new ArrayList<>();
		List<CourseDeliveryModesDto> additionalInfoDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(aliasToValueMapList)) {
			aliasToValueMapList.stream().forEach(e -> {
				Map<String, Object> row = e;

				CourseDeliveryModesDto additionalInfoDto = new CourseDeliveryModesDto();
				CourseDto courseDto = new CourseDto();
				courseDto.setId(String.valueOf(row.get("id")));
				courseDto.setName(String.valueOf(row.get("name")));
				courseDto.setWorldRanking(String.valueOf(row.get("world_ranking")));
				courseDto.setStars(String.valueOf(row.get("stars")));
				courseDto.setDescription(String.valueOf(row.get("description")));
				courseDto.setRemarks(String.valueOf(row.get("remarks")));
				courseDto.setInstituteName(String.valueOf(row.get("instituteName")));

				additionalInfoDto.setDeliveryType(String.valueOf(row.get("delivery_type")));
				additionalInfoDto.setStudyMode(String.valueOf(row.get("study_mode")));

				String duration = String.valueOf(row.get("duration"));
				additionalInfoDto.setDuration(StringUtils.isEmpty(duration) ? Double.parseDouble(duration) : null);

				additionalInfoDto.setDurationTime(String.valueOf(row.get("duration_time")));
				additionalInfoDto.setCourseId(String.valueOf(row.get("id")));
				additionalInfoDtos.add(additionalInfoDto);
				courseDto.setCourseDeliveryModes(additionalInfoDtos);
				courses.add(courseDto);
			});
		}
		return courses;
	}

	@Override
	public int findTotalCountByUserId(final String userId) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select count(*) from  user_my_course umc inner join course c on umc.course_id = c.id where umc.is_active = 1 and c.is_active = 1 and umc.deleted_on IS NULL and umc.user_id='"+ userId + "'";
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	public Course getCourseData(final String id) {
		return courseRepository.findById(id).get();
	}

	@Override
	public int getCountOfAdvanceSearch(List<String> entityIds, final Object... values) {
		AdvanceSearchDto courseSearchDto = (AdvanceSearchDto) values[0];
		GlobalFilterSearchDto globalSearchFilterDto = null;

		if (values.length > 1) {
			globalSearchFilterDto = (GlobalFilterSearchDto) values[1];
		}
		String sizeSqlQuery = "select count(*) from course crs inner join institute inst "
				+ " on crs.institute_id = inst.id"
				+ " left join course_delivery_modes cai on cai.course_id = crs.id"
				+ " left join institute_service iis  on iis.institute_id = inst.id where 1=1 and crs.is_active=1";
		
		if(!CollectionUtils.isEmpty(entityIds)) {
			sizeSqlQuery += " and crs.id NOT IN ( :entityIds )";
		}
		
		if (globalSearchFilterDto != null && globalSearchFilterDto.getIds() != null && globalSearchFilterDto.getIds().size() > 0) {
			sizeSqlQuery += " and crs.id in (:gloabalSearchFilterCourseIds)";
		}
		sizeSqlQuery = addCondition(sizeSqlQuery, courseSearchDto);
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sizeSqlQuery);
		addParameter(query, courseSearchDto);
		if(!CollectionUtils.isEmpty(entityIds)) {
			query.setParameterList("entityIds", entityIds);
		}
		
		if (globalSearchFilterDto != null && globalSearchFilterDto.getIds() != null && globalSearchFilterDto.getIds().size() > 0) {
			query.setParameterList("gloabalSearchFilterCourseIds",globalSearchFilterDto.getIds());
		}
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<CourseResponseDto> advanceSearch(List<String> entityIds, final Object... values) {
		AdvanceSearchDto courseSearchDto = (AdvanceSearchDto) values[0];
		GlobalFilterSearchDto globalSearchFilterDto = null;

		if (values.length > 1) {
			globalSearchFilterDto = (GlobalFilterSearchDto) values[1];
		}
		Session session = sessionFactory.getCurrentSession();

		String sqlQuery = "select distinct crs.id as courseId,crs.name as courseName, inst.id as instId,inst.name as instName,"
				+ " crs.currency,cai.duration,cai.duration_time,inst.city_name as cityName,"
				+ " inst.country_name as countryName,inst.world_ranking,crs.stars,crs.recognition, cai.domestic_fee, cai.international_fee,"
				+ " crs.remarks, usd_domestic_fee, usd_international_fee,inst.latitude as latitude,inst.longitude as longitude,"
				+ " cai.delivery_type as deliveryType, cai.study_mode"
				+ " from course crs inner join institute inst"
				+ " on crs.institute_id = inst.id"
				+ " left join course_delivery_modes cai on cai.course_id = crs.id"
				+ " inner join faculty f on f.id = crs.faculty_id"
				+ " left join institute_service iis on iis.institute_id = inst.id where 1=1 and crs.is_active=1";

		if(!CollectionUtils.isEmpty(entityIds)) {
			sqlQuery += " and crs.id NOT IN ( :entityIds )";
		}
		
		if (globalSearchFilterDto != null && globalSearchFilterDto.getIds() != null && globalSearchFilterDto.getIds().size() > 0) {
			sqlQuery += " and crs.id in (:gloabalSearchFilterCourseIds)";
		}
		boolean showIntlCost = false;
		sqlQuery = addCondition(sqlQuery, courseSearchDto);
//		sqlQuery += " group by crs.id";

		String sortingQuery = "";
		if (courseSearchDto.getSortBy() != null && !courseSearchDto.getSortBy().isEmpty()) {
			sortingQuery = addSorting(sortingQuery, courseSearchDto);
		}
		if (courseSearchDto.getPageNumber() != null && courseSearchDto.getMaxSizePerPage() != null) {
			PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage());
			sqlQuery += sortingQuery + " LIMIT " + PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage()) + " ,"
					+ courseSearchDto.getMaxSizePerPage();
		} else {
			sqlQuery += sortingQuery;
		}
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		addParameter(query, courseSearchDto);
		if(!CollectionUtils.isEmpty(entityIds)) {
			query.setParameterList("entityIds", entityIds);
		}
		
		if (globalSearchFilterDto != null && globalSearchFilterDto.getIds() != null && globalSearchFilterDto.getIds().size() > 0) {
			query.setParameterList("gloabalSearchFilterCourseIds",globalSearchFilterDto.getIds());
		}
		
		List<Object[]> rows = query.list();

		List<CourseResponseDto> list = new ArrayList<>();
		List<CourseDeliveryModesDto> additionalInfoDtos = new ArrayList<>();
		CourseResponseDto courseResponseDto = null;
		for (Object[] row : rows) {
			courseResponseDto = getCourseData(row, courseSearchDto, showIntlCost,additionalInfoDtos);
			list.add(courseResponseDto);
		}
		return list;
	}

	private CourseResponseDto getCourseData(final Object[] row, final AdvanceSearchDto courseSearchDto, final boolean showIntlCost, 
			List<CourseDeliveryModesDto> additionalInfoDtos) {
		CourseResponseDto courseResponseDto = null;
		CourseDeliveryModesDto additionalInfoDto = new CourseDeliveryModesDto();
		courseResponseDto = new CourseResponseDto();
		courseResponseDto.setId(String.valueOf(row[0]));
		courseResponseDto.setName(String.valueOf(row[1]));
		courseResponseDto.setInstituteId(String.valueOf(row[2]));
		courseResponseDto.setInstituteName(String.valueOf(row[3]));
		courseResponseDto.setCityName(String.valueOf(row[7]));
		courseResponseDto.setCountryName(String.valueOf(row[8]));
		courseResponseDto.setLocation(String.valueOf(row[7]) + ", " + String.valueOf(row[8]));

		Integer worldRanking = 0;
		if (null != row[9]) {
			worldRanking = Double.valueOf(String.valueOf(row[9])).intValue();
		}
		courseResponseDto.setCourseRanking(worldRanking);
		courseResponseDto.setStars(Double.valueOf(String.valueOf(row[10])));
		courseResponseDto.setRequirements(String.valueOf(row[15]));
		if (row[4] != null) {
			courseResponseDto.setCurrencyCode(row[4].toString());
		}
		courseResponseDto.setLatitude((Double) row[17]);
		courseResponseDto.setLongitude((Double) row[18]);
		
		//Course Additional Info
		additionalInfoDto.setDuration(Double.valueOf(String.valueOf(row[5])));
		additionalInfoDto.setDurationTime(String.valueOf(row[6]));
		additionalInfoDto.setDeliveryType(String.valueOf(row[19]));
		additionalInfoDto.setStudyMode(String.valueOf(row[20]));
		additionalInfoDto.setCourseId(String.valueOf(row[0]));

		additionalInfoDtos.add(additionalInfoDto);
		courseResponseDto.setCourseDeliveryModes(additionalInfoDtos);
		return courseResponseDto;
	}

	private String addSorting(String sortingQuery, final AdvanceSearchDto courseSearchDto) {
		String sortTypeValue = "ASC";
		if (!courseSearchDto.isSortAsscending()) {
			sortTypeValue = "DESC";
		}
		if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.NAME.toString())) {
			sortingQuery = sortingQuery + " ORDER BY crs.name " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.DURATION.toString())) {
			sortingQuery = sortingQuery + " ORDER BY cai.duration " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.RECOGNITION.toString())) {
			sortingQuery = sortingQuery + " ORDER BY crs.recognition " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.DOMESTIC_PRICE.toString())) {
			sortingQuery = sortingQuery + " ORDER BY cai.domestic_fee " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.INTERNATION_PRICE.toString())) {
			sortingQuery = sortingQuery + " ORDER BY cai.international_fee " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.CREATED_DATE.toString())) {
			sortingQuery = sortingQuery + " ORDER BY crs.created_on " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.LOCATION.toString())) {
			sortingQuery = sortingQuery + " ORDER BY inst.country_name " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.PRICE.toString())) {
			sortingQuery = sortingQuery + " ORDER BY IF(crs.currency='" + courseSearchDto.getCurrencyCode()
					+ "', cai.usd_domestic_fee, cai.usd_international_fee) " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase("instituteName")) {
			sortingQuery = " order by inst.name " + sortTypeValue.toLowerCase();
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase("countryName")) {
			sortingQuery = " order by inst.country_name " + sortTypeValue.toLowerCase();
		}
		return sortingQuery;
	}

	private String addCondition(String sqlQuery, final AdvanceSearchDto courseSearchDto) {
		if (null != courseSearchDto.getCountryNames() && !courseSearchDto.getCountryNames().isEmpty()) {
			sqlQuery += " and inst.country_name in (:countryNames)";
		}
		if (null != courseSearchDto.getCityNames() && !courseSearchDto.getCityNames().isEmpty()) {
			sqlQuery += " and inst.city_name in (:cityNames)";
		}
		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and crs.level_id in (:levelIds)";
		}

		if (null != courseSearchDto.getFaculties() && !courseSearchDto.getFaculties().isEmpty()) {
			sqlQuery += " and crs.faculty_id in (:facultyIds)";
		}

		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			sqlQuery += " and crs.name in (:courseKeys)";
		}
		/**
		 * This is added as in advanced search names are to be passed now, so not
		 * disturbing the already existing code, this condition has been kept in place.
		 */
		else if (null != courseSearchDto.getNames() && !courseSearchDto.getNames().isEmpty()) {
			sqlQuery += " and crs.name in (:courseNames)";
		}

		if (null != courseSearchDto.getServiceIds() && !courseSearchDto.getServiceIds().isEmpty()) {
			sqlQuery += " and iis.service_id in (:serviceIds)";
		}

		if(!StringUtils.isEmpty(courseSearchDto.getUserCountryName())) {
			if(null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0 && 
					null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
				sqlQuery += "and ((inst.country_name = :userCountryName and cai.usd_domestic_fee >="+ courseSearchDto.getMinCost() +
						" and cai.usd_domestic_fee <= "+ courseSearchDto.getMaxCost() +")" +
						" OR (cai.usd_international_fee >= "+ courseSearchDto.getMinCost() +" and cai.usd_international_fee <= " + courseSearchDto.getMaxCost() +"))";
			} else {
				if(null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0) {
					sqlQuery += "and ((inst.country_name = :userCountryName and cai.usd_domestic_fee >= "+ courseSearchDto.getMinCost() +") OR (inst.country_name != '" + courseSearchDto.getUserCountryName() +"'"
							+ " and cai.usd_international_fee >= "+ courseSearchDto.getMinCost() +"))";
				} else if (null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
					sqlQuery += "and ((inst.country_name = :userCountryName and cai.usd_domestic_fee <= "+ courseSearchDto.getMaxCost() +") OR (inst.country_name != '" + courseSearchDto.getUserCountryName() +"'"
							+ " and cai.usd_international_fee <= "+ courseSearchDto.getMaxCost() +"))";
				}
			}
		}
		
		if (null != courseSearchDto.getMinDuration() && courseSearchDto.getMinDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) >= " + courseSearchDto.getMinDuration();
		}

		if (null != courseSearchDto.getMaxDuration() && courseSearchDto.getMaxDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) <= " + courseSearchDto.getMaxDuration();
		}

		if (null != courseSearchDto.getInstituteId()) {
			sqlQuery += " and inst.id = :instituteId";
		}

		if (courseSearchDto.getSearchKeyword() != null) {
			sqlQuery += " and ( inst.name like %:searchKeyword%";
			sqlQuery += " or inst.country_name like %:searchKeyword%";
			sqlQuery += " or crs.name like %:searchKeyword% )";
		}

		if (courseSearchDto.getStudyModes() != null && !CollectionUtils.isEmpty(courseSearchDto.getStudyModes())) {
			sqlQuery += " and cai.study_mode in (:studyModes)";
		}

		if (courseSearchDto.getDeliveryMethods() != null && !CollectionUtils.isEmpty(courseSearchDto.getDeliveryMethods())) {
			sqlQuery += " and cai.delivery_type in (:deliveryMethods)";
		}

		/**
		 * This filter is added to get domestic courses from user country and
		 * international courses from other countries, the courses with availbilty ='A'
		 * will be shown to all users and with availbilty='N' will be shown to no one.
		 *
		 */
		if (null != courseSearchDto.getUserCountryName()) {
			sqlQuery += " and ((inst.country_name =:userCountryName and crs.availabilty = 'D') OR (inst.country_name <>'"
					+ courseSearchDto.getUserCountryName() + "' and crs.availabilty = 'I') OR crs.availabilty = 'A')";
		}
		return sqlQuery;
	}
	private void addParameter(Query query , final AdvanceSearchDto courseSearchDto) {
		if (null != courseSearchDto.getCountryNames() && !courseSearchDto.getCountryNames().isEmpty()) {
			query.setParameterList("countryNames", courseSearchDto.getCountryNames());
		}
		if (null != courseSearchDto.getCityNames() && !courseSearchDto.getCityNames().isEmpty()) {
			query.setParameterList("cityNames", courseSearchDto.getCityNames());
		}
		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			query.setParameterList("levelIds", courseSearchDto.getLevelIds());
		}
		
		if (null != courseSearchDto.getFaculties() && !courseSearchDto.getFaculties().isEmpty()) {
			query.setParameterList("facultyIds", courseSearchDto.getFaculties());
		}
		
		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			query.setParameterList("courseKeys", courseSearchDto.getCourseKeys());
		}
		/**
		 * This is added as in advanced search names are to be passed now, so not
		 * disturbing the already existing code, this condition has been kept in place.
		 */
		else if (null != courseSearchDto.getNames() && !courseSearchDto.getNames().isEmpty()) {
			query.setParameterList("courseNames", courseSearchDto.getNames());
		}
		
		if (null != courseSearchDto.getServiceIds() && !courseSearchDto.getServiceIds().isEmpty()) {
			query.setParameterList("serviceIds", courseSearchDto.getServiceIds());
		}
		
		if(!StringUtils.isEmpty(courseSearchDto.getUserCountryName())) {
			
			query.setParameter("userCountryName", courseSearchDto.getUserCountryName());
		}
		
		if (null != courseSearchDto.getInstituteId()) {
			query.setParameter("instituteId", courseSearchDto.getInstituteId());
		}
		
		if (courseSearchDto.getSearchKeyword() != null) {
			query.setParameter("searchKeyword", courseSearchDto.getSearchKeyword().trim());
		}
		
		if (courseSearchDto.getStudyModes() != null && !CollectionUtils.isEmpty(courseSearchDto.getStudyModes())) {
			query.setParameterList("studyModes", courseSearchDto.getStudyModes());
		}
		
		if (courseSearchDto.getDeliveryMethods() != null && !CollectionUtils.isEmpty(courseSearchDto.getDeliveryMethods())) {
			query.setParameterList("deliveryMethods", courseSearchDto.getDeliveryMethods());
		}
	}

	@Override
	public Long autoSearchTotalCount(final String searchKey) {
		Session session = sessionFactory.getCurrentSession();
		BigInteger count = (BigInteger) session.createNativeQuery(
				"select count(*) FROM course c inner join institute ist on c.institute_id = ist.id" + 
				" where c.is_active = 1 and c.deleted_on IS NULL and (c.name like '%" + searchKey + "%' or ist.name like '%" + searchKey + 
				" %') ORDER BY c.created_on DESC ").uniqueResult();
		return count != null ? count.longValue() : 0L;
	}

	@Override
	public List<Course> facultyWiseCourseForTopInstitute(final List<Faculty> facultyList, final Institute institute) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.add(Restrictions.eq("institute", institute));
		crit.add(Restrictions.in("faculty", facultyList));
		return crit.list();
	}

	@Override
	public long getCourseCountForCountry(final String country) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.add(Restrictions.eq("country", country));
		crit.setProjection(Projections.rowCount());
		return (long) crit.uniqueResult();
	}

	/*@Override
	public List<Course> getTopRatedCoursesForCountryWorldRankingWise(final String country) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.add(Restrictions.eq("country", country));
		crit.addOrder(Order.desc("worldRanking"));
		return crit.list();
	}*/

	/*@Override
	public List<Course> getAllCourseForFacultyWorldRankingWise(final String facultyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.createAlias("course.faculty", "courseFaculty");
		crit.add(Restrictions.eq("courseFaculty.id",facultyId));
		crit.addOrder(Order.asc("worldRanking"));
		return crit.list();
	}*/

	/*@Override
	public List<String> getAllCourseForFacultyWorldRankingWises(final String facultyId) {

		Session session = sessionFactory.getCurrentSession();
		List<String> courseList = session.createNativeQuery("select id from course c where c.faculty_id = ? order by c.world_ranking asc")
				.setString(1, facultyId).getResultList();
		return courseList;

	}*/

	/*@Override
	public List<Course> getCoursesFromId(final List<String> allSearchCourses) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.add(Restrictions.in("id", allSearchCourses));
		return crit.list();
	}*/

	/*@Override
	public Map<String, String> facultyWiseCourseIdMapForInstitute(final List<Faculty> facultyList, final String instituteId) {
		Map<String, String> mapOfCourseIdFacultyId = new HashMap<>();
		StringBuilder facultyIds = new StringBuilder();
		int count = 0;

		facultyIds = new StringBuilder();
		for (Faculty faculty : facultyList) {
			count++;

			facultyIds.append(faculty.getId());
			if (count < facultyList.size() - 1) {
				facultyIds.append(",");
			}
		}

		Session session = sessionFactory.getCurrentSession();
		List<Object[]> rows = session
				.createNativeQuery("Select id, faculty_id from course where institute_id = ? and faculty_id in (" + facultyIds + ") and id is not null")
				.setString(1, instituteId).getResultList();

		for (Object[] row : rows) {
			mapOfCourseIdFacultyId.put(row[0].toString(), row[1].toString());
		}

		return mapOfCourseIdFacultyId;
	}*/

	@Override
	public List<Course> getAllCoursesUsingId(final List<String> listOfRecommendedCourseIds) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.add(Restrictions.in("id", listOfRecommendedCourseIds));
		return crit.list();
	}

	/*@Override
	public List<String> getTopRatedCourseIdsForCountryWorldRankingWise(final String country) {
		Session session = sessionFactory.getCurrentSession();
		List<String> rows = session.createNativeQuery("select id from course where country_name = ? order by world_ranking desc")
				.setString(1, country).getResultList();
		List<String> courseIds = rows;

		return courseIds;
	}*/

	@Override
	public Long getCountOfDistinctInstitutesOfferingCoursesForCountry(final UserDto userDto, final String country) {
		Session session = sessionFactory.getCurrentSession();

		BigInteger count = (BigInteger) session.createNativeQuery(
				"select count(*) from (Select count(*) from course where country_id = ? and is_active = ? group by country_name, institute_id) as temp_table")
				.setParameter(1, country).setParameter(2, 1).uniqueResult();
		return count != null ? count.longValue() : 0L;
	}

	@Override
	public List<String> getDistinctCountryBasedOnCourses(final List<String> topSearchedCourseIds) {
		Session session = sessionFactory.getCurrentSession();
		String ids = topSearchedCourseIds.stream().map(String::toString).collect(Collectors.joining(","));
		System.out.println("IDs -- " + ids);
		List<String> countryIds = session
                .createNativeQuery("Select distinct i.country_name from course c join institute i on i.id=c.institute_id  where i.country_name is not null and " + "c.id in ('" + ids.replace(",", "','") + "')").getResultList();
		return countryIds;
	}

	@Override
	public List<String> getCourseListForCourseBasedOnParameters(final String courseId, final String instituteId, final String facultyId,
			final String countryId, final String cityId) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder query = new StringBuilder();
		query.append("Select c.id from course c left join institute i on c.institute_id = i.id where 1=1");
		if (courseId != null) {
			query.append(" and c.id = '" + courseId + "'");
		}
		if (instituteId != null) {
			query.append(" and c.institute_id ='" + instituteId + "'");
		}
		if (facultyId != null) {
			query.append(" and c.faculty_id = '" + facultyId + "'");
		}
		if (countryId != null) {
			query.append(" and i.country_name = " + countryId);
		}
		if (cityId != null) {
			query.append(" and i.city_name = " + cityId);
		}
		List<String> courseIds = session.createNativeQuery(query.toString()).getResultList();
		return courseIds;
	}

	@Override
	public List<String> getCourseIdsForCountry(final String country) {
		Session session = sessionFactory.getCurrentSession();
		List<String> courseList = session.createNativeQuery("select c.id from course c left join institute i on i.id = c.institute_id"
				+ " where i.country_name ='"+country+"'").getResultList();
		return courseList;
	}

	@Override
	public List<String> getAllCoursesForCountry(final List<String> otherCountryIds) {
		Session session = sessionFactory.getCurrentSession();
		String ids = otherCountryIds.stream().map(String::toString).collect(Collectors.joining(","));
		List<String> courseIdList = session.createNativeQuery("Select c.id from course c left join institute i on i.id = c.institute_id"
				+ " where i.country_name in ('" + ids.replace("'", "") + "')").getResultList();
		return courseIdList;
	}

	@Override
	public int updateCourseForCurrency(final CurrencyRateDto currencyRate) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println(currencyRate);
		Integer count = session.createNativeQuery(
				"update course_delivery_modes cai inner join course c on c.id = cai.course_id " + 
				"set cai.usd_domestic_fee = domestic_fee * ?, cai.usd_international_fee = international_fee * ?, cai.updated_on = now() where c.currency = ?")
				.setParameter(1, 1 / currencyRate.getConversionRate()).setParameter(2, 1 / currencyRate.getConversionRate())
				.setParameter(3, currencyRate.getToCurrencyCode()).executeUpdate();
		System.out.println("courses updated for " + currencyRate.getToCurrencyCode() + "-" + count);
		return count;
	}

	@Override
	public Integer getTotalCourseCountForInstitute(final String instituteId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Course.class, "course");
		criteria.createAlias("institute", "institute");
		criteria.add(Restrictions.eq("institute.id", instituteId));
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count == null ? 0 : count.intValue();
	}

	@Override
	public List<CourseSyncDTO> getUpdatedCourses(final Date updatedOn, final Integer startIndex, final Integer limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNativeQuery("select crs.id, crs.name, crs.world_ranking as courseRanking, \r\n" + "crs.stars,crs.recognition,\r\n"
				+ "crs.duration, \r\n" + "crs.website, crs.language, crs.abbreviation,\r\n" + "crs.rec_date ,crs.remarks, crs.description,\r\n"
				+ "crs.is_active, crs.created_on, crs.updated_on,\r\n" + "crs.deleted_on, crs.created_by, crs.updated_by, crs.is_deleted,\r\n"
				+ "crs.availbilty, crs.part_full, crs.study_mode, crs.international_fee,\r\n"
				+ "crs.domestic_fee, crs.currency, crs.currency_time, crs.usd_international_fee,\r\n"
				+ "crs.usd_domestic_fee, crs.cost_range, crs.content, \r\n" + "inst.id as institute_id, inst.name as institute_name, fac.id as faculty_id,\r\n"
				+ "fac.name as faculty_name,\r\n" + "fac.description as faculty_description, cntry.name as country_name,\r\n"
				+ "ct.name as city_name, lev.id as level_id, lev.code as level_code, lev.name as level_name, crs.recognition_type,"
				+ "crs.duration_time from course crs \r\n" + "inner join institute inst on crs.institute_id = inst.id\r\n"
				+ "inner join faculty fac on crs.faculty_id = fac.id\r\n" + "inner join country cntry on inst.country_id = cntry.id\r\n"
				+ "inner join city ct on crs.city_id = ct.id\r\n" + "inner join level lev on crs.level_id = lev.id\r\n" + "where crs.updated_on >= ? \r\n"
				+ "limit ?,?;");
		query.setParameter(1, updatedOn).setParameter(2, startIndex).setParameter(3, limit);

		List<Object[]> rows = query.list();
		List<CourseSyncDTO> courseElasticSearchList = new ArrayList<>();
		for (Object[] objects : rows) {
			CourseSyncDTO courseDtoElasticSearch = new CourseSyncDTO();
			courseDtoElasticSearch.setId(String.valueOf(objects[0]));
			courseDtoElasticSearch.setName(String.valueOf(objects[1]));
			if (String.valueOf(objects[2]) != null && !String.valueOf(objects[2]).isEmpty() && !"null".equalsIgnoreCase(String.valueOf(objects[2]))) {
				courseDtoElasticSearch.setWorldRanking(Integer.valueOf(String.valueOf(objects[2])));
			} else {
				courseDtoElasticSearch.setWorldRanking(null);
			}

			if (String.valueOf(objects[3]) != null && !String.valueOf(objects[3]).isEmpty() && !"null".equalsIgnoreCase(String.valueOf(objects[3]))) {
				courseDtoElasticSearch.setStars(Integer.valueOf(String.valueOf(objects[3])));
			} else {
				courseDtoElasticSearch.setStars(null);
			}

			courseDtoElasticSearch.setRecognition(String.valueOf(objects[4]));
			courseDtoElasticSearch.setWebsite(String.valueOf(objects[6]));
			courseDtoElasticSearch.setAbbreviation(String.valueOf(objects[8]));
			courseDtoElasticSearch.setRemarks(String.valueOf(objects[10]));
			courseDtoElasticSearch.setDescription(String.valueOf(objects[11]));
			courseDtoElasticSearch.setAvailabilty(String.valueOf(objects[19]));

			courseDtoElasticSearch.setCurrency(String.valueOf(objects[24]));
			courseDtoElasticSearch.setCurrencyTime(String.valueOf(objects[25]));

			courseDtoElasticSearch.setContent(String.valueOf(objects[29]));
			
			InstituteSyncDTO institute = new InstituteSyncDTO();
			institute.setId(String.valueOf(objects[30]));
			institute.setName(String.valueOf(objects[31]));
			courseDtoElasticSearch.setInstitute(institute);
			
			FacultyDto facultyDto = new FacultyDto();
			facultyDto.setId(String.valueOf(objects[32]));
			facultyDto.setName(String.valueOf(objects[33]));
			facultyDto.setDescription(String.valueOf(objects[34]));
			courseDtoElasticSearch.setFaculty(facultyDto);
			
		//	courseDtoElasticSearch.setCountryName(String.valueOf(objects[35]));
		//	courseDtoElasticSearch.setCityName(String.valueOf(objects[36]));

			LevelDto levelDto = new LevelDto();
			levelDto.setId(String.valueOf(objects[37]));
			levelDto.setCode(String.valueOf(objects[38]));
			levelDto.setName(String.valueOf(objects[39]));
			courseDtoElasticSearch.setLevel(levelDto);
			
			courseDtoElasticSearch.setRecognitionType(String.valueOf(objects[40]));
			courseElasticSearchList.add(courseDtoElasticSearch);
		}
		return courseElasticSearchList;
	}

	@Override
	public Integer getCountOfTotalUpdatedCourses(final Date utCdatetimeAsOnlyDate) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Course.class, "course");
		criteria.add(Restrictions.ge("updatedOn", utCdatetimeAsOnlyDate));
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count != null ? count.intValue() : 0;
	}

	@Override
	public List<CourseSyncDTO> getCoursesToBeRetriedForElasticSearch(final List<String> courseIds, final Integer startIndex, final Integer limit) {
		if (courseIds == null || courseIds.isEmpty()) {
			return new ArrayList<>();
		}
		Session session = sessionFactory.getCurrentSession();
		StringBuilder queryString = new StringBuilder("select crs.id, crs.name, crs.world_ranking as courseRanking, \r\n" + "crs.stars,crs.recognition,\r\n"
				+ "crs.duration, \r\n" + "crs.website, crs.language, crs.abbreviation,\r\n" + "crs.rec_date ,crs.remarks, crs.description,\r\n"
				+ "crs.is_active, crs.created_on, crs.updated_on,\r\n" + "crs.deleted_on, crs.created_by, crs.updated_by, crs.is_deleted,\r\n"
				+ "crs.availbilty, crs.part_full, crs.study_mode, crs.international_fee,\r\n"
				+ "crs.domestic_fee, crs.currency, crs.currency_time, crs.usd_international_fee,\r\n"
				+ "crs.usd_domestic_fee, crs.cost_range, crs.content, \r\n" + "inst.id as institute_id, inst.name as institute_name, fac.id as faculty_id, fac.name as faculty_name,\r\n"
				+ "fac.description as faculty_description, cntry.name as country_name,\r\n"
				+ "ct.name as city_name, lev.id as level_id, lev.code as level_code, lev.name as level_name, crs.recognition_type,"
				+ "crs.duration_time from course crs \r\n" + "inner join institute inst on crs.institute_id = inst.id\r\n"
				+ "inner join faculty fac on crs.faculty_id = fac.id\r\n" + "inner join country cntry on inst.country_id = cntry.id\r\n"
				+ "inner join city ct on crs.city_id = ct.id\r\n" + "inner join level lev on crs.level_id = lev.id\r\n" + "where crs.id in (");

		for (int i = 0; i < courseIds.size(); i++) {
			queryString.append("?");
			if (!(i == courseIds.size() - 1)) {
				queryString.append(",");
			}
		}
		queryString.append(")");
		Query query = session.createNativeQuery(queryString.toString());
		for (int i = 0; i < courseIds.size(); i++) {
			query.setParameter(i + 1, courseIds.get(i));
		}

		List<Object[]> rows = query.list();
		List<CourseSyncDTO> courseElasticSearchList = new ArrayList<>();
		for (Object[] objects : rows) {
			CourseSyncDTO courseDtoElasticSearch = new CourseSyncDTO();
			courseDtoElasticSearch.setId(String.valueOf(objects[0]));
			courseDtoElasticSearch.setName(String.valueOf(objects[1]));
			if (String.valueOf(objects[2]) != null && !String.valueOf(objects[2]).isEmpty() && !"null".equalsIgnoreCase(String.valueOf(objects[2]))) {
				courseDtoElasticSearch.setWorldRanking(Integer.valueOf(String.valueOf(objects[2])));
			} else {
				courseDtoElasticSearch.setWorldRanking(null);
			}

			if (String.valueOf(objects[3]) != null && !String.valueOf(objects[3]).isEmpty() && !"null".equalsIgnoreCase(String.valueOf(objects[3]))) {
				courseDtoElasticSearch.setStars(Integer.valueOf(String.valueOf(objects[3])));
			} else {
				courseDtoElasticSearch.setStars(null);
			}

			courseDtoElasticSearch.setRecognition(String.valueOf(objects[4]));
			courseDtoElasticSearch.setWebsite(String.valueOf(objects[6]));
			courseDtoElasticSearch.setAbbreviation(String.valueOf(objects[8]));
			courseDtoElasticSearch.setRemarks(String.valueOf(objects[10]));
			courseDtoElasticSearch.setDescription(String.valueOf(objects[11]));
			courseDtoElasticSearch.setAvailabilty(String.valueOf(objects[19]));
			
			courseDtoElasticSearch.setCurrency(String.valueOf(objects[24]));
			courseDtoElasticSearch.setCurrencyTime(String.valueOf(objects[25]));

			courseDtoElasticSearch.setContent(String.valueOf(objects[29]));

			InstituteSyncDTO institute = new InstituteSyncDTO();
			institute.setId(String.valueOf(objects[30]));
			institute.setName(String.valueOf(objects[31]));
			courseDtoElasticSearch.setInstitute(institute);
			
			FacultyDto facultyDto = new FacultyDto();
			facultyDto.setId(String.valueOf(objects[32]));
			facultyDto.setName(String.valueOf(objects[33]));
			facultyDto.setDescription(String.valueOf(objects[34]));
			courseDtoElasticSearch.setFaculty(facultyDto);
			
//			courseDtoElasticSearch.setCountryName(String.valueOf(objects[35]));
//			courseDtoElasticSearch.setCityName(String.valueOf(objects[36]));

			LevelDto levelDto = new LevelDto();
			levelDto.setId(String.valueOf(objects[37]));
			levelDto.setCode(String.valueOf(objects[38]));
			levelDto.setName(String.valueOf(objects[39]));
			courseDtoElasticSearch.setLevel(levelDto);
			
			courseDtoElasticSearch.setRecognitionType(String.valueOf(objects[40]));
			courseElasticSearchList.add(courseDtoElasticSearch);
		}
		return courseElasticSearchList;
	}

	@Override
	public List<CourseIntake> getCourseIntakeBasedOnCourseId(final String courseId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(CourseIntake.class, "courseIntake");
		crit.createAlias("courseIntake.course", "course");
		crit.add(Restrictions.eq("course.id", courseId));
		return crit.list();
	}

	@Override
	public void deleteCourseDeliveryMethod(final String courseId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "delete CourseDeliveryMethod where course_id = :courseId";
		Query q = session.createQuery(hql).setParameter("courseId", courseId);
		q.executeUpdate();
	}

	@Override
	public void saveCourseLanguage(final CourseLanguage courseLanguage) {
		Session session = sessionFactory.getCurrentSession();
		session.save(courseLanguage);

	}

	@Override
	public List<CourseLanguage> getCourseLanguageBasedOnCourseId(final String courseId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(CourseLanguage.class, "courseLanguage");
		crit.createAlias("courseLanguage.course", "course");
		crit.add(Restrictions.eq("course.id", courseId));
		return crit.list();
	}

	@Override
	public List<String> getUserSearchCourseRecommendation(final Integer startIndex, final Integer pageSize, final String searchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Course.class);
		criteria.setProjection(Projections.property("name"));
		if (searchKeyword != null && !searchKeyword.isEmpty()) {
			criteria.add(Restrictions.ilike("name", searchKeyword, MatchMode.ANYWHERE));
		}
		if (startIndex != null && pageSize != null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(pageSize);
		}
		return criteria.list();
	}

	@Override
	public int getDistinctCourseCountbyName(String courseName) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder sqlQuery = new StringBuilder("select distinct c.name as courseName from course c");
		if (StringUtils.isNotEmpty(courseName)) {
			sqlQuery.append(" where name like ('" + "%" + courseName.trim() + "%')");
		}
		Query query = session.createSQLQuery(sqlQuery.toString());
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public List<CourseResponseDto> getDistinctCourseListByName(Integer startIndex, Integer pageSize, String courseName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Course.class).setProjection(Projections.projectionList()
				.add(Projections.groupProperty("name").as("name"))
				.add(Projections.property("id").as("id"))
				.add(Projections.property("worldRanking").as("courseRanking"))
				.add(Projections.property("currency").as("currencyCode"))
				.add(Projections.property("readableId").as("readableId")))
				.setResultTransformer(Transformers.aliasToBean(CourseResponseDto.class));
		if (StringUtils.isNotEmpty(courseName)) {
			criteria.add(Restrictions.like("name", courseName,MatchMode.ANYWHERE));
		}
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	public Integer getCoursesCountBylevelId(final String levelId) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder sqlQuery = new StringBuilder("select count(*) from  course c where c.level_id='"+ levelId + "'");
		Query query = session.createSQLQuery(sqlQuery.toString());
		Integer count =  Integer.valueOf(query.uniqueResult().toString());
		return count;
	}
	
	@Override
	public List<Course> getAllCourseByInstituteIdAndFacultyIdAndStatus(String instituteId, String facultyId, boolean isActive) {
		return courseRepository.findByInstituteIdAndFacultyIdAndIsActive(instituteId, facultyId, isActive);
	}

	@Override
	public List<Course> getAllCourseByInstituteIdAndFacultyId(String instituteId, String facultyId) {
		return courseRepository.findByInstituteIdAndFacultyId(instituteId, facultyId);
	}

	@Override
	public List<CourseResponseDto> getNearestCourseForAdvanceSearch(AdvanceSearchDto courseSearchDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQueryForCourse = "SELECT DISTINCT crs.id as courseId,crs.name as courseName,crs.world_ranking,crs.stars as stars, inst.id, inst.name as instituteName," + 
				" inst.country_name, inst.city_name, crs.currency, inst.latitude, inst.longitude, cai.duration, cai.duration_time," +
				" cai.study_mode, cai.usd_domestic_fee, cai.usd_international_fee, cai.id as additionalInfoId, cai.delivery_type,"+
				" 6371 * ACOS(SIN(RADIANS('"+ courseSearchDto.getLatitude() +"')) * SIN(RADIANS(inst.latitude)) + COS(RADIANS('"+ courseSearchDto.getLatitude() +"')) * COS(RADIANS(inst.latitude)) * COS(RADIANS(inst.longitude)-" + 
				" RADIANS('"+ courseSearchDto.getLongitude() +"'))) AS distance_in_km FROM course crs inner join institute inst on inst.id = crs.institute_id" +
				" inner join faculty f on f.id = crs.faculty_id LEFT JOIN institute_service iis on iis.institute_id = inst.id" +
				" LEFT JOIN course_delivery_modes cai on cai.course_id = crs.id where crs.is_off_campus false and inst.latitude is not null and" +
				" inst.longitude is not null and inst.latitude!= " + courseSearchDto.getLatitude() + " and inst.longitude!= " + courseSearchDto.getLongitude();
		
		
		String sqlQueryForOffCampusCourse = "SELECT DISTINCT crs.id as courseId,crs.name as courseName,crs.world_ranking,crs.stars as stars, inst.id, inst.name as instituteName," + 
				" inst.country_name, inst.city_name, crs.currency, occ.latitude, occ.longitude, cai.duration, cai.duration_time," +
				" cai.study_mode, cai.usd_domestic_fee, cai.usd_international_fee, cai.id as additionalInfoId, cai.delivery_type,"+
				" 6371 * ACOS(SIN(RADIANS('"+ courseSearchDto.getLatitude() +"')) * SIN(RADIANS(occ.latitude)) + COS(RADIANS('"+ courseSearchDto.getLatitude() +"')) * COS(RADIANS(occ.latitude)) * COS(RADIANS(occ.longitude)-" + 
				" RADIANS('"+ courseSearchDto.getLongitude() +"'))) AS distance_in_km " + 
				" FROM off_campus_course occ LEFT JOIN course crs ON crs.id = occ.course_id inner join institute inst on inst.id = crs.institute_id" +
				" inner join faculty f on f.id = crs.faculty_id LEFT JOIN institute_service iis on iis.institute_id = inst.id" +
				" LEFT JOIN course_delivery_modes cai on cai.course_id = crs.id where inst.latitude is not null and" +
				" inst.longitude is not null and occ.latitude!= " + courseSearchDto.getLatitude() + " and occ.longitude!= " + courseSearchDto.getLongitude();
		
		sqlQueryForCourse = addCondition(sqlQueryForCourse, courseSearchDto);
		sqlQueryForOffCampusCourse = addCondition(sqlQueryForOffCampusCourse, courseSearchDto);
		
	    sqlQueryForCourse += " HAVING distance_in_km <= " + courseSearchDto.getInitialRadius();
	    sqlQueryForOffCampusCourse += " HAVING distance_in_km <= " + courseSearchDto.getInitialRadius();
		
		String sortingQuery = "";
		if (courseSearchDto.getSortBy() != null && !courseSearchDto.getSortBy().isEmpty()) {
			sortingQuery = addSorting(sortingQuery, courseSearchDto);
		}
		
		if (courseSearchDto.getPageNumber() != null && courseSearchDto.getMaxSizePerPage() != null) {
			PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage());
			sqlQueryForCourse += " UNION " + sqlQueryForOffCampusCourse + " LIMIT " + PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage()) + " ,"
					+ courseSearchDto.getMaxSizePerPage();
		} else {
			sqlQueryForCourse += " UNION " + sqlQueryForOffCampusCourse ;
		}
		System.out.println(sqlQueryForCourse);
		Query query = session.createSQLQuery(sqlQueryForCourse);
		addParameter(query, courseSearchDto);
		List<Object[]> rows = query.list();
		List<CourseResponseDto> nearestCourseDTOs = new ArrayList<>();
		List<CourseDeliveryModesDto> additionalInfoDtos = new ArrayList<>();
		for (Object[] row : rows) {
			CourseResponseDto nearestCourseDTO = new CourseResponseDto();
			nearestCourseDTO.setId(String.valueOf(row[0]));
			nearestCourseDTO.setName(String.valueOf(row[1]));
			nearestCourseDTO.setCourseRanking((Integer) row[2]);
			nearestCourseDTO.setStars(Double.parseDouble(String.valueOf(row[3])));
			nearestCourseDTO.setInstituteId((String) row[4]);
			nearestCourseDTO.setInstituteName((String) row[5]);
			nearestCourseDTO.setCountryName((String) row[6]);
			nearestCourseDTO.setCityName((String) row[7]);
			nearestCourseDTO.setCurrencyCode((String) row[8]);
			nearestCourseDTO.setLatitude((Double) row[9]);
			nearestCourseDTO.setLongitude((Double) row[10]);
			
			CourseDeliveryModesDto additionalInfoDto = new CourseDeliveryModesDto();
			if(row[16] != null) {
				additionalInfoDto.setId(String.valueOf(row[16]));
				additionalInfoDto.setDeliveryType(String.valueOf(row[17]));
				if(row[13] != null) {
					additionalInfoDto.setStudyMode(String.valueOf(row[13]));
				}
				if(row[11] != null) {
					additionalInfoDto.setDuration(Double.valueOf(String.valueOf(row[11])));
				}
				if(row[12] != null) {
					additionalInfoDto.setDurationTime(String.valueOf(row[12]));
				}
//				if(row[14] != null) {
//					additionalInfoDto.setUsdDomesticFee(Double.parseDouble(String.valueOf(row[14])));
//				}
//				if(row[15] != null) {
//					additionalInfoDto.setUsdInternationalFee(Double.parseDouble(String.valueOf(row[15])));
//				}
				additionalInfoDto.setCourseId(String.valueOf(row[0]));
				additionalInfoDtos.add(additionalInfoDto);
			}
			nearestCourseDTO.setCourseDeliveryModes(additionalInfoDtos);
			nearestCourseDTOs.add(nearestCourseDTO);
		}
		return nearestCourseDTOs;
	}

	@Override
	public List<CourseResponseDto> getCourseByCountryName(Integer pageNumber, Integer pageSize, String countryName) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "Select DISTINCT crs.id as courseId, crs.name as courseName, crs.world_ranking,avg(crs.stars) as stars,"
				+ " institute.id as instituteId, institute.name as instituteName, institute.country_name, institute.city_name, "
				+ " crs.currency, institute.latitude,institute.longitude from"
				+ " course crs left join institute institute on crs.institute_id ="
				+ " institute.id where institute.country_name = '"+ countryName + "' GROUP BY crs.id limit "+ PaginationUtil.getStartIndex(pageNumber, pageSize) + " ," + pageSize;
		
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<CourseResponseDto> nearestCourseDTOs = new ArrayList<>();
		for (Object[] row : rows) {
			CourseResponseDto nearestCourseDTO = new CourseResponseDto();
			nearestCourseDTO.setId((String.valueOf(row[0])));
			nearestCourseDTO.setName(String.valueOf(row[1]));
			nearestCourseDTO.setCourseRanking((Integer) row[2]);
			nearestCourseDTO.setStars(((BigDecimal) row[3]).doubleValue());
			nearestCourseDTO.setInstituteId((String) row[4]);
			nearestCourseDTO.setInstituteName((String) row[5]);
			nearestCourseDTO.setCountryName((String) row[6]);
			nearestCourseDTO.setCityName((String) row[7]);
			nearestCourseDTO.setCurrencyCode((String) row[8]);
			nearestCourseDTO.setLatitude((Double) row[9]);
			nearestCourseDTO.setLongitude((Double) row[10]);
			nearestCourseDTO.setLocation((String) row[7] + ", " + (String) row[6]);
			nearestCourseDTOs.add(nearestCourseDTO);
		}
		return nearestCourseDTOs;
	}

	@Override
	public Integer getTotalCountOfNearestCourses(Double latitude, Double longitude, Integer initialRadius) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT course.id," + 
				" 6371 * ACOS(SIN(RADIANS('"+ latitude +"')) * SIN(RADIANS(institute.latitude)) + COS(RADIANS('"+ latitude +"')) * COS(RADIANS(institute.latitude)) *" + 
				" COS(RADIANS(institute.longitude) - RADIANS('"+ longitude +"'))) AS distance_in_km FROM institute institute inner join course on \r\n" + 
				" institute.id = course.institute_id where course.is_off_campus = true institute.latitude is not null" + 
				" and institute.longitude is not null and institute.latitude!= "+ latitude +" and institute.longitude!= " + longitude +
				" group by course.id HAVING distance_in_km <= "+initialRadius;
		String sqlQueryForOffCampus = "SELECT course.id," + 
				" 6371 * ACOS(SIN(RADIANS('"+ latitude +"')) * SIN(RADIANS(occ.latitude)) + COS(RADIANS('"+ latitude +"')) * COS(RADIANS(occ.latitude)) *" + 
				" COS(RADIANS(occ.longitude) - RADIANS('"+ longitude +"'))) AS distance_in_km FROM off_campus_course occ left join course on course.id = occ.course_id"
				+ "inner join institute institute on \r\n" + 
				" institute.id = course.institute_id where institute.occ is not null" + 
				" and occ.longitude is not null and occ.latitude!= "+ latitude +" and occ.longitude!= " + longitude +
				" group by course.id HAVING distance_in_km <= "+initialRadius;
		Query query = session.createSQLQuery(sqlQuery + " union " + sqlQueryForOffCampus);
		List<Object[]> rows = query.list();
		Integer totalCount = rows.size();
		return totalCount;
	}

	@Override
	public long getCourseCountByInstituteId(String instituteId) {
		return courseRepository.getTotalCountOfCourseByInstituteId(instituteId);
	}

	@Override
	public List<CourseResponseDto> getRelatedCourseBasedOnCareerTest(List<String> searchKeyword, Integer startIndex, Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		List<CourseResponseDto> careerJobRelatedCourseDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(searchKeyword)) {
			Integer count = 0;
			StringBuilder sqlQuery = new StringBuilder("select c.id, c.name as courseName, inst.id as instituteId, inst.name as instituteName, inst.country_name,"
					+ "inst.city_name, c.currency, c.world_ranking,c.description as courseDescription from course c left join institute inst on c.institute_id = inst.id where ");
			for(String keyword : searchKeyword) {
				count++;
				sqlQuery.append("INSTR(c.name,'" + keyword + "')");
				if(searchKeyword.size() > 1 && searchKeyword.size() != count) {
					sqlQuery.append(" OR ");
				}
			}
			sqlQuery.append(" LIMIT " + startIndex + ", " + pageSize);
			System.out.println(sqlQuery.toString());
			Query query = session.createSQLQuery(sqlQuery.toString());
			List<Object[]> rows = query.list();
			for (Object[] row : rows) {
				CourseResponseDto careerJobRelatedCourseDto = new CourseResponseDto();
				careerJobRelatedCourseDto.setId(String.valueOf(row[0]));
				careerJobRelatedCourseDto.setName(String.valueOf(row[1]));
				careerJobRelatedCourseDto.setInstituteId(String.valueOf(row[2]));
				careerJobRelatedCourseDto.setInstituteName(String.valueOf(row[3]));
				careerJobRelatedCourseDto.setCountryName(String.valueOf(row[4]));
				careerJobRelatedCourseDto.setCityName(String.valueOf(row[5]));
				careerJobRelatedCourseDto.setCurrencyCode(String.valueOf(row[6]));
				careerJobRelatedCourseDto.setCourseRanking(Integer.parseInt(String.valueOf(row[7])));
				careerJobRelatedCourseDto.setLocation(String.valueOf(row[5]) + ", " + String.valueOf(row[4]));
				careerJobRelatedCourseDto.setCourseDescription(String.valueOf(row[8]));
				careerJobRelatedCourseDtos.add(careerJobRelatedCourseDto);
			}
		}
		return careerJobRelatedCourseDtos;
	}

	@Override
	public Integer getRelatedCourseBasedOnCareerTestCount(List<String> searchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		Integer totalCount = 0;
		if(!CollectionUtils.isEmpty(searchKeyword)) {
			Integer count = 0;
			StringBuilder sqlQuery = new StringBuilder("select count(*) from course c where ");
			for(String keyword : searchKeyword) {
				count++;
				sqlQuery.append("INSTR(NAME,'" + keyword + "')");
				if(searchKeyword.size() > 1 && searchKeyword.size() != count) {
					sqlQuery.append(" OR ");
				}
			}
			System.out.println(sqlQuery.toString());
			Query query = session.createSQLQuery(sqlQuery.toString());
			totalCount = ((Number) query.getSingleResult()).intValue();
		}
		return totalCount;
	}

	@Override
	public void deleteCourse(String id) {
		courseRepository.deleteById(id);
	}
	
	@Override
	public List<Course> findByInstituteId(String instituteId) {
		return courseRepository.findByInstituteId(instituteId);
	}

	@Override
	public List<Course> findAllById(List<String> ids) {
		return courseRepository.findAllById(ids);
	}
	

	@Override
	public void deleteAll(List<Course> courses) {
		courseRepository.deleteAll(courses);
	}

	@Override
	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	@Override
	public List<Course> findByReadableIdIn(List<String> readableIds) {
		return courseRepository.findByReadableIdIn(readableIds);
	}

	@Override
	public Course findByReadableId(String readableId) {
		return courseRepository.findByReadableId(readableId);
	}

	@Override
	public CourseRequest saveDocument(CourseRequest courseRequest) {
		return courseDocumentRepository.save(courseRequest);
	}

	@Override
	public Page<CourseRequest> filterDocuments(String name, String instituteId, Pageable pageable) {
		return courseDocumentRepository.findByNameContainingIgnoreCaseAndInstituteId(name, instituteId, pageable);
	}
	
	@Override
	public Optional<CourseRequest> findDocumentById(String id) {
		return courseDocumentRepository.findById(id);
	}

	@Override
	public void deleteDocumentById(String id) {
		courseDocumentRepository.deleteById(id);
	}

	@Override
	public boolean existsById(String id) {
		return courseRepository.existsById(id);
	}

	@Override
	public boolean documentExistsById(String id) {
		return courseDocumentRepository.existsById(id);
	}

}
