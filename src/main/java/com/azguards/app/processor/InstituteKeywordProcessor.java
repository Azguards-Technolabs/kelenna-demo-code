package com.azguards.app.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.InstituteKeywords;
import com.azguards.app.dao.InstituteKeywordDao;

@Service
@Transactional
public class InstituteKeywordProcessor {

	@Autowired
	private InstituteKeywordDao instituteKeywordDao;
	
	public void save(InstituteKeywords obj) {
		instituteKeywordDao.save(obj);
	}
	
	public void update(InstituteKeywords obj) {
		instituteKeywordDao.update(obj);
	}
	
	public List<InstituteKeywords> getAll(){
		return instituteKeywordDao.getAll();
	}
	
	/*@Override
	public List<InstituteKeywords> searchCourseKeyword(String keyword){
		return dao.searchCourseKeyword(keyword);
	}*/

}
