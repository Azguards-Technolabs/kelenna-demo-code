package com.azguards.app.dao.impl;import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.InstituteKeywords;
import com.azguards.app.dao.InstituteKeywordDao;

@Repository
public class InstituteKeywordDaoImpl implements InstituteKeywordDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public void save(InstituteKeywords obj) {
		Session session = sessionFactory.getCurrentSession();		
		session.save(obj);	   					
	}
	
	@Override
	public void update(InstituteKeywords obj) {	
		Session session = sessionFactory.getCurrentSession();		
		session.update(obj);	   					
	}
	
	@Override
	public List<InstituteKeywords> getAll() {
		Session session = sessionFactory.getCurrentSession();		
		Criteria crit = session.createCriteria(InstituteKeywords.class);
		return crit.list();
	}
}
