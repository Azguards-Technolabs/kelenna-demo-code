package com.azguards.app.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.GradeDetails;
import com.azguards.app.dao.GradeDao;
import com.azguards.app.repository.GradeDetailRepository;

@Component
public class GradeDaoImpl implements GradeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private GradeDetailRepository gradeDetailRepository;
	
	@Override
	public String getGradeDetails(String countryId, String educationSystemId, String grade) {
		String gpaGrade = "0.0";
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(GradeDetails.class);
		crit.add(Restrictions.eq("countryName", countryId)).add(Restrictions.eq("educationSystem.id", educationSystemId)).add(Restrictions.eq("grade", grade));
		List<GradeDetails> details = crit.list();
		System.out.println("The List: " + details.size());
		ArrayList<GradeDetails> min = new ArrayList<>();
		for (GradeDetails x : details) {
			if (min.size() == 0 || Double.valueOf(x.getGpaGrade()) == Double.valueOf(min.get(0).getGpaGrade())) {
				min.add(x);
			} else if (Double.valueOf(x.getGpaGrade()) < Double.valueOf(min.get(0).getGpaGrade())) {
				min.clear();
				min.add(x);
			}
		}
		if (min != null && !min.isEmpty()) {
			gpaGrade = min.get(0).getGpaGrade();
		}
		return gpaGrade;
	}

	@Override
	public List<GradeDetails> getGrades(String countryId, String systemId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(GradeDetails.class);
		crit.add(Restrictions.eq("countryName", countryId));
		crit.add(Restrictions.eq("educationSystemId", systemId));
		return crit.list();
	}

	@Override
	public void saveAll(List<GradeDetails> grades) {
		gradeDetailRepository.saveAll(grades);
	}

	@Override
	public GradeDetails findByCountryNameAndStateNameAndGradeAndEducationSystem(String countryName,
			String stateName, String grade, EducationSystem educationSystem) {
		return gradeDetailRepository.findByCountryNameAndStateNameAndGradeAndEducationSystem(countryName,
				stateName, grade, educationSystem);
	}
}
