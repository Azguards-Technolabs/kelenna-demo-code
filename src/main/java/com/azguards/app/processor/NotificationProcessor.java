package com.azguards.app.processor;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.common.lib.dto.notification.ContentTemplateDataDto;
import com.azguards.common.lib.dto.notification.EmailAddressDto;
import com.azguards.common.lib.dto.notification.NotificationMetaDataDto;
import com.azguards.common.lib.dto.notification.NotificationTemplateDataDto;
import com.azguards.common.lib.dto.transaction.ViewTransactionDto;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.handler.NotificationHandler;

import lombok.extern.slf4j.Slf4j;

@Service("notificationProcessor")
@Slf4j
public class NotificationProcessor {
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	public void notifyCourseChanged(Course course, List<ViewTransactionDto> favoriteTransactions,String notificationTemplateType) {
		log.info("Inside method NotificationProcessor.notifyUserMentioned");	
		if(!CollectionUtils.isEmpty(favoriteTransactions)) {
			favoriteTransactions.stream().forEach(transaction -> {
				try {
					log.info("Send notification if user liked a post");
					NotificationTemplateDataDto notificationDto = new NotificationTemplateDataDto();
					notificationDto.setNotificationTemplateType(notificationTemplateType);
					if(!ObjectUtils.isEmpty(transaction.getUser())) {
						notificationDto.setUserId(transaction.getUser().getUserId());
						
						notificationDto.setEmailDetails(new EmailAddressDto(transaction.getUser().getFirstName(),transaction.getUser().getEmail(),null,null));
						
						NotificationMetaDataDto notificationMetaDataDto = new NotificationMetaDataDto();
						notificationMetaDataDto.setTargetEntityId(course.getId());
								
						notificationMetaDataDto.setContentTemplateData(Arrays.asList(new ContentTemplateDataDto(course.getId(),course.getName(),"COURSE")));
						
						notificationDto.setNotificationMetadata(notificationMetaDataDto);
						notificationDto.setSenderUserId(course.getUpdatedBy());
						log.info("Calling notification service to send push notification");
						notificationHandler.sendPushNotification(notificationDto);						
					}
				} catch (InvokeException se) {
					log.error("Unable to send notifications to mentioned users ",se);
				}
			});
		}
	}	
}
