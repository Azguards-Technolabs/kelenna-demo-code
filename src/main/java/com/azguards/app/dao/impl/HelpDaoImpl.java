package com.azguards.app.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Help;
import com.azguards.app.bean.HelpAnswer;
import com.azguards.app.bean.HelpCategory;
import com.azguards.app.bean.HelpSubCategory;
import com.azguards.app.dao.HelpDao;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.local.config.MessageTranslator;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class HelpDaoImpl implements HelpDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public void save(final Help help) {
		Session session = sessionFactory.getCurrentSession();
		session.save(help);
	}
	
	@Override
	public HelpCategory getHelpCategory(final String id) {
		Session session = sessionFactory.getCurrentSession();
		HelpCategory obj = session.get(HelpCategory.class, id);
		return obj;
	}

	@Override
	public HelpSubCategory getHelpSubCategory(final String id) {
		Session session = sessionFactory.getCurrentSession();
		HelpSubCategory obj = session.get(HelpSubCategory.class, id);
		return obj;
	}

	@Override
	public Help get(final String id) {
		Session session = sessionFactory.getCurrentSession();
		Help obj = session.get(Help.class, id);
		return obj;
	}

	@Override
	public void update(final Help help) {
		Session session = sessionFactory.getCurrentSession();
		session.update(help);
	}

	@Override
	public int findTotalHelpRecord(final String userId, final Boolean isArchive) {
		int status = 1;
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select count(*) from help sa where sa.is_active = " + status + " and sa.deleted_on IS NULL";
		if (userId != null) {
			sqlQuery = sqlQuery + " and sa.user_id = '" + userId + "'";
		}
		if (isArchive != null) {
			sqlQuery = sqlQuery + " and sa.is_archive = " + isArchive;
		}
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		BigInteger count = (BigInteger) query.uniqueResult();
		return Integer.parseInt(String.valueOf(count));
	}

	@Override
	public List<Help> getAll(final Integer startIndex, final Integer pageSize, final String userId, final Boolean isArchive) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Help.class, "help");
		if (userId != null) {
			crit.add(Restrictions.eq("help.userId", userId));
		}
		crit.add(Restrictions.eq("help.isActive", true));
		if (isArchive != null) {
			crit.add(Restrictions.eq("help.isArchive", isArchive));
		}

		if ((startIndex != null) && (pageSize != null)) {
			crit.setFirstResult(startIndex);
			crit.setMaxResults(pageSize);
		}
		crit.addOrder(Order.desc("help.isFavourite"));
		crit.addOrder(Order.desc("help.createdOn"));
		return crit.list();

	}

	@Override
	public void save(final HelpCategory helpCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.save(helpCategory);
	}

	@Override
	public void save(final HelpSubCategory helpSubCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.save(helpSubCategory);
	}

	@Override
	public List<HelpSubCategory> getSubCategoryByCategory(final String categoryId, final Integer startIndex, final Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(HelpSubCategory.class, "helpSubCategory");
		crit.createAlias("helpSubCategory.helpCategory", "helpCategory");
		crit.add(Restrictions.eq("helpCategory.id", categoryId));
		crit.add(Restrictions.eq("helpCategory.isActive", true));
		if ((startIndex != null) && (pageSize != null)) {
			crit.setFirstResult(startIndex);
			crit.setMaxResults(pageSize);
		}
		return crit.list();

	}

	@Override
	public List<Help> getHelpByCategory(final String categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Help.class, "help");
		crit.createAlias("help.category", "category");
		return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("isActive", true)).list();
	}

	@Override
	public Integer findTotalHelpRecordBySubCategory(final String sub_category_id) {
		int status = 1;
		Integer helpCount = null;
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "select count(*) from help sa where sa.is_active = " + status + " and sa.deleted_on IS NULL and sub_category_id='"
				+ sub_category_id + "'";
		System.out.println(sqlQuery);
		Query query = session.createSQLQuery(sqlQuery);
		BigInteger count = (BigInteger) query.uniqueResult();
		if (count != null) {
			helpCount = count.intValue();
		}
		return helpCount;
	}

	@Override
	public List<HelpSubCategory> getAllHelpSubCategories() {
		Session session = sessionFactory.getCurrentSession();
		List<HelpSubCategory> list = session.createCriteria(HelpSubCategory.class).list();
		return list;
	}

	@Override
	public HelpAnswer save(final HelpAnswer helpAnswer) {
		Session session = sessionFactory.getCurrentSession();
		session.save(helpAnswer);
		return helpAnswer;
	}

	@Override
	public List<HelpAnswer> getAnswerByHelpId(final String userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(HelpAnswer.class, "helpAnswer");
		crit.createAlias("helpAnswer.help", "help");
		return crit.add(Restrictions.eq("help.id", userId)).list();
	}

	@Override
	public List<HelpCategory> getAllCategory(final Integer startIndex, final Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(HelpCategory.class, "helpCategory");
		crit.add(Restrictions.eq("helpCategory.isActive", true));
		if ((startIndex != null) && (pageSize != null)) {
			crit.setFirstResult(startIndex);
			crit.setMaxResults(pageSize);
		}
		return crit.list();
	}

	@Override
	public List<Help> findByStatus(final String status, final String categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Help.class, "help");
		return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("status", status)).add(Restrictions.eq("isActive", true)).list();
	}

	@Override
	public List<Help> findByMostRecent(final String mostRecent, final String categoryId) {
		if ((mostRecent != null) && mostRecent.equals("asc")) {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Help.class, "help");
			return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("isActive", true)).addOrder(Order.asc("createdOn")).list();
		} else {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Help.class, "help");
			return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("isActive", true)).addOrder(Order.desc("createdOn")).list();
		}
	}

	@Override
	public List<Help> findByStatusAndMostRecent(final String status, final String mostRecent, final String categoryId) {
		if ((mostRecent != null) && mostRecent.equals("asc")) {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Help.class, "help");
			return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("status", status)).add(Restrictions.eq("isActive", true))
					.addOrder(Order.asc("createdOn")).list();
		} else {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Help.class, "help");
			return crit.add(Restrictions.eq("category.id", categoryId)).add(Restrictions.eq("status", status)).add(Restrictions.eq("isActive", true))
					.addOrder(Order.desc("createdOn")).list();
		}
	}

	@Override
	public void updateAnwser(final HelpAnswer helpAnswer) {
		Session session = sessionFactory.getCurrentSession();
		session.update(helpAnswer);
	}

	@Override
	public void setIsFavouriteFlag(final String id, final boolean isFavourite) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "update help set is_favourite = ? where id = ?";
		int updateCount = session.createNativeQuery(sqlQuery).setParameter(1, isFavourite).setParameter(2, id).executeUpdate();
		if (updateCount == 0) {
			throw new NotFoundException(messageTranslator.toLocale("help.not_found.id") + id);
		}

	}

	@Override
	public int getCategoryCount() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(HelpCategory.class, "helpCategory");
		crit.add(Restrictions.eq("helpCategory.isActive", true));
		crit.setProjection(Projections.rowCount());
		return ((Long) crit.uniqueResult()).intValue();
	}

	@Override
	public int getSubCategoryCount(final String categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(HelpSubCategory.class, "helpSubCategory");
		crit.createAlias("helpSubCategory.helpCategory", "helpCategory");
		crit.add(Restrictions.eq("helpSubCategory.isActive", true));
		crit.add(Restrictions.eq("helpCategory.isActive", true));
		crit.add(Restrictions.eq("helpCategory.id", categoryId));
		crit.setProjection(Projections.rowCount());
		return ((Long) crit.uniqueResult()).intValue();
	}

	@Override
	public List<String> getRelatedSearchQuestions(final List<String> searchKeywords) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Help.class, "help");
		Disjunction disjunction = Restrictions.disjunction();
		for (String string : searchKeywords) {
			Criterion keywordCriteria = Restrictions.ilike("help.descritpion", string, MatchMode.ANYWHERE);
			disjunction.add(keywordCriteria);
		}
		crit.add(disjunction);
		crit.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		crit.setMaxResults(3);
		crit.setProjection(Projections.property("help.title"));
		return crit.list();
	}


}
