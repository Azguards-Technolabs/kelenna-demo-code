package com.azguards.app.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "youtube_video", uniqueConstraints = @UniqueConstraint(columnNames = {"video_id"} , name = "UK_YOUTUBE_VIDEO_VI"),
indexes = { @Index (name = "IDX_TYPE", columnList="type", unique = false)})
@Data
public class YoutubeVideo {

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	private String youtubeVideoId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "country_code")
	private String countryCode;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "video_title")
	private String videoTitle;
	
	@Column(name = "description", columnDefinition = "LONGTEXT")
	private String description;
	
	@Column(name = "video_id", length = 50)
	private String vedioId;
	
	@Column(name = "url")
	private String url;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "upload_date")
	private String uploadDate;
}
