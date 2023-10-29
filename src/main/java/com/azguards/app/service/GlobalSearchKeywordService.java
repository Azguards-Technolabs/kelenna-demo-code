package com.azguards.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.GlobalSearchKeyword;
import com.azguards.app.dao.IGlobalSearchKeywordDAO;

@Service
@Transactional(rollbackFor = Throwable.class)
public class GlobalSearchKeywordService implements IGlobalSearchKeywordService {

	@Autowired
	private IGlobalSearchKeywordDAO iGlobalSearchKeyWordDao;
	
	@Override
	public void addGlobalSearhcKeyForUser(String searchKeyword, String userId) {
		
		GlobalSearchKeyword globalSearchKeyword = new GlobalSearchKeyword();
		globalSearchKeyword.setUserId(userId);
		globalSearchKeyword.setUpdatedBy(userId);
		globalSearchKeyword.setCreatedBy(userId);
		globalSearchKeyword.setSearchKeyword(searchKeyword);
		globalSearchKeyword.setCreatedOn(new java.util.Date(System.currentTimeMillis()));
		globalSearchKeyword.setUpdatedOn(new java.util.Date(System.currentTimeMillis()));
		iGlobalSearchKeyWordDao.save(globalSearchKeyword);
	}

	@Override
	public List<String> getOtherUsersTopSearchedKeywords(String userId) {
		// TODO Auto-generated method stub
		List<String> topKeyWordList = iGlobalSearchKeyWordDao.getOtherUsersTopSearchedKeywords(userId);
		return topKeyWordList;
	}

}
