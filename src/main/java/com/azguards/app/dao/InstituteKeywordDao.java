package com.azguards.app.dao;import java.util.List;

import com.azguards.app.bean.InstituteKeywords;

public interface InstituteKeywordDao {
	
	public void save(InstituteKeywords obj);
	
	public void update(InstituteKeywords obj);

	public List<InstituteKeywords> getAll();
	
}
