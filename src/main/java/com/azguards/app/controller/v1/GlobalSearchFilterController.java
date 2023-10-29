package com.azguards.app.controller.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.GlobalFilterSearchDto;
import com.azguards.app.service.IGlobalSearchFilterService;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.GenericResponseHandlers;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

@RestController("globalSearchFilterControllerV1")
@RequestMapping("/api/v1/globalSearch")
public class GlobalSearchFilterController {

	@Autowired
	private MessageTranslator messageTranslator;
	@Autowired
	private IGlobalSearchFilterService globalSearchFilterService;
	
	@PostMapping(value = "/filter")
	public ResponseEntity<?> filterEntityBasedOnParameters(@RequestBody GlobalFilterSearchDto globalFilterSearchDto) 
			throws ValidationException, NotFoundException, InvokeException{
		Map<String,Object> responseEntityMap = globalSearchFilterService.filterByEntity(globalFilterSearchDto);
		if(globalFilterSearchDto == null || globalFilterSearchDto.getIds() == null || globalFilterSearchDto.getIds().isEmpty()) {
			throw new ValidationException(messageTranslator.toLocale("global-search.course_fitered"));
		}
		List<?> responseList = (List<?>)responseEntityMap.get("entity");
		Integer count = (Integer)responseEntityMap.get("count");
		PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(PaginationUtil.getStartIndex(globalFilterSearchDto.getPageNumber(), globalFilterSearchDto.getMaxSizePerPage()), globalFilterSearchDto.getMaxSizePerPage(), count.intValue());
		Map<String, Object> responseMap = new HashMap<>(4);
		responseMap.put("status", HttpStatus.OK);
		responseMap.put("message", "Course List Display Successfully");
		responseMap.put("data", responseList);
		responseMap.put("totalCount", count);
		responseMap.put("pageNumber", paginationUtilDto.getPageNumber());
		responseMap.put("hasPreviousPage", paginationUtilDto.isHasPreviousPage());
		responseMap.put("hasNextPage", paginationUtilDto.isHasNextPage());
		responseMap.put("totalPages", paginationUtilDto.getTotalPages());
		
		return new GenericResponseHandlers.Builder().setData(responseMap).setMessage(messageTranslator.toLocale("global_search_filter.list.retrieved")).setStatus(HttpStatus.OK).create();
	}
}
