package com.azguards.app.dao.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CoursePayment;
import com.azguards.app.dao.CoursePaymentDao;
import com.azguards.app.repository.CoursePaymentRepository;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CoursePaymentDaoImpl implements CoursePaymentDao {

	@Autowired
	private CoursePaymentRepository coursePaymentRepository;

	@Autowired
	private MessageTranslator messageTranslator;
	
	@Override
	public CoursePayment save(CoursePayment coursePayment) throws ValidationException {
		try {
			return coursePaymentRepository.save(coursePayment);
		} catch (DataIntegrityViolationException e) {
			log.error(messageTranslator.toLocale("course-payment.already.name_exist",Locale.US));
			throw new ValidationException(messageTranslator.toLocale("course-payment.already.name_exist"));
		}
	}

	@Override
	public void delete(CoursePayment coursePayment) {
		coursePaymentRepository.delete(coursePayment);
	}
}
