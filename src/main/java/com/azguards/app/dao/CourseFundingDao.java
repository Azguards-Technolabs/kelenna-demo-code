package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CourseFunding;
import com.azguards.common.lib.exception.ValidationException;

public interface CourseFundingDao {
	public List<CourseFunding> saveAll(List<CourseFunding> courseFundings) throws ValidationException;
}
