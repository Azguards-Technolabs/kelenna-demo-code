package com.azguards.app.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.GlobalSearchKeyword;

@Repository
public class GlobalSearchKeywordDAO implements IGlobalSearchKeywordDAO {

	 @Autowired
	 private SessionFactory sessionFactory;

	@Override
	public void save(final GlobalSearchKeyword globalSearchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		session.save(globalSearchKeyword);
	}

	@Override
	public List<String> getOtherUsersTopSearchedKeywords(String userId) {

		Session session = sessionFactory.getCurrentSession();
		List<String> keyWordList = session.createNativeQuery("Select search_keyword from global_search_keyword where user_id not in (?) group by search_keyword order by count(*) desc")
			.setParameter(1, userId).getResultList();
		return keyWordList;
	}

}
