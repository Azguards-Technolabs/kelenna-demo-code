package com.azguards.app.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.Faculty;
import com.azguards.app.dao.FacultyDao;

@Component
@SuppressWarnings({ "unchecked", "deprecation" })
public class FacultyDaoImpl implements FacultyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdateFaculty(final Faculty obj) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(obj);
	}

	@Override
	public Faculty get(final String id) {
		Session session = sessionFactory.getCurrentSession();
		Faculty obj = session.get(Faculty.class, id);
		return obj;
	}

	@Override
	public List<Faculty> getAll() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Faculty.class);
		return crit.list();
	}

	@Override
	public List<Faculty> getFacultyListByFacultyNames(final List<String> facultyNameList) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Faculty.class, "faculty");
		crit.add(Restrictions.in("name", facultyNameList));
		return crit.list();
	}

	@Override
	public Faculty getFacultyByFacultyName(String facultyName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Faculty.class, "faculty");
		crit.add(Restrictions.in("name", facultyName));
		Faculty faculty = (Faculty) crit.uniqueResult();
		return faculty;
	}

	@Override
	public Map<String, String> getFacultyNameIdMap() {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Faculty.class);
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.property("id"));
        projList.add(Projections.property("name"));
        criteria.setProjection(projList);
        List facultyList = criteria.list();
		Iterator it=facultyList.iterator();
		Map<String, String> facultyListMap = new HashMap<>();
		
		while(it.hasNext()) {
			Object[] obj = (Object[])it.next();
			facultyListMap.put(String.valueOf(obj[1]), obj[0].toString());
		}
		
		return facultyListMap;
	}
	
	@Override
	public Faculty getFaculty(String id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Faculty.class, "faculty");
		criteria.add(Restrictions.eq("faculty.id", id));
		return (Faculty) criteria.uniqueResult();
	}
}
