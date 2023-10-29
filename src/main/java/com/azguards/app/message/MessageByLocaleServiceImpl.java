package com.azguards.app.message;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

	@Autowired
	private MessageSource messageSource;

	/**
	 * Get message based on Locale
	 */
	@Override
	public String getMessage(String id, Object[] arg, String language) {
		
		/**
		 * If language is passed empty call the overloaded version of method with default locale as en
		 */
		if(language == null || language.isEmpty()) {
			return getMessage(id, arg);
		}
		Locale locale = LocaleUtils.toLocale(language);
		return messageSource.getMessage(id, arg, locale);
	}
	
	/**
	 * If no locale specified take english as local and display messages.
	 */
	@Override
	public String getMessage(String id, Object[] arg) {
		
		Locale locale = LocaleUtils.toLocale("en");
		return messageSource.getMessage(id, arg, locale);
	}
	
}