package com.azguards.app.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.EducationSystem;
import com.azguards.app.bean.Subject;
import com.azguards.app.dao.EducationSystemDao;
import com.azguards.app.repository.EducationSystemRepository;
import com.azguards.common.lib.dto.institute.EducationSystemDto;
import com.azguards.common.lib.dto.institute.GradeDto;
import com.azguards.common.lib.dto.institute.SubjectDto;
import com.azguards.common.lib.enumeration.GradeType;

@Component
@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
public class EducationSystemDaoImpl implements EducationSystemDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EducationSystemRepository educationSystemRepository;

	@Override
	public void save(final EducationSystem hobbiesObj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(hobbiesObj);
	}

	public void saveAll(List<EducationSystem> educationSystems) {
		educationSystemRepository.saveAll(educationSystems);
	}
	
	@Override
	public void update(final EducationSystem educationSystem) {
		Session session = sessionFactory.getCurrentSession();
		session.update(educationSystem);
	}

	@Transactional
	@Override
	public EducationSystem get(final String id) {
		Session session = sessionFactory.getCurrentSession();
		EducationSystem obj = session.get(EducationSystem.class, id);
		return obj;
	}

	@Override
	public List<EducationSystem> getAll() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(EducationSystem.class);
		return crit.list();
	}

	@Override
	public List<EducationSystem> getAllGlobeEducationSystems() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
				"select id,name,code,country_id,is_active from education_system  where country_id is null " + "and is_active = 1 order by code");
		List<Object[]> rows = query.list();
		List<EducationSystem> list = new ArrayList<>();
		for (Object[] row : rows) {
			EducationSystem obj = new EducationSystem();
			obj.setId(row[0].toString());
			obj.setName(row[1].toString());
			obj.setCode(row[2].toString());
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<EducationSystem> getEducationSystemsByCountryName(final String countryName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(EducationSystem.class, "educationSystem");
		criteria.add(Restrictions.eq("educationSystem.countryName", countryName));
		return criteria.list();
	}

	@Override
	public List<Subject> getSubject() {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Subject.class);
		return crit.list();
	}

	@Override
	public List<Subject> getSubjectByEducationSystem(final String educationSystemId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Subject.class);
		crit.add(Restrictions.eq("educationSystemId", educationSystemId));
		return crit.list();
	}

	@Override
	public List<EducationSystemDto> getEducationSystemByCountryNameAndStateName(String countryName, String stateName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
				"select es.id,es.country_name,es.name,es.code,es.description,es.state_name,es.grade_type from education_system es where es.country_name='"+countryName+"'and( es.state_name='"+stateName+"' OR es.state_name = 'All' )");
		List<Object[]> rows = query.list();
		List<EducationSystemDto> list = new ArrayList<>();
		List<SubjectDto> subjects = new ArrayList<>();
		List<GradeDto> gradeDetails = new ArrayList<>();
		for (Object[] row : rows) {
			EducationSystemDto obj = new EducationSystemDto();
			obj.setId(row[0].toString());
			obj.setCountryName(row[1].toString());
			obj.setName(row[2].toString());
			obj.setCode(row[3].toString());
			obj.setDescription(row[4].toString());
			obj.setStateName(row[5].toString());
			obj.setGradeType(GradeType.valueOf(row[6].toString()));
			
			Query subjectQuery = session.createSQLQuery("select id,subject_name from subject where education_system_id ='"+row[0].toString()+"'");
			List<Object[]> subjectRows = subjectQuery.list();
			for (Object[] subjectRow : subjectRows) {
				SubjectDto subject = new SubjectDto();
				subject.setId(subjectRow[0].toString());
				subject.setSubjectName(subjectRow[1].toString());
				subjects.add(subject);
				obj.setSubjects(subjects);
			}
			
			Query gradeQuery = session.createSQLQuery("SELECT DISTINCT id,country_name,grade,MIN(gpa_grade) FROM grade_details where education_system_id ='"+row[0].toString()+"' GROUP BY grade;");
			List<Object[]> gradeRows = gradeQuery.list();
			for(Object[] gradeRow : gradeRows) {
				GradeDto grade = new GradeDto();
				grade.setId(gradeRow[0].toString());
				grade.setCountryName(gradeRow[1].toString());
				if(gradeRow[2] != null) {
					grade.setGrade(gradeRow[2].toString());
				}
				if(gradeRow[3].toString() != null) {
					grade.setGpaGrade(gradeRow[3].toString());
				}
				gradeDetails.add(grade);
				obj.setGradeDtos(gradeDetails);
			}
			list.add(obj);
		}
		return list;
	}

	@Override
	public EducationSystem findByNameAndCountryNameAndStateName(String name, String countryName, String stateName) {
		return educationSystemRepository.findByNameAndCountryNameAndStateName(name, countryName, stateName);
	}

}
