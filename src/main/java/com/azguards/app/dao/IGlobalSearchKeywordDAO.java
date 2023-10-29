package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.GlobalSearchKeyword;

public interface IGlobalSearchKeywordDAO {

	void save(GlobalSearchKeyword globalSearchKeyword);
	
	List<String> getOtherUsersTopSearchedKeywords(String userId);
}
