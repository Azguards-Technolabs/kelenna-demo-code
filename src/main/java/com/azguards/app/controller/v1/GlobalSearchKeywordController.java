package com.azguards.app.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.service.IGlobalSearchKeywordService;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.local.config.MessageTranslator;


@RestController("globalSearchKeywordControllerV1")
@RequestMapping("/api/v1/globalSearch/keyword")
public class GlobalSearchKeywordController {

	@Autowired
	private IGlobalSearchKeywordService iGlobalSearchKeywordService;
	@Autowired
	private MessageTranslator messageTranslator;
	@PutMapping("/add/{searchKeyWord}")
	public ResponseEntity<?> addKeyword(@RequestHeader("userId") String userId, @PathVariable(name="searchKeyWord") String searchKeyword){
		if(searchKeyword != null) {
			iGlobalSearchKeywordService.addGlobalSearhcKeyForUser(searchKeyword, userId);
		}	
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("global_search_keyword.added")).setStatus(HttpStatus.OK).setData(null).create();
	}
	
	@GetMapping("/getTopSearched")
	public ResponseEntity<?> getOtherUsersTopSearchedKeywords(@RequestHeader("userId") String userId){
		List<String> globalKeywordList = iGlobalSearchKeywordService.getOtherUsersTopSearchedKeywords(userId);
		return new GenericResponseHandlers.Builder().setMessage(messageTranslator.toLocale("global_search_keyword.list.displayed")).setStatus(HttpStatus.OK).setData(globalKeywordList).create();
	}
}
