package com.azguards.app.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.Level;
import com.azguards.app.dao.LevelDao;
import com.azguards.app.repository.LevelRepository;
import com.azguards.common.lib.dto.institute.LevelDto;

@Component
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class LevelDaoImpl implements LevelDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private LevelRepository levelRepository;

    @Override
    public void addUpdateLevel(Level level) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(level);
    }

    @Override
    public Level getLevel(String levelId) {
        Session session = sessionFactory.getCurrentSession();
        Level obj = session.get(Level.class, levelId);
        return obj;
    }

    @Override
    public List<Level> getAll() {
        return levelRepository.findAllByOrderBySequenceNoAsc();
    }

    @Override
    public List<Level> getCourseTypeByCountryId(String countryID) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select distinct ct.id, ct.type_txt as courseType from course_type ct  inner join faculty f  on f.course_type_id = ct.id "
                        + "inner join course c  on c.faculty_id = f.id inner join institute_course ic  on ic.course_id = c.id " + "where ic.country_id = :countryId")
                        .setParameter("countryId", countryID);
        List<Object[]> rows = query.list();
        List<Level> courseTypes = new ArrayList<>();
        for (Object[] row : rows) {
            Level obj = new Level();
            obj.setId(row[0].toString());
            obj.setName(row[1].toString());
            courseTypes.add(obj);
        }
        return courseTypes;
    }

    @Override
    public List<Level> getLevelByCountryId(String countryId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select distinct le.id, le.name as name,le.code as levelkey from level le inner join institute_level il  on il.level_id = le.id "
                        + "inner join country c on c.name = il.country_name " + "where il.country_name = :countryId").setParameter("countryId", countryId);
        List<Object[]> rows = query.list();
        List<Level> level = new ArrayList<>();
        for (Object[] row : rows) {
            Level obj = new Level();
            obj.setId(row[0].toString());
            obj.setName(row[1].toString());
            obj.setCode(row[2].toString());
            level.add(obj);
        }
        return level;
    }

    @Override
    public List<Level> getAllLevelByCountry() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select distinct le.id, le.name as name,le.code as levelkey,il.country_name from level le  inner join institute_level il  on "
                        + "il.level_id = le.id inner join country c  on c.name = il.country_name");
        List<Object[]> rows = query.list();
        List<Level> level = new ArrayList<>();

        for (Object[] row : rows) {
            Level obj = new Level();
            obj.setId(row[0].toString());
            obj.setName(row[1].toString());
            if (row[2] != null) {
                obj.setCode(row[2].toString());
            }
//            obj.setCountryId(row[3].toString());
            level.add(obj);
        }
        return level;
    }

    @Override
    public List<LevelDto> getCountryLevel(String countryId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select ll.id, ll.name as name,ll.code as levelkey from level ll inner join institute_level il on il.level_id = ll.id" + 
        		" inner join institute i on i.id = il.institute_id where i.country_name =:countryId")
                        .setParameter("countryId", countryId);
        List<Object[]> rows = query.list();
        List<LevelDto> level = new ArrayList<>();
        for (Object[] row : rows) {
        	LevelDto obj = new LevelDto();
            obj.setId(row[0].toString());
            obj.setName(row[1].toString());
            obj.setCode(row[2].toString());
            level.add(obj);
        }
        return level;
    }
    
    @Override
	public List<String> getAllLevelNamesByInstituteId(final String instituteId) {
        List<String> list = new ArrayList<String>();
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select l.id, l.`name` from institute_level il left join level l on l.id = il.level_id where il.institute_id ='"+instituteId+"'");
        List<Object[]> rows = query.list();
        for (Object[] row : rows) {
            list.add(new String(row[1].toString()));
        }
        return list;
    }

	@Override
	public List<Level> findByIdIn(List<String> ids) {
		return levelRepository.findAllById(ids);
	}

	@Override
	public Level getLevelByLevelCode(String levelCode) {
		Session session = sessionFactory.getCurrentSession();
		Level level = null;
		Criteria criteria = session.createCriteria(Level.class);
		criteria.add(Restrictions.eq("code", levelCode));
		List<Level> levels = criteria.list();
		if (levels != null && !levels.isEmpty()) {
			level = levels.get(0);
		}
		return level;
	}
	
	@Override
	@Transactional
	public Map<String, String> getAllLevelMap() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Level.class, "level");
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("level.id"));
		projList.add(Projections.property("level.code"));
		criteria.setProjection(projList);
		List<Level> instituteList = criteria.list();
		Iterator it = instituteList.iterator();
		Map<String, String> levelMap = new HashMap<>();

		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			levelMap.put(String.valueOf(obj[1]), obj[0].toString());
		}
		return levelMap;
	}

	@Override
	public void addUpdateLevels(List<Level> levels) {
		levelRepository.saveAll(levels);
	}
}
