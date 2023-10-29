package com.azguards.app.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserReviewResultDto implements Serializable {

	private static final long serialVersionUID = 1298861732976808001L;

	private String id;
	private String userId;
	private String entityId;
	private String entityType;
	private String studentType;
	private String studentCategory;
	private Boolean isAnonymous;
	private Double reviewStar;
	private String comments;
	private Boolean isActive;
	private Date createdOn;
	private String userName;
//	private List<UserReviewRatingDto> ratings;
}
