package com.azguards.app.dao;

import com.azguards.app.bean.CoursePayment;
import com.azguards.common.lib.exception.ValidationException;

public interface CoursePaymentDao {

	CoursePayment save(CoursePayment coursePayment) throws ValidationException;

	void delete(CoursePayment coursePayment);
}
