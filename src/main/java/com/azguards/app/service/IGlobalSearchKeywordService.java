package com.azguards.app.service;

import java.util.List;

public interface IGlobalSearchKeywordService {

	void addGlobalSearhcKeyForUser(String searchKeyword, String userId);
	
	List<String> getOtherUsersTopSearchedKeywords(String userId);
}
