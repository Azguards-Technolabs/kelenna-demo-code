package com.azguards.app.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.Top10Course;

@Repository
public class Top10CourseDAO implements ITop10CourseDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(final Top10Course top10Course) {
		Session session = sessionFactory.getCurrentSession();
		session.save(top10Course);
	}

	@Override
	public void deleteAll() {
		Session session = sessionFactory.getCurrentSession();
		session.createSQLQuery("delete from top_10_courses").executeUpdate();
	}

	@Override
	public List<String> getAllDistinctFaculty() {
		Session session = sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(Top10Course.class, "top10Course");
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.groupProperty("top10Course.faculty"));
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.setProjection(projList);
		return crit.list();
	}

	@Override
	public List<Top10Course> getTop10CourseKeyword(final String faculty) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Top10Course.class, "top10Course");
		crit.add(Restrictions.eq("faculty", faculty).ignoreCase());
		return crit.list();
	}

	@Override
	public List<String> getCourseIdsOfTop10CoursesFromEveryFaculty() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Top10Course.class, "top10Course");
		crit.setProjection(Projections.property("course"));
		List<String> courseNames = crit.list();

		Criteria criteria = session.createCriteria(Course.class);
		criteria.add(Restrictions.in("name", courseNames));
		criteria.setProjection(Projections.property("id"));
		return criteria.list();
	}

	@Override
	public List<Course> getRandomCourseFromTop10Course(final String countryName, final List<String> levelList, final List<String> top10CourseIds) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class, "course");
		crit.createAlias("faculty", "faculty");
		crit.createAlias("institute", "institute");
		crit.createAlias("level", "level");
		crit.add(Restrictions.in("level.name", levelList));
		crit.add(Restrictions.in("course.id", top10CourseIds));
		crit.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		crit.setMaxResults(2);
		return crit.list();

	}
}
