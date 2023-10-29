package com.azguards.app.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.AuditErrorReport;
import com.azguards.app.bean.ErrorReport;
import com.azguards.app.bean.ErrorReportCategory;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.util.DateUtil;

@Repository
@SuppressWarnings({ "deprecation", "unchecked" })
public class ErrorReportDAO implements IErrorReportDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveErrorReportCategory(final ErrorReportCategory errorReportCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.save(errorReportCategory);
	}

	@Override
	public void save(final ErrorReport errorReport) {
		Session session = sessionFactory.getCurrentSession();
		session.save(errorReport);
	}

	@Override
	public ErrorReportCategory getErrorCategory(final String errorReportCategoryId) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(ErrorReportCategory.class, errorReportCategoryId);
	}

	@Override
	public List<ErrorReport> getAllErrorReport(final String userId, final Integer startIndex, final Integer pageSize,
			final String errorReportCategoryId, final String errorReportStatus, final Date updatedOn, final Boolean isFavourite, final Boolean isArchive,
			final String sortByField, String sortByType, final String searchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(ErrorReport.class, "errorReport");
		crit.createAlias("errorReport.errorReportCategory", "errorReportCategory");
		if (userId != null) {
			crit.add(Restrictions.eq("errorReport.userId", userId));
		}
		crit.add(Restrictions.eq("errorReport.isActive", true));
		if (errorReportCategoryId != null) {
			crit.add(Restrictions.eq("errorReportCategory.id", errorReportCategoryId));
		}
		if (errorReportStatus != null) {
			crit.add(Restrictions.eq("errorReport.status", errorReportStatus));
		}
		if (isFavourite != null) {
			crit.add(Restrictions.eq("errorReport.isFavourite", isFavourite));
		}
		if (isArchive != null) {
			crit.add(Restrictions.eq("errorReport.isArchive", isArchive));
		}
		if (updatedOn != null) {
			Date fromDate = DateUtil.removeTimeFromDate(updatedOn);
			Date toDate = DateUtil.removeTimeFromDate(updatedOn);
			crit.add(Restrictions.ge("errorReport.updatedOn", fromDate));
			crit.add(Restrictions.le("errorReport.updatedOn", toDate));
		}
		if (searchKeyword != null) {
			crit.add(Restrictions.disjunction().add(Restrictions.ilike("errorReport.description", searchKeyword, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("errorReport.phoneName", searchKeyword, MatchMode.ANYWHERE)));
		}
		if (sortByType == null) {
			sortByType = "DESC";
		}
		if (sortByField != null) {
			if ("description".equals(sortByField)) {
				if ("ASC".equals(sortByType)) {
					crit.addOrder(Order.asc("errorReport.description"));
				} else if ("DESC".equals(sortByType)) {
					crit.addOrder(Order.desc("errorReport.description"));
				}
			} else if ("phoneName".equals(sortByField)) {
				if ("ASC".equals(sortByType)) {
					crit.addOrder(Order.asc("errorReport.phoneName"));
				} else if ("DESC".equals(sortByType)) {
					crit.addOrder(Order.desc("errorReport.phoneName"));
				}
			}
		} else {
			if ("ASC".equals(sortByType)) {
				crit.addOrder(Order.asc("errorReport.id"));
			} else if ("DESC".equals(sortByType)) {
				crit.addOrder(Order.desc("errorReport.id"));
			}
		}
		if (startIndex != null && pageSize != null) {
			crit.setFirstResult(startIndex);
			crit.setMaxResults(pageSize);
		}
		crit.addOrder(Order.desc("errorReport.isFavourite"));
		crit.addOrder(Order.desc("errorReport.createdOn"));
		return crit.list();
	}

	@Override
	public ErrorReport getErrorReportById(final String id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(ErrorReport.class);
		crit.add(Restrictions.eq("id", id)).add(Restrictions.eq("isActive", true));
		return (ErrorReport) crit.uniqueResult();
	}

	@Override
	public void update(final ErrorReport errorReport) {
		Session session = sessionFactory.getCurrentSession();
		session.update(errorReport);
	}

	@Override
	public List<ErrorReportCategory> getAllErrorCategory(final String errorCategoryType) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(ErrorReportCategory.class);
		crit.add(Restrictions.eq("isActive", true));
		crit.add(Restrictions.eq("errorCategoryType", errorCategoryType));
		return crit.list();
	}

	@Override
	public void addErrorRepoerAudit(final AuditErrorReport auditErrorReport) {
		Session session = sessionFactory.getCurrentSession();
		session.save(auditErrorReport);
	}

	@Override
	public int getErrorReportCountForUser(final String userId, final String errorReportCategoryId, final String errorReportStatus, final Date updatedOn,
			final Boolean isFavourite, final Boolean isArchive, final String searchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(ErrorReport.class, "errorReport");
		crit.createAlias("errorReport.errorReportCategory", "errorReportCategory");
		if (userId != null) {
			crit.add(Restrictions.eq("errorReport.userId", userId));
		}
		crit.add(Restrictions.eq("errorReport.isActive", true));
		if (errorReportCategoryId != null) {
			crit.add(Restrictions.eq("errorReportCategory.id", errorReportCategoryId));
		}
		if (errorReportStatus != null) {
			crit.add(Restrictions.eq("errorReport.status", errorReportStatus));
		}
		if (isFavourite != null) {
			crit.add(Restrictions.eq("errorReport.isFavourite", isFavourite));
		}
		if (isArchive != null) {
			crit.add(Restrictions.eq("errorReport.isArchive", isArchive));
		}
		if (searchKeyword != null) {
			crit.add(Restrictions.disjunction().add(Restrictions.ilike("errorReport.description", searchKeyword, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("errorReport.phoneName", searchKeyword, MatchMode.ANYWHERE)));
		}
		if (updatedOn != null) {
			Date fromDate = DateUtil.removeTimeFromDate(updatedOn);
			Date toDate = DateUtil.removeTimeFromDate(updatedOn);
			crit.add(Restrictions.ge("errorReport.updatedOn", fromDate));
			crit.add(Restrictions.le("errorReport.updatedOn", toDate));
		}
		crit.setProjection(Projections.rowCount());
		return ((Long) crit.uniqueResult()).intValue();
	}

	@Override
	public void setIsFavouriteFlag(final String errorReportId, final boolean isFavourite) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "update error_report set is_favourite = ? where id = ?";
		int updateCount = session.createNativeQuery(sqlQuery).setParameter(1, isFavourite).setParameter(2, errorReportId).executeUpdate();
		if (updateCount == 0) {
			throw new NotFoundException("No Error Report Found with Id : " + errorReportId);
		}
	}

	@Override
	public List<AuditErrorReport> getAuditListByErrorReport(final String errorReportId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AuditErrorReport.class, "auditErrorReport");
		criteria.createAlias("auditErrorReport.errorReport", "errorReport");
		criteria.add(Restrictions.eq("errorReport.id", errorReportId));
		return criteria.list();
	}

}
