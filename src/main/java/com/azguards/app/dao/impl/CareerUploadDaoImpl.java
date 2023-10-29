package com.azguards.app.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobLevel;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingActivity;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.Careers;
import com.azguards.app.bean.RelatedCareer;
import com.azguards.app.dao.CareerUploadDao;

@Component
@SuppressWarnings({"deprecation", "unchecked"})
public class CareerUploadDaoImpl implements CareerUploadDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveCareerList(Careers careerList) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerList);
	}

	@Override
	public void saveRelatedCareers(RelatedCareer relatedCareer) {
		Session session = sessionFactory.getCurrentSession();
		session.save(relatedCareer);
	}

	@Override
	public void saveJobsData(CareerJob careerJobs) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobs);
	}

	@Override
	public void saveJobsCourseSearchKeyword(CareerJobCourseSearchKeyword careerJobCourseSearchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobCourseSearchKeyword);
	}

	@Override
	public void saveJobsLevel(CareerJobLevel careerJobLevel) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobLevel);
	}

	@Override
	public void saveJobsSkill(CareerJobSkill careerJobSkill) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobSkill);
	}

	@Override
	public void saveJobSubject(CareerJobSubject careerJobSubject) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobSubject);
	}

	@Override
	public void saveJobType(CareerJobType careerJobType) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobType);
	}

	@Override
	public void saveJobWorkingActivity(CareerJobWorkingActivity careerJobWorkingActivity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobWorkingActivity);
	}

	@Override
	public void saveJobWorkingStyle(CareerJobWorkingStyle careerJobWorkingStyle) {
		Session session = sessionFactory.getCurrentSession();
		session.save(careerJobWorkingStyle);		
	}

	@Override
	public Careers getCareer(String careerName) {
		Session session = sessionFactory.getCurrentSession();
		Careers career = null;
		Criteria criteria = session.createCriteria(Careers.class);
		criteria.add(Restrictions.eq("career", careerName));
		List<Careers> careersFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careersFromDB)) {
			career = careersFromDB.get(0);
		}
		return career;
	}

	@Override
	public RelatedCareer getRelatedCareer(String careerId, String relatedCareerName) {
		Session session = sessionFactory.getCurrentSession();
		RelatedCareer relatedCareer = null;
		Criteria criteria = session.createCriteria(RelatedCareer.class, "relatedCareer");
		criteria.add(Restrictions.eq("relatedCareer.relatedCareer", relatedCareerName));
		criteria.createAlias("relatedCareer.careers", "careerList");
		criteria.add(Restrictions.eq("careerList.id", careerId));
		List<RelatedCareer> relatedCareerFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(relatedCareerFromDB)) {
			relatedCareer = relatedCareerFromDB.get(0);
		}
		return relatedCareer;
	}

	@Override
	public CareerJob getJob(String job) {
		Session session = sessionFactory.getCurrentSession();
		CareerJob careerJob = null;
		Criteria criteria = session.createCriteria(CareerJob.class);
		criteria.add(Restrictions.eq("job", job));
		List<CareerJob> careerJobsFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobsFromDB)) {
			careerJob = careerJobsFromDB.get(0);
		}
		return careerJob;
	}

	@Override
	public CareerJobCourseSearchKeyword getJobCourseSearchKeyword(String jobId, String courseSearchKeyword) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobCourseSearchKeyword careerJobCourseSearchKeyword = null;
		Criteria criteria = session.createCriteria(CareerJobCourseSearchKeyword.class, "careerJobCourseSearchKeyword");
		criteria.add(Restrictions.eq("careerJobCourseSearchKeyword.courseSearchKeyword", courseSearchKeyword));
		criteria.createAlias("careerJobCourseSearchKeyword.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobCourseSearchKeyword> careerJobCourseSearchKeywordFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobCourseSearchKeywordFromDB)) {
			careerJobCourseSearchKeyword = careerJobCourseSearchKeywordFromDB.get(0);
		}
		return careerJobCourseSearchKeyword;
	}

	@Override
	public CareerJobLevel getJobLevel(String jobId, String levelId) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobLevel careerJobLevel = null;
		Criteria criteria = session.createCriteria(CareerJobLevel.class, "careerJobLevel");
		criteria.createAlias("careerJobLevel.careerJobs", "careerJobs");
		criteria.createAlias("careerJobLevel.level", "level");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		criteria.add(Restrictions.eq("level.id", levelId));
		List<CareerJobLevel> careerJobLevelFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobLevelFromDB)) {
			careerJobLevel = careerJobLevelFromDB.get(0);
		}
		return careerJobLevel;
	}

	@Override
	public CareerJobSkill getJobSkill(String jobId, String skillName) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobSkill careerJobSkill = null;
		Criteria criteria = session.createCriteria(CareerJobSkill.class, "careerJobSkill");
		criteria.add(Restrictions.eq("careerJobSkill.skill", skillName));
		criteria.createAlias("careerJobSkill.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobSkill> careerJobSkillFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobSkillFromDB)) {
			careerJobSkill = careerJobSkillFromDB.get(0);
		}
		return careerJobSkill;
	}

	@Override
	public CareerJobSubject getJobSubject(String jobId, String subjectName) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobSubject careerJobSubject = null;
		Criteria criteria = session.createCriteria(CareerJobSubject.class, "careerJobSubject");
		criteria.add(Restrictions.eq("careerJobSubject.subject", subjectName));
		criteria.createAlias("careerJobSubject.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobSubject> careerJobSubjectFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobSubjectFromDB)) {
			careerJobSubject = careerJobSubjectFromDB.get(0);
		}
		return careerJobSubject;
	}

	@Override
	public CareerJobType getJobType(String jobId, String jobType) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobType careerJobType = null;
		Criteria criteria = session.createCriteria(CareerJobType.class, "careerJobType");
		criteria.add(Restrictions.eq("careerJobType.jobType", jobType));
		criteria.createAlias("careerJobType.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobType> careerJobTypeFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobTypeFromDB)) {
			careerJobType = careerJobTypeFromDB.get(0);
		}
		return careerJobType;
	}

	@Override
	public CareerJobWorkingActivity getJobWorkingActivity(String jobId, String workingActivity) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobWorkingActivity careerJobWorkingActivity = null;
		Criteria criteria = session.createCriteria(CareerJobWorkingActivity.class, "careerJobWorkingActivity");
		criteria.add(Restrictions.eq("careerJobWorkingActivity.workActivities", workingActivity));
		criteria.createAlias("careerJobWorkingActivity.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobWorkingActivity> careerJobWorkingActivityFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobWorkingActivityFromDB)) {
			careerJobWorkingActivity = careerJobWorkingActivityFromDB.get(0);
		}
		return careerJobWorkingActivity;
	}

	@Override
	public CareerJobWorkingStyle getJobWorkingStyle(String jobId, String workingStyle) {
		Session session = sessionFactory.getCurrentSession();
		CareerJobWorkingStyle careerJobWorkingStyle = null;
		Criteria criteria = session.createCriteria(CareerJobWorkingStyle.class, "careerJobWorkingStyle");
		criteria.add(Restrictions.eq("careerJobWorkingStyle.workStyle", workingStyle));
		criteria.createAlias("careerJobWorkingStyle.careerJobs", "careerJobs");
		criteria.add(Restrictions.eq("careerJobs.id", jobId));
		List<CareerJobWorkingStyle> careerJobWorkingStyleFromDB = criteria.list();
		if (!CollectionUtils.isEmpty(careerJobWorkingStyleFromDB)) {
			careerJobWorkingStyle = careerJobWorkingStyleFromDB.get(0);
		}
		return careerJobWorkingStyle;
	}
}
