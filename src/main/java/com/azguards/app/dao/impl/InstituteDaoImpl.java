package com.azguards.app.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.Institute;
import com.azguards.app.bean.InstituteCategoryType;
import com.azguards.app.bean.InstituteIntake;
import com.azguards.app.bean.InstituteService;
import com.azguards.app.dao.InstituteDao;
import com.azguards.app.dto.AdvanceSearchDto;
import com.azguards.app.dto.CourseSearchDto;
import com.azguards.app.dto.InstituteFacultyDto;
import com.azguards.app.dto.InstituteFilterDto;
import com.azguards.app.dto.InstituteGetRequestDto;
import com.azguards.app.dto.InstituteResponseDto;
import com.azguards.app.enumeration.CourseSortBy;
import com.azguards.app.repository.InstituteRepository;
import com.azguards.app.util.IConstant;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.util.DateUtil;
import com.azguards.common.lib.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
public class InstituteDaoImpl implements InstituteDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private InstituteRepository instituteRepository;

	Function<String, String> addQuotes = s -> "\"" + s + "\"";

	@Override
	public Institute addUpdateInstitute(final Institute institute) {
		return instituteRepository.saveAndFlush(institute);
	}

	@Override
	public Institute get(final String instituteId) {
		Optional<Institute> institute = instituteRepository.findById(instituteId);
		if (institute.isPresent()) {
			return institute.get();
		}
		return null;
	}

	@Override
	public int getCountOfInstitute(final CourseSearchDto courseSearchDto, final String searchKeyword,
			final String cityId, final List<String> instituteId, final Boolean isActive, final Date updatedOn,
			final Integer fromWorldRanking, final Integer toWorldRanking) {
		Session session = sessionFactory.getCurrentSession();

		String sqlQuery = "select count(distinct inst.id) from institute inst "
				+ "left join course c on c.institute_id=inst.id where 1=1 ";

		if (!CollectionUtils.isEmpty(courseSearchDto.getCountryNames())) {
			sqlQuery += " and inst.country_name in (:countryNames)";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getLevelIds())) {
			sqlQuery += " and c.level_id in (:levelIds)";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getFacultyIds())) {
			sqlQuery += " and c.faculty_id in (:facultyIds)";
		}

		sqlQuery += " and (:name is null or inst.name like %:name%) ";
		sqlQuery += " and (:cityName is null or inst.city_name = :cityName )";
		sqlQuery += " and (:isActive is null or inst.is_active = :isActive )";
		if (null != updatedOn) {
			sqlQuery += " and Date(inst.updated_on) ='" + new java.sql.Date(updatedOn.getTime()).toLocalDate() + "'";
		}
		if (null != fromWorldRanking && null != toWorldRanking) {
			sqlQuery += " and inst.world_ranking between " + fromWorldRanking + " and " + toWorldRanking;
		}

		sqlQuery += " and ( :searchKeyword is null or inst.name like %:searchKeyword%";
		sqlQuery += " or inst.country_name like %:searchKeyword%";
		sqlQuery += " or inst.city_name like %:searchKeyword%";
		sqlQuery += " or inst.institute_type like %:searchKeyword%)";

		Query query1 = session.createSQLQuery(sqlQuery).setParameter("name", courseSearchDto.getSearchKey())
				.setParameter("cityId", "cityId").setParameter("isActive", isActive)
				.setParameter("searchKeyword", searchKeyword);

		if (!CollectionUtils.isEmpty(courseSearchDto.getCountryNames())) {
			query1.setParameter("countryNames", courseSearchDto.getCountryNames());
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getLevelIds())) {
			query1.setParameter("levelIds", courseSearchDto.getLevelIds());
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getFacultyIds())) {
			query1.setParameter("facultyIds", courseSearchDto.getFacultyIds());
		}

		return ((Number) query1.uniqueResult()).intValue();
	}

	@Override
	public List<InstituteResponseDto> getAllInstitutesByFilter(final CourseSearchDto courseSearchDto,
			final String sortByField, String sortByType, final String searchKeyword, final Integer startIndex,
			final String cityId, final List<String> instituteId, final Boolean isActive, final Date updatedOn,
			final Integer fromWorldRanking, final Integer toWorldRanking) {
		Session session = sessionFactory.getCurrentSession();

		String sqlQuery = "select distinct inst.id as instId,inst.name as instName,inst.city_name as cityName,"
				+ "inst.Country_name as countryName,count(c.id) as courses, inst.world_ranking as world_ranking, MIN(c.stars) as stars "
				+ " ,inst.updated_on as updatedOn, inst.institute_type as instituteType,"
				+ " inst.is_active, inst.domestic_ranking, inst.latitude,inst.longitude,MIN(cai.usd_international_fee), MAX(cai.usd_international_fee),c.currency,"
				+ " inst.website,inst.about_us_info,instAdd.student_number,inst.email,inst.address,inst.tag_line as tagLine"
				+ " from institute inst left join institute_additional_info instAdd  on instAdd.institute_id=inst.id "
				+ "left join course c  on c.institute_id=inst.id LEFT JOIN course_delivery_modes cai on cai.course_id = c.id where 1=1 ";

		if (!CollectionUtils.isEmpty(courseSearchDto.getCountryNames())) {
			sqlQuery += " and inst.country_name in (:countryNames)";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getLevelIds())) {
			sqlQuery += " and c.level_id in (:levelIds)";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getFacultyIds())) {
			sqlQuery += " and c.faculty_id in (:facultyIds)";
		}

		sqlQuery += " and (:name is null or inst.name like %:name%) ";
		sqlQuery += " and (:cityName is null or inst.city_name = :cityName )";
		sqlQuery += " and (:isActive is null or inst.is_active = :isActive )";
		if (null != updatedOn) {
			sqlQuery += " and Date(inst.updated_on) ='" + new java.sql.Date(updatedOn.getTime()).toLocalDate() + "'";
		}
		if (null != fromWorldRanking && null != toWorldRanking) {
			sqlQuery += " and inst.world_ranking between " + fromWorldRanking + " and " + toWorldRanking;
		}

		sqlQuery += " and ( :searchKeyword is null or inst.name like %:searchKeyword%";
		sqlQuery += " or inst.country_name like %:searchKeyword%";
		sqlQuery += " or inst.city_name like %:searchKeyword%";
		sqlQuery += " or inst.institute_type like %:searchKeyword%)";

		sqlQuery += " group by inst.id ";

		String sortingQuery = "";
		if (sortByType == null) {
			sortByType = "DESC";
		}
		if (null != sortByField) {
			if (sortByField.equalsIgnoreCase("name")) {
				sortingQuery = " order by inst.name " + sortByType.toLowerCase();
			} else if (sortByField.equalsIgnoreCase("countryName")) {
				sortingQuery = " order by inst.country_name " + sortByType.toLowerCase();
			} else if (sortByField.equalsIgnoreCase("cityName")) {
				sortingQuery = " order by inst.city_name " + sortByType.toLowerCase();
			} else if (sortByField.equalsIgnoreCase("instituteType")) {
				sortingQuery = " order by inst.institute_type " + sortByType.toLowerCase();
			} else {
				sortingQuery = " order by inst.id " + sortByType.toLowerCase();
			}
		} else {
			sortingQuery = " order by inst.id " + sortByType.toLowerCase();
		}

		if (startIndex != null && courseSearchDto.getMaxSizePerPage() != null) {
			sqlQuery += sortingQuery + " LIMIT " + startIndex + " ," + courseSearchDto.getMaxSizePerPage();
		}

		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);

		if (!CollectionUtils.isEmpty(courseSearchDto.getCountryNames())) {
			query.setParameter("countryNames", courseSearchDto.getCountryNames());
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getLevelIds())) {
			query.setParameter("levelIds", courseSearchDto.getLevelIds());
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getFacultyIds())) {
			query.setParameter("facultyIds", courseSearchDto.getFacultyIds());
		}

		List<Object[]> rows = query.list();
		List<InstituteResponseDto> list = new ArrayList<>();
		InstituteResponseDto instituteResponseDto = null;
		for (Object[] row : rows) {
			instituteResponseDto = new InstituteResponseDto();
			instituteResponseDto.setId(String.valueOf(row[0]));
			instituteResponseDto.setName(String.valueOf(row[1]));
			instituteResponseDto.setLocation(String.valueOf(row[2]) + ", " + String.valueOf(row[3]));
			instituteResponseDto.setCityName(String.valueOf(row[2]));
			instituteResponseDto.setCountryName(String.valueOf(row[3]));
			instituteResponseDto.setTotalCourses(Integer.parseInt(String.valueOf(row[4])));
			Integer worldRanking = 0;
			if (null != row[5]) {
				worldRanking = Integer.parseInt(String.valueOf(row[5]));
			}
			instituteResponseDto.setWorldRanking(worldRanking);
			if (null != row[6]) {
				instituteResponseDto.setStars(Double.parseDouble(String.valueOf(row[6])));
			} else {
				instituteResponseDto.setStars(0.0);
			}

			instituteResponseDto.setInstituteType(Arrays.asList(row[8].toString()));
			if (row[10] != null) {
				instituteResponseDto.setDomesticRanking(Integer.valueOf(String.valueOf(row[10])));
			}
			if (row[11] != null) {
				instituteResponseDto.setLatitude(Double.valueOf(row[11].toString()));
			}
			if (row[12] != null) {
				instituteResponseDto.setLongitude(Double.valueOf(row[12].toString()));
			}
			if (row[13] != null) {
				instituteResponseDto.setMinPriceRange(Double.valueOf(row[13].toString()));
			}
			if (row[14] != null) {
				instituteResponseDto.setMaxPriceRange(Double.valueOf(row[14].toString()));
			}
			instituteResponseDto.setCurrency(String.valueOf(row[15]));
			instituteResponseDto.setWebsite(String.valueOf(row[16]));
			instituteResponseDto.setAboutUs(String.valueOf(row[17]));
			if (row[18] != null) {
				instituteResponseDto.setTotalStudent(Integer.valueOf(String.valueOf(row[18])));
			}
			instituteResponseDto.setEmail(String.valueOf(row[19]));
			instituteResponseDto.setAddress(String.valueOf(row[20]));
			instituteResponseDto.setTagLine(String.valueOf(row[21]));
			list.add(instituteResponseDto);
		}
		return list;
	}

	@Override
	public InstituteResponseDto getInstituteById(final String instituteId) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select distinct inst.id as instId,inst.name as instName,inst.city_name as cityName,"
				+ " inst.country_name as countryName,crs.world_ranking,crs.stars,crs.totalCourse from institute inst"
				+ " CROSS APPLY ( select count(c.id) as totalCourse, MIN(c.world_ranking) as world_ranking, MIN(c.stars) as stars from course c where"
				+ " c.institute_id = inst.id group by c.institute_id ) crs where 1=1 and inst.id =:instituteId";
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery).setParameter("instituteId", instituteId);
		List<Object[]> rows = query.list();
		InstituteResponseDto obj = null;
		for (Object[] row : rows) {
			obj = new InstituteResponseDto();
			obj.setId(String.valueOf(row[0]));
			obj.setName(String.valueOf(row[1]));
			obj.setLocation(String.valueOf(row[2]) + ", " + String.valueOf(row[3]));
			if (row[4] != null) {
				obj.setWorldRanking(Integer.valueOf(String.valueOf(row[4])));
			}
			obj.setTotalCourses(Integer.parseInt(String.valueOf(row[6])));
		}
		return obj;
	}

	@Override
	public List<Institute> searchInstitute(final String sqlQuery) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<Institute> instituteList = new ArrayList<>();
		Institute obj = null;
		for (Object[] row : rows) {
			obj = new Institute();
			obj.setId(row[0].toString());
			obj.setName(row[1].toString());
			if (row[2] != null) {
				obj.setCountryName(row[2].toString());
			}
			if (row[3] != null) {
				obj.setCityName(row[3].toString());
			}
			if (row[4] != null) {
				obj.setInstituteType(Arrays.asList(row[4].toString()));
			}
			instituteList.add(obj);
		}
		return instituteList;
	}

//	private InstituteType getInstituteType(final String id, final Session session) {
//		return session.get(InstituteType.class, id);
//	}

//	private City getCity(final String id, final Session session) {
//		return session.get(City.class, id);
//	}

	@Override
	public int findTotalCount() {
		int status = 1;
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select sa.id from institute sa where sa.is_active = " + status
				+ " and sa.deleted_on IS NULL";
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public List<InstituteGetRequestDto> getAll(final Integer pageNumber, final Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select inst.id, inst.name , inst.country_name , inst.city_name, inst.institute_type, inst.description,"
				+ " inst.latitude, inst.longitude, instAdd.student_number, inst.world_ranking, inst.accreditation, inst.email, inst.phone_number, inst.website,"
				+ "inst.address, inst.avg_cost_of_living, inst.tag_line"
				+ " FROM institute as inst left join institute_additional_info instAdd  on instAdd.institute_id=inst.id  where inst.is_active = 1 and inst.deleted_on IS NULL ORDER BY inst.created_on DESC";
		sqlQuery = sqlQuery + " LIMIT " + pageNumber + " ," + pageSize;
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<InstituteGetRequestDto> instituteList = getInstituteData(rows);
		return instituteList;
	}

	private List<InstituteGetRequestDto> getInstituteData(final List<Object[]> rows) {
		List<InstituteGetRequestDto> instituteList = new ArrayList<>();
		for (Object[] row : rows) {
			InstituteGetRequestDto instituteGetRequestDto = new InstituteGetRequestDto();
			instituteGetRequestDto.setId(row[0].toString());
			if (row[1] != null) {
				instituteGetRequestDto.setName(row[1].toString());
			}
			if (row[2] != null) {
				instituteGetRequestDto.setCountryName(row[2].toString());
			}
			if (row[3] != null) {
				instituteGetRequestDto.setCityName(row[3].toString());
			}
			if (row[4] != null) {
				instituteGetRequestDto.setInstituteType(Arrays.asList(row[4].toString()));
			}
			if (row[5] != null) {
				instituteGetRequestDto.setDescription(row[5].toString());
			}
			if (!ObjectUtils.isEmpty(row[6])) {
				instituteGetRequestDto.setLatitude(Double.parseDouble(row[6].toString()));
			}
			if (!ObjectUtils.isEmpty(row[7])) {
				instituteGetRequestDto.setLongitude(Double.parseDouble(row[7].toString()));
			}
			if (!ObjectUtils.isEmpty(row[8])) {
				instituteGetRequestDto.setTotalStudent(Integer.parseInt(row[8].toString()));
			}
			if (!ObjectUtils.isEmpty(row[9])) {
				instituteGetRequestDto.setWorldRanking(Integer.parseInt(row[9].toString()));
			}
			if (!ObjectUtils.isEmpty(row[10])) {
				instituteGetRequestDto.setAccreditation(row[10].toString());
			}
			if (!ObjectUtils.isEmpty(row[11])) {
				instituteGetRequestDto.setEmail(row[11].toString());
			}
			if (!ObjectUtils.isEmpty(row[12])) {
				instituteGetRequestDto.setPhoneNumber(row[12].toString());
			}
			if (!ObjectUtils.isEmpty(row[13])) {
				instituteGetRequestDto.setWebsite(row[13].toString());
			}
			if (!ObjectUtils.isEmpty(row[14])) {
				instituteGetRequestDto.setAddress(row[14].toString());
			}
			if (!ObjectUtils.isEmpty(row[15])) {
				instituteGetRequestDto.setAvgCostOfLiving(row[15].toString());
			}
			if (!ObjectUtils.isEmpty(row[16])) {
				instituteGetRequestDto.setTagLine(row[16].toString());
			}
			instituteList.add(instituteGetRequestDto);
		}
		return instituteList;
	}

	@Override
	public List<Institute> instituteFilter(final int pageNumber, final Integer pageSize,
			final InstituteFilterDto instituteFilterDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select inst.id, inst.name , inst.country_name , inst.city_name, inst.institute_type,"
				+ " inst.description, inst.updated_on, inst.domestic_ranking FROM institute as inst"
				+ " left join course c  on c.institute_id=inst.id where inst.is_active = 1 and inst.deleted_on IS NULL  ";

		sqlQuery += " and ( :countryName is null or inst.country_name = :countryName )";
		sqlQuery += " and ( :cityName is null or inst.city_name = :cityName )";
		sqlQuery += " and ( :instituteId is null or inst.id = :instituteId )";
		sqlQuery += " and ( :worldRanking is null or inst.world_ranking = :worldRanking )";
		sqlQuery += " and ( :instituteCategoryTypeId is null or inst.institute_category_type_id = :instituteCategoryTypeId)";
		if (instituteFilterDto.getDatePosted() != null && !instituteFilterDto.getDatePosted().isEmpty()) {
			Date postedDate = DateUtil.convertStringDateToDate(instituteFilterDto.getDatePosted());
			Calendar c = Calendar.getInstance();
			c.setTime(postedDate);
			c.add(Calendar.DATE, 1);
			postedDate = c.getTime();
			String updatedDate = DateUtil.getStringDateFromDate(postedDate);
			sqlQuery += " and (inst.created_on >= '" + instituteFilterDto.getDatePosted() + "' and inst.created_on < '"
					+ updatedDate + "')";
		}
		sqlQuery = sqlQuery + " LIMIT " + pageNumber + " ," + pageSize;
		Query query = session.createSQLQuery(sqlQuery).setParameter("countryName", instituteFilterDto.getCountryName())
				.setParameter("cityName", instituteFilterDto.getCityName())
				.setParameter("instituteId", instituteFilterDto.getInstituteId())
				.setParameter("worldRanking", instituteFilterDto.getWorldRanking())
				.setParameter("instituteCategoryTypeId", instituteFilterDto.getInstituteTypeId());
		List<Object[]> rows = query.list();
		List<Institute> instituteList = new ArrayList<>();
		Institute obj = null;
		for (Object[] row : rows) {
			obj = new Institute();
			obj.setId(row[0].toString());
			obj.setName(row[1].toString());
			if (row[2] != null) {
				obj.setCountryName(row[2].toString());
			}
			if (row[3] != null) {
				obj.setCityName(row[3].toString());
			}
			if (row[4] != null) {
				obj.setInstituteType(Arrays.asList(row[4].toString()));
			}
			if (row[5] != null) {
				obj.setDescription(row[5].toString());
			}
			/*
			 * if (row[6] != null) { System.out.println(row[6].toString()); Date createdDate
			 * = (Date) row[6]; System.out.println(createdDate); SimpleDateFormat formatter
			 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ"); String dateResult =
			 * formatter.format(createdDate); obj.setLastUpdated(dateResult);
			 * 
			 * }
			 */
			if (row[7] != null) {
				obj.setDomesticRanking((Integer) row[7]);
			}
			instituteList.add(obj);
		}
		return instituteList;
	}

	@Override
	public int findTotalCountFilterInstitute(final InstituteFilterDto instituteFilterDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select inst.id, inst.name , inst.country_name , inst.city_name, inst.institute_type,"
				+ " inst.description, inst.updated_on, inst.domestic_ranking FROM institute as inst"
				+ " left join course c  on c.institute_id=inst.id where inst.is_active = 1 and inst.deleted_on IS NULL  ";

		sqlQuery += " and ( :countryName is null or inst.country_name = :countryName )";
		sqlQuery += " and ( :cityName is null or inst.city_name = :cityName )";
		sqlQuery += " and ( :instituteId is null or inst.id = :instituteId )";
		sqlQuery += " and ( :worldRanking is null or inst.world_ranking = :worldRanking )";
		sqlQuery += " and ( :instituteCategoryTypeId is null or inst.institute_category_type_id = :instituteCategoryTypeId)";
		if (instituteFilterDto.getDatePosted() != null && !instituteFilterDto.getDatePosted().isEmpty()) {
			Date postedDate = DateUtil.convertStringDateToDate(instituteFilterDto.getDatePosted());
			Calendar c = Calendar.getInstance();
			c.setTime(postedDate);
			c.add(Calendar.DATE, 1);
			postedDate = c.getTime();
			String updatedDate = DateUtil.getStringDateFromDate(postedDate);
			sqlQuery += " and (inst.created_on >= '" + instituteFilterDto.getDatePosted() + "' and inst.created_on < '"
					+ updatedDate + "')";
		}
		Query query = session.createSQLQuery(sqlQuery).setParameter("countryName", instituteFilterDto.getCountryName())
				.setParameter("cityName", instituteFilterDto.getCityName())
				.setParameter("instituteId", instituteFilterDto.getInstituteId())
				.setParameter("worldRanking", instituteFilterDto.getWorldRanking())
				.setParameter("instituteCategoryTypeId", instituteFilterDto.getInstituteTypeId());
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public List<InstituteGetRequestDto> autoSearch(final int pageNumber, final Integer pageSize,
			final String searchKey) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select inst.id, inst.name , inst.country_name , inst.city_name, inst.institute_type, inst.description,"
				+ " inst.latitude, inst.longitude, instAdd.student_number, inst.world_ranking, inst.accreditation, inst.email, inst.phone_number, inst.website,"
				+ " inst.address, inst.avg_cost_of_living,inst.tag_line FROM institute as inst left join institute_additional_info instAdd on instAdd.institute_id=inst.id "
				+ " where  inst.deleted_on IS NULL and (inst.name like %:searchKey% or inst.description like %:searchKey% or inst.country_name like %:searchKey%"
				+ " or inst.city_name like %:searchKey% or inst.institute_type like %:searchKey%) "
				+ " ORDER BY inst.created_on DESC";
		sqlQuery = sqlQuery + " LIMIT " + pageNumber + " ," + pageSize;
		Query query = session.createSQLQuery(sqlQuery).setParameter("searchKey", searchKey);
		List<Object[]> rows = query.list();
		return getInstituteData(rows);
	}

	@Override
	public int findTotalCountForInstituteAutosearch(final String searchKey) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select inst.id, inst.name , inst.country_name , inst.city_name, inst.institute_type, inst.description, inst.updated_on FROM institute as inst  "
				+ " where inst.deleted_on IS NULL and (inst.name like %:searchKey% or inst.description like %:searchKey% or inst.country_name like %:searchKey% or inst.city_name like %:searchKey% or inst.institute_type like %:searchKey%) "
				+ " ";
		Query query = session.createSQLQuery(sqlQuery).setParameter("searchKey", searchKey);
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public InstituteCategoryType getInstituteCategoryType(final String instituteCategoryTypeId) {
		InstituteCategoryType obj = null;
		if (instituteCategoryTypeId != null) {
			Session session = sessionFactory.getCurrentSession();
			obj = session.get(InstituteCategoryType.class, instituteCategoryTypeId);
		}
		return obj;
	}

	@Override
	public List<Institute> getSecondayCampus(final String countryId, final String cityId, final String name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Institute.class);
		crit.add(Restrictions.eq("countryName", countryId));
		crit.add(Restrictions.eq("cityName", cityId));
		crit.add(Restrictions.eq("name", name));
//		crit.add(Restrictions.eq("campusType", "SECONDARY"));
		return crit.list();
	}

	@Override
	public void saveInstituteserviceDetails(final InstituteService instituteServiceDetails) {
		Session session = sessionFactory.getCurrentSession();
		session.save(instituteServiceDetails);
	}

	@Override
	public void deleteInstituteService(final String id) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("delete from InstituteService where institute_id =:instituteId")
				.setParameter("instituteId", id);
		q.executeUpdate();

	}

	@Override
	public void saveInstituteIntake(final InstituteIntake instituteIntake) {
		Session session = sessionFactory.getCurrentSession();
		session.save(instituteIntake);

	}

	@Override
	public void deleteInstituteIntakeById(final String id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("DELETE FROM institute_intake WHERE institute_id =:instituteId")
				.setParameter("instituteId", id);
		query.executeUpdate();
	}

	@Override
	public List<String> getIntakesById(@Valid final String id) {
		List<String> list = new ArrayList<>();
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(InstituteIntake.class);
		crit.createAlias("institute", "institute");
		crit.add(Restrictions.eq("institute.id", id));
		List<InstituteIntake> accreditedInstituteDetails = crit.list();
		for (InstituteIntake bean : accreditedInstituteDetails) {
			list.add(bean.getIntake());
		}
		return list;
	}

	@Override
	public List<InstituteCategoryType> getAllCategories() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(InstituteCategoryType.class);
		return crit.list();
	}

	@Override
	public List<Institute> ratingWiseInstituteListByCountry(final String countryName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Institute.class, "institute");

		crit.add(Restrictions.eq("countryName", countryName));
		crit.addOrder(Order.asc("worldRanking"));
		return crit.list();
	}

	@Override
	public List<String> getInstituteIdsBasedOnGlobalRanking(final Long startIndex, final Long pageSize) {
		Session session = sessionFactory.getCurrentSession();
		List<String> insituteIds = session
				.createNativeQuery("SELECT ID FROM INSTITUTE ORDER BY WORLD_RANKING LIMIT ?,?")
				.setParameter(1, startIndex).setParameter(2, pageSize).getResultList();
		return insituteIds;
	}

	@Override
	public List<String> getRandomInstituteByCountry(final List<String> countryIdList) {
		Session session = sessionFactory.getCurrentSession();

		String countryIds = countryIdList.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(","));
		List<String> idList = session
				.createNativeQuery("select id from institute where country_name in (?) order by Rand() LIMIT ?")
				.setParameter(1, countryIds).setParameter(2, IConstant.TOTAL_INSTITUTES_PER_PAGE).getResultList();
		return idList;
	}

	@Override
	public Map<String, Integer> getDomesticRanking(final List<String> courseIdList) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Course.class, "course");
		criteria.createAlias("institute", "institute");
		criteria.add(Restrictions.in("course.id", courseIdList));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("course.id"));
		projectionList.add(Projections.property("institute.domesticRanking"));
		criteria.setProjection(projectionList);

		List<Object[]> resultList = criteria.list();
		Map<String, Integer> courseDomesticRanking = new HashMap<>();
		for (Object[] result : resultList) {
			courseDomesticRanking.put((String) result[0], (Integer) result[1]);
		}

		return courseDomesticRanking;
	}

	@Override
	public List<InstituteResponseDto> getNearestInstituteListForAdvanceSearch(AdvanceSearchDto courseSearchDto) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT institute.id,institute.name,count(course.id),min(cai.usd_international_fee),max(cai.usd_international_fee),institute.latitude,institute.longitude,"
				+ " 6371 * ACOS(SIN(RADIANS('" + courseSearchDto.getLatitude()
				+ "')) * SIN(RADIANS(institute.latitude)) +" + " COS(RADIANS('" + courseSearchDto.getLatitude()
				+ "')) * COS(RADIANS(institute.latitude)) * COS(RADIANS(institute.longitude) -" + " RADIANS('"
				+ courseSearchDto.getLongitude() + "'))) AS distance_in_km,institute.world_ranking,"
				+ " institute.domestic_ranking,MIN(course.stars) as stars,course.currency,institute.country_name,institute.city_name,course.name as courseName,"
				+ " instAdd.student_number,institute.about_us_info,institute.website,institute.email,institute.address,"
				+ " institute.is_active, institute.institute_type,institute.tag_line"
				+ " FROM institute left join institute_additional_info instAdd  on instAdd.institute_id=institute.id "
				+ " institute inner join course on institute.id = course.institute_id"
				+ " inner join faculty f  on f.id = course.faculty_id "
				+ " left join institute_service iis  on iis.institute_id = institute.id"
				+ " LEFT JOIN course_delivery_modes cai on cai.course_id = course.id"
				+ " where institute.latitude is not null and institute.longitude is not null"
				+ " and institute.latitude!= " + courseSearchDto.getLatitude() + " and institute.longitude!= "
				+ courseSearchDto.getLongitude();

		sqlQuery = addCondition(sqlQuery, courseSearchDto);

		sqlQuery += " group by institute.id HAVING distance_in_km <= " + courseSearchDto.getInitialRadius();

		String sortingQuery = "";
		if (courseSearchDto.getSortBy() != null && !courseSearchDto.getSortBy().isEmpty()) {
			sortingQuery = addSorting(sortingQuery, courseSearchDto);
		}

		if (courseSearchDto.getPageNumber() != null && courseSearchDto.getMaxSizePerPage() != null) {
			PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage());
			sqlQuery += sortingQuery + " LIMIT "
					+ PaginationUtil.getStartIndex(courseSearchDto.getPageNumber(), courseSearchDto.getMaxSizePerPage())
					+ " ," + courseSearchDto.getMaxSizePerPage();
		} else {
			sqlQuery += sortingQuery;
		}
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<InstituteResponseDto> instituteResponseDtos = new ArrayList<>();
		for (Object[] row : rows) {
			InstituteResponseDto instituteResponseDto = new InstituteResponseDto();
			instituteResponseDto.setId((String.valueOf(row[0])));
			instituteResponseDto.setName(String.valueOf(row[1]));
			instituteResponseDto.setTotalCourses(((BigInteger) row[2]).intValue());
			instituteResponseDto.setMinPriceRange((Double) row[3]);
			instituteResponseDto.setMaxPriceRange((Double) row[4]);
			instituteResponseDto.setLatitude((Double) row[5]);
			instituteResponseDto.setLongitude((Double) row[6]);
			instituteResponseDto.setWorldRanking((Integer) row[8]);
			instituteResponseDto.setDomesticRanking((Integer) row[9]);
			instituteResponseDto.setStars(Double.parseDouble(String.valueOf(row[10])));
			instituteResponseDto.setCurrency((String) row[11]);
			instituteResponseDto.setCountryName((String) row[12]);
			instituteResponseDto.setCityName((String) row[13]);
			instituteResponseDto.setLocation((String) row[13] + ", " + (String) row[12]);
			instituteResponseDto.setTotalStudent((Integer) row[15]);
			instituteResponseDto.setAboutUs((String) row[16]);
			instituteResponseDto.setWebsite((String) row[17]);
			instituteResponseDto.setEmail((String) row[18]);
			instituteResponseDto.setAddress((String) row[19]);
			instituteResponseDto.setInstituteType((Arrays.asList((String) row[19])));
			instituteResponseDto.setTagLine((String) row[22]);
			instituteResponseDtos.add(instituteResponseDto);
		}
		return instituteResponseDtos;
	}

	@Override
	public List<String> getUserSearchInstituteRecommendation(final Integer startIndex, final Integer pageSize,
			final String searchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Institute.class);
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
	public List<InstituteResponseDto> getInstitutesByInstituteName(Integer startIndex, Integer pageSize,
			String instituteName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Institute.class).setProjection(Projections.projectionList()
				.add(Projections.groupProperty("name").as("name")).add(Projections.property("id").as("id"))
				.add(Projections.property("worldRanking").as("worldRanking"))
				.add(Projections.property("cityName").as("cityName"))
				.add(Projections.property("countryName").as("countryName"))
				.add(Projections.property("website").as("website")).add(Projections.property("aboutInfo").as("aboutUs"))
				.add(Projections.property("latitude").as("latitude"))
				.add(Projections.property("longitude").as("longitude"))
				.add(Projections.property("phoneNumber").as("phoneNumber"))
				.add(Projections.property("email").as("email")).add(Projections.property("address").as("address"))
				.add(Projections.property("domesticRanking").as("domesticRanking"))
				.add(Projections.property("readableId").as("readableId"))
				.add(Projections.property("tagLine").as("tagLine")))
				.setResultTransformer(Transformers.aliasToBean(InstituteResponseDto.class));
		if (StringUtils.isNotEmpty(instituteName)) {
			criteria.add(Restrictions.like("name", instituteName, MatchMode.ANYWHERE));
		}
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}

	@Override
	public int getDistinctInstituteCountByName(String instituteName) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder sqlQuery = new StringBuilder("select distinct i.name as instituteName from institute i ");
		if (StringUtils.isNotEmpty(instituteName)) {
			sqlQuery.append(" where name like :instituteName");
		}
		Query query = session.createSQLQuery(sqlQuery.toString());
		if (StringUtils.isNotEmpty(instituteName)) {
			query.setParameter("instituteName", "%" + instituteName + "%");
		}
		List<Object[]> rows = query.list();
		return rows.size();
	}

	@Override
	public Optional<Institute> getInstituteByInstituteId(String instituteId) {
		return instituteRepository.findById(instituteId);
	}

	public Integer increaseIntitalRadiusCount(Integer initialRadius) {
		return initialRadius + 2;
	}

	private String addCondition(String sqlQuery, final AdvanceSearchDto courseSearchDto) {
		if (null != courseSearchDto.getCountryNames() && !courseSearchDto.getCountryNames().isEmpty()) {
			sqlQuery += " and institute.country_name in ('"
					+ courseSearchDto.getCountryNames().stream().map(String::valueOf).collect(Collectors.joining(","))
					+ "')";
		}
		if (null != courseSearchDto.getCityNames() && !courseSearchDto.getCityNames().isEmpty()) {
			sqlQuery += " and institute.city_name in ('"
					+ courseSearchDto.getCityNames().stream().map(String::valueOf).collect(Collectors.joining(","))
					+ "')";
		}
		if (null != courseSearchDto.getLevelIds() && !courseSearchDto.getLevelIds().isEmpty()) {
			sqlQuery += " and course.level_id in ('"
					+ courseSearchDto.getLevelIds().stream().map(String::valueOf).collect(Collectors.joining(","))
					+ "')";
		}

		if (null != courseSearchDto.getFaculties() && !courseSearchDto.getFaculties().isEmpty()) {
			sqlQuery += " and course.faculty_id in ('"
					+ courseSearchDto.getFaculties().stream().map(String::valueOf).collect(Collectors.joining(","))
					+ "')";
		}

		if (null != courseSearchDto.getCourseKeys() && !courseSearchDto.getCourseKeys().isEmpty()) {
			sqlQuery += " and course.name in ("
					+ courseSearchDto.getCourseKeys().stream().map(addQuotes).collect(Collectors.joining(",")) + ")";
		}
		/**
		 * This is added as in advanced search names are to be passed now, so not
		 * disturbing the already existing code, this condition has been kept in place.
		 */
		else if (null != courseSearchDto.getNames() && !courseSearchDto.getNames().isEmpty()) {
			sqlQuery += " and course.name in ('"
					+ courseSearchDto.getNames().stream().map(String::valueOf).collect(Collectors.joining(","))
					+ "\"')";
		}

//		if (null != courseSearchDto.getServiceIds() && !courseSearchDto.getServiceIds().isEmpty()) {
//			sqlQuery += " and iis.service_id in ('" + courseSearchDto.getServiceIds().stream().map(String::valueOf).collect(Collectors.joining(",")) + "')";
//		}

		if (null != courseSearchDto.getMinCost() && courseSearchDto.getMinCost() >= 0) {
			sqlQuery += " and course.cost_range >= " + courseSearchDto.getMinCost();
		}

		if (null != courseSearchDto.getMaxCost() && courseSearchDto.getMaxCost() >= 0) {
			sqlQuery += " and course.cost_range <= " + courseSearchDto.getMaxCost();
		}

		if (null != courseSearchDto.getMinDuration() && courseSearchDto.getMinDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) >= " + courseSearchDto.getMinDuration();
		}

		if (null != courseSearchDto.getMaxDuration() && courseSearchDto.getMaxDuration() >= 0) {
			sqlQuery += " and cast(cai.duration as DECIMAL(9,2)) <= " + courseSearchDto.getMaxDuration();
		}

		if (null != courseSearchDto.getInstituteId()) {
			sqlQuery += " and institute.id ='" + courseSearchDto.getInstituteId() + "'";
		}

		if (courseSearchDto.getSearchKeyword() != null) {
			sqlQuery += " and ( institute.name like '%" + courseSearchDto.getSearchKeyword().trim() + "%'";
			sqlQuery += " or institute.country_name like '%" + courseSearchDto.getSearchKeyword().trim() + "%'";
			sqlQuery += " or course.name like '%" + courseSearchDto.getSearchKeyword().trim() + "%' )";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getStudyModes()) && courseSearchDto.getStudyModes() != null) {
			sqlQuery += " and cai.study_mode in ("
					+ courseSearchDto.getStudyModes().stream().map(addQuotes).collect(Collectors.joining(",")) + ")";
		}

		if (!CollectionUtils.isEmpty(courseSearchDto.getDeliveryMethods())
				&& courseSearchDto.getDeliveryMethods() != null) {
			sqlQuery += " and cai.delivery_type in ("
					+ courseSearchDto.getDeliveryMethods().stream().map(addQuotes).collect(Collectors.joining(","))
					+ ")";
		}

		/**
		 * This filter is added to get domestic courses from user country and
		 * international courses from other countries, the courses with availbilty ='A'
		 * will be shown to all users and with availbilty='N' will be shown to no one.
		 *
		 */
		if (null != courseSearchDto.getUserCountryName()) {
			sqlQuery += " and ((institute.country_name ='" + courseSearchDto.getUserCountryName()
					+ "' and course.availbilty = 'D') OR (institute.country_name <>'"
					+ courseSearchDto.getUserCountryName()
					+ "' and course.availbilty = 'I') OR course.availbilty = 'A')";
		}
		return sqlQuery;
	}

	private String addSorting(String sortingQuery, final AdvanceSearchDto courseSearchDto) {
		String sortTypeValue = "ASC";
		if (!courseSearchDto.isSortAsscending()) {
			sortTypeValue = "DESC";
		}
		sortingQuery = sortingQuery + " ORDER BY distance_in_km ";
		if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.NAME.toString())) {
			sortingQuery = sortingQuery + " , course.name " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.DURATION.toString())) {
			sortingQuery = sortingQuery + " , course.duration " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.RECOGNITION.toString())) {
			sortingQuery = sortingQuery + " , course.recognition " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.DOMESTIC_PRICE.toString())) {
			sortingQuery = sortingQuery + " , course.domestic_fee " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.INTERNATION_PRICE.toString())) {
			sortingQuery = sortingQuery + " , course.international_fee " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.CREATED_DATE.toString())) {
			sortingQuery = sortingQuery + " , course.created_on " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.LOCATION.toString())) {
			sortingQuery = sortingQuery + " , institute.country_name " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase(CourseSortBy.PRICE.toString())) {
			sortingQuery = sortingQuery + " , IF(course.currency='" + courseSearchDto.getCurrencyCode()
					+ "', cai.usd_domestic_fee, cai.usd_international_fee) " + sortTypeValue + " ";
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase("instituteName")) {
			sortingQuery = sortingQuery + " , institute.name " + sortTypeValue.toLowerCase();
		} else if (courseSearchDto.getSortBy().equalsIgnoreCase("countryName")) {
			sortingQuery = sortingQuery + " , institute.country_name " + sortTypeValue.toLowerCase();
		}
		return sortingQuery;
	}

	@Override
	public List<InstituteResponseDto> getNearestInstituteList(Integer pageNumber, Integer pageSize, Double latitutde,
			Double longitude, Integer initialRadius) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT institute.id,institute.name,count(course.id),min(cai.usd_international_fee),max(cai.usd_international_fee),institute.latitude,institute.longitude,"
				+ " 6371 * ACOS(SIN(RADIANS('" + latitutde + "')) * SIN(RADIANS(institute.latitude)) +"
				+ " COS(RADIANS('" + latitutde
				+ "')) * COS(RADIANS(institute.latitude)) * COS(RADIANS(institute.longitude) -" + " RADIANS('"
				+ longitude
				+ "'))) AS distance_in_km,institute.world_ranking,institute.domestic_ranking,MIN(course.stars) as stars,course.currency,institute.country_name,institute.city_name,"
				+ " instAdd.student_number,institute.about_us_info,institute.website,institute.email,institute.address,"
				+ " institute.is_active, institute.institute_type,institute.tag_line"
				+ " FROM institute institute left join institute_additional_info instAdd  on instAdd.institute_id=institute.id  inner join course on institute.id = course.institute_id LEFT JOIN course_delivery_modes cai on cai.course_id = course.id"
				+ " where institute.latitude is not null and institute.longitude is not null"
				+ " and institute.latitude!= " + latitutde + " and institute.longitude!= " + longitude
				+ " group by institute.id" + " HAVING distance_in_km <= " + initialRadius
				+ " ORDER BY distance_in_km ASC LIMIT " + pageNumber + "," + pageSize;
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<InstituteResponseDto> instituteResponseDtos = new ArrayList<>();
		for (Object[] row : rows) {
			InstituteResponseDto instituteResponseDto = new InstituteResponseDto();
			instituteResponseDto.setId((String.valueOf(row[0])));
			instituteResponseDto.setName(String.valueOf(row[1]));
			instituteResponseDto.setTotalCourses(((BigInteger) row[2]).intValue());
			instituteResponseDto.setMinPriceRange((Double) row[3]);
			instituteResponseDto.setMaxPriceRange((Double) row[4]);
			instituteResponseDto.setLatitude((Double) row[5]);
			instituteResponseDto.setLongitude((Double) row[6]);
			instituteResponseDto.setWorldRanking((Integer) row[8]);
			instituteResponseDto.setDomesticRanking((Integer) row[9]);
			instituteResponseDto.setStars(Double.parseDouble(String.valueOf(row[10])));
			instituteResponseDto.setCurrency((String) row[11]);
			instituteResponseDto.setCountryName((String) row[12]);
			instituteResponseDto.setCityName((String) row[13]);
			instituteResponseDto.setLocation((String) row[13] + ", " + (String) row[12]);
			instituteResponseDto.setTotalStudent((Integer) row[14]);
			instituteResponseDto.setAboutUs((String) row[15]);
			instituteResponseDto.setWebsite((String) row[16]);
			instituteResponseDto.setEmail((String) row[17]);
			instituteResponseDto.setAddress((String) row[18]);
			instituteResponseDto.setInstituteType(Arrays.asList((String) row[20]));
			instituteResponseDto.setTagLine((String) row[21]);
			instituteResponseDtos.add(instituteResponseDto);
		}
		return instituteResponseDtos;
	}

	@Override
	public Integer getTotalCountOfNearestInstitutes(Double latitude, Double longitude, Integer initialRadius) {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT institute.id," + " 6371 * ACOS(SIN(RADIANS('" + latitude
				+ "')) * SIN(RADIANS(institute.latitude)) + COS(RADIANS('" + latitude
				+ "')) * COS(RADIANS(institute.latitude)) *" + " COS(RADIANS(institute.longitude) - RADIANS('"
				+ longitude + "'))) AS distance_in_km FROM institute institute inner join course on"
				+ " institute.id = course.institute_id where institute.latitude is not null"
				+ " and institute.longitude is not null and institute.latitude!= " + latitude
				+ " and institute.longitude!= " + longitude + " group by institute.id HAVING distance_in_km <= "
				+ initialRadius;
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		Integer totalCount = rows.size();
		return totalCount;
	}

	@Override
	public List<InstituteCategoryType> addInstituteCategoryTypes(List<InstituteCategoryType> instituteCategoryTypes) {
		Session session = sessionFactory.getCurrentSession();
		instituteCategoryTypes.stream().forEach(type -> session.save(type));
		return instituteCategoryTypes;
	}

	public List<Institute> getInstituteCampuses(String instituteId, String instituteName) throws NotFoundException {
		log.debug("inside dao.getInstitutCampuses method.");
		return instituteRepository.findByIdNotAndNameAndIsDeletedFalse(instituteId, instituteName);
	}

	public List<InstituteFacultyDto> getInstituteFaculties(String instituteId) throws NotFoundException {
		log.debug("inside dao.getInstituteFaculties method.");
		return instituteRepository.findFacultyWithCourseCountById(instituteId);
	}

	@Override
	public List<InstituteResponseDto> findByIds(List<String> instituteIds) {
		return instituteRepository.findByIdIn(instituteIds);
	}

	@Override
	public List<Institute> findAllById(List<String> instituteIds) {
		return instituteRepository.findAllById(instituteIds);
	}


	@Override
	public List<Institute> findByReadableIdIn(List<String> readableIds) {
		return instituteRepository.findByReadableIdIn(readableIds);
	}

	@Override
	public Institute findByReadableId(String readableId) {
		return instituteRepository.findByReadableId(readableId);
	}
	
	@Override
	public Map<String, String> getAllInstituteMap() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select id, `name`, city_name , country_name from institute");
		List instituteList = query.getResultList();
		Iterator it = instituteList.iterator();
		Map<String, String> instituteListMap = new HashMap<>();

		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			instituteListMap.put(String.valueOf(obj[1]) + "~" + String.valueOf(obj[2]) + "~" + String.valueOf(obj[3]),
					obj[0].toString());
		}

		return instituteListMap;
	}

	@Override
	public Institute getInstitute(String instituteId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Institute.class, "institute");
		criteria.add(Restrictions.eq("institute.id", instituteId));
		return (Institute) criteria.uniqueResult();
	}

	@Override
	public List<Institute> saveAll(List<Institute> institutesFromDb) {
		log.info("Class InstituteDaoImple method saveAll institutesFromDb : {}", institutesFromDb);
		return instituteRepository.saveAll(institutesFromDb);
	}
	
}
