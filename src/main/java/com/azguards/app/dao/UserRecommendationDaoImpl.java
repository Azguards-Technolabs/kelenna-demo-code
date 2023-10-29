package com.azguards.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.azguards.app.bean.Course;

@Repository
public class UserRecommendationDaoImpl implements UserRecommendationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Course> getRecommendCourse(final String facultyId, final String instituteId, final String countryId, final String cityId,
			final Double price, final Double variablePrice, final int pageSize, final List<String> courseIds) {
		return getRelatedCourse(facultyId, instituteId, countryId, cityId, price, variablePrice, pageSize, courseIds, null);
	}

	@Override
	public List<Course> getRelatedCourse(final String facultyId, final String instituteId, final String countryId, final String cityId,
			final Double price, final Double variablePrice, final int pageSize, final List<String> courseIds, final String courseName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.createAlias("course.faculty", "faculty");

		crit.createAlias("course.institute", "institute");
		crit.add(Restrictions.eq("faculty.id", facultyId));
		if (instituteId != null) {
			crit.add(Restrictions.eq("institute.id", instituteId));
		}

		if (price != null) {
			Double low = price - variablePrice;
			if (low < 0) {
				low = Double.valueOf(0);
			}
			Double high = price + variablePrice;
			crit.add(Restrictions.between("course.usdInternationFee", low, high));
			crit.addOrder(Order.asc("course.usdInternationFee"));
		}
		if (courseIds != null && !courseIds.isEmpty()) {
			crit.add(Restrictions.not(Restrictions.in("course.id", courseIds.toArray())));
		}

		if (courseName != null) {
			crit.add(Restrictions.eq("course.name", courseName));
		}

		crit.setFirstResult(0);
		crit.setMaxResults(pageSize);

		return crit.list();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Course> getCourseNoResultRecommendation(final String facultyId, final String countryId, final List<String> courseIds,
			final Integer startIndex, final Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.createAlias("course.faculty", "faculty");
		if (facultyId != null) {
			crit.add(Restrictions.eq("faculty.id", facultyId));
		}

		if (courseIds != null && !courseIds.isEmpty()) {
			crit.add(Restrictions.not(Restrictions.in("course.id", courseIds.toArray())));
		}
		if (startIndex != null && pageSize != null) {
			crit.setFirstResult(startIndex);
			crit.setMaxResults(pageSize);
		}
		return crit.list();
	}

	@Override
	public List<Course> getCheapestCourse(final String facultyId, final String countryId, final String levelId, final String cityId,
			final List<String> courseIds, final Integer startIndex, final Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		
		String sqlQuery = "select c.id, c.name, c.world_ranking, c.stars, c.availabilty, c.currency, c.website, c.recognition, c.description"
				+ " from course c left join course_delivery_modes cai on cai.course_id = c.id";
		
		if(!StringUtils.isEmpty(facultyId)) {
			sqlQuery+= " where c.faculty_id = '"+ facultyId +"'";
		}
		
		if(!StringUtils.isEmpty(levelId)) {
			sqlQuery+= " and c.level_id = '"+ levelId +"'";
		}
		
		if(!CollectionUtils.isEmpty(courseIds)) {
			sqlQuery += " and c.id not in ("+ courseIds.stream().map(String::valueOf).collect(Collectors.joining("','", "'", "'")) + ")";
		}
		sqlQuery += " order by cai.usd_international_fee desc limit " + startIndex + ", " + pageSize;
		
		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<Course> courses = new ArrayList<>();
		for (Object[] row : rows) {
			Course course = new Course();
			course.setId(row[0].toString());
			course.setName(row[1].toString());
			course.setWorldRanking(Integer.parseInt(row[2].toString()));
			course.setStars(Integer.parseInt(row[3].toString()));
			course.setAvailabilty(row[4].toString());
			course.setCurrency(row[5].toString());
			course.setWebsite(row[6].toString());
			course.setRecognition(row[7].toString());
			course.setDescription(row[8].toString());
			courses.add(course);
		}
		return courses;
	}
}
