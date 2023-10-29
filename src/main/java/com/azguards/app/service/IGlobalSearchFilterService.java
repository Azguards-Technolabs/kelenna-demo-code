package com.azguards.app.service;

import java.util.Map;

import com.azguards.app.dto.GlobalFilterSearchDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;

public interface IGlobalSearchFilterService {

	Map<String,Object> filterByEntity(GlobalFilterSearchDto globalSearchFilterDto) throws ValidationException, NotFoundException, InvokeException;
}
