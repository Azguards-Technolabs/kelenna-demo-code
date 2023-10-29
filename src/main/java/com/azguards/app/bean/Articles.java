package com.azguards.app.bean;

import java.io.Serializable;
import java.math.BigInteger;

// Generated 7 Jun, 2019 2:45:49 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.azguards.app.dto.CategoryDto;
import com.azguards.app.dto.SubCategoryDto;

/**
 * SeekaArticles generated by hbm2java
 */
@Entity
@Table(name = "articles")
public class Articles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7910522322611149651L;
	private String id;
	private Category category;
	private SubCategory subcategory;
	private String userId;
	private String addType;
	private String heading;
	private String content;
	private String url;
	private String imagepath;
	private BigInteger type;
	private Boolean active;
	private Date deletedOn;
	private BigInteger shared;
	private BigInteger reviewed;
	private BigInteger likes;
	private String link;
	private Date createdAt;
	private Date updatedAt;
	private String countryName;
	private String cityName;
	private Faculty faculty;
	private Institute institute;
	private Course course;
	private String articleType;
	private String companyName;
	private String companyWebsite;
	private SubCategoryDto subCategoryDropDownDto;
	private CategoryDto categoryobj;
	private String author;

	@Temporal(TemporalType.TIMESTAMP)
	private Date postDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expireDate;
	private Boolean enabled;
	private Boolean published;
	private Boolean featured;
	private String notes;
	private String websiteUrl;
	private Boolean seekaRecommended;
	private String tags;
	private String status;

	public Articles() {
	}

	public Articles(final Category category, final SubCategory subcategory, final String userInfo, final String addType, final String heading,
			final String content, final String url, final String imagepath, final BigInteger type, final Boolean active, final Date deletedOn,
			final Date createdAt, final BigInteger shared, final BigInteger reviewed, final BigInteger likes, final String link, final Date updatedAt,
			final Faculty faculty, final Institute institute, final Course courses, final String gender,
			final String articleType, final String companyName, final String companyWebsite) {
		this.category = category;
		this.subcategory = subcategory;
		this.userId = userInfo;
		this.addType = addType;
		this.heading = heading;
		this.content = content;
		this.url = url;
		this.imagepath = imagepath;
		this.type = type;
		this.active = active;
		this.deletedOn = deletedOn;
		this.createdAt = createdAt;
		this.shared = shared;
		this.reviewed = reviewed;
		this.likes = likes;
		this.link = link;
		this.updatedAt = updatedAt;
		this.faculty = faculty;
		this.institute = institute;
		this.course = courses;
		this.articleType = articleType;
		this.companyName = companyName;
		this.companyWebsite = companyWebsite;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcategory_id")
	public SubCategory getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(final SubCategory subcategory) {
		this.subcategory = subcategory;
	}

	@Column(name = "user_id", length=36)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(final String userInfo) {
		this.userId = userInfo;
	}

	@Column(name = "add_type", length = 100)
	public String getAddType() {
		return this.addType;
	}

	public void setAddType(final String addType) {
		this.addType = addType;
	}

	@Column(name = "heading", length = 200)
	public String getHeading() {
		return this.heading;
	}

	public void setHeading(final String heading) {
		this.heading = heading;
	}

	@Column(name = "content", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Column(name = "url", length = 500)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Column(name = "imagepath", length = 500)
	public String getImagepath() {
		return this.imagepath;
	}

	public void setImagepath(final String imagepath) {
		this.imagepath = imagepath;
	}

	@Column(name = "type")
	public BigInteger getType() {
		return this.type;
	}

	public void setType(final BigInteger type) {
		this.type = type;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_on", length = 19)
	public Date getDeletedOn() {
		return this.deletedOn;
	}

	public void setDeletedOn(final Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(final Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "shared")
	public BigInteger getShared() {
		return this.shared;
	}

	public void setShared(final BigInteger shared) {
		this.shared = shared;
	}

	@Column(name = "reviewed")
	public BigInteger getReviewed() {
		return this.reviewed;
	}

	public void setReviewed(final BigInteger reviewed) {
		this.reviewed = reviewed;
	}

	@Column(name = "likes")
	public BigInteger getLikes() {
		return this.likes;
	}

	public void setLikes(final BigInteger likes) {
		this.likes = likes;
	}

	@Column(name = "link")
	public String getLink() {
		return this.link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(final Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "faculty")
	public Faculty getFaculty() {
		return this.faculty;
	}

	public void setFaculty(final Faculty faculty) {
		this.faculty = faculty;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institute")
	public Institute getInstitute() {
		return this.institute;
	}

	public void setInstitute(final Institute institute) {
		this.institute = institute;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "courses")
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(final Course courses) {
		this.course = courses;
	}

	@Column(name = "article_type", length = 100)
	public String getArticleType() {
		return this.articleType;
	}

	public void setArticleType(final String articleType) {
		this.articleType = articleType;
	}

	@Column(name = "company_name", length = 100)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "company_website", length = 500)
	public String getCompanyWebsite() {
		return this.companyWebsite;
	}

	public void setCompanyWebsite(final String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	/**
	 * @return the subCategoryDropDownDto
	 */
	@Transient
	public SubCategoryDto getSubCategoryDropDownDto() {
		return subCategoryDropDownDto;
	}

	/**
	 * @param subCategoryDropDownDto the subCategoryDropDownDto to set
	 */
	public void setSubCategoryDropDownDto(final SubCategoryDto subCategoryDropDownDto) {
		this.subCategoryDropDownDto = subCategoryDropDownDto;
	}

	/**
	 * @return the categoryobj
	 */
	@Transient
	public CategoryDto getCategoryobj() {
		return categoryobj;
	}

	/**
	 * @param categoryobj the categoryobj to set
	 */
	public void setCategoryobj(final CategoryDto categoryobj) {
		this.categoryobj = categoryobj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active == null ? 0 : active.hashCode());
		result = prime * result + (addType == null ? 0 : addType.hashCode());
		result = prime * result + (articleType == null ? 0 : articleType.hashCode());
		result = prime * result + (category == null ? 0 : category.hashCode());
		result = prime * result + (categoryobj == null ? 0 : categoryobj.hashCode());
		result = prime * result + (getCityName() == null ? 0 : getCityName().hashCode());
		result = prime * result + (companyName == null ? 0 : companyName.hashCode());
		result = prime * result + (companyWebsite == null ? 0 : companyWebsite.hashCode());
		result = prime * result + (content == null ? 0 : content.hashCode());
		result = prime * result + (getCountryName() == null ? 0 : getCountryName().hashCode());
		result = prime * result + (course == null ? 0 : course.hashCode());
		result = prime * result + (createdAt == null ? 0 : createdAt.hashCode());
		result = prime * result + (deletedOn == null ? 0 : deletedOn.hashCode());
		result = prime * result + (faculty == null ? 0 : faculty.hashCode());
		result = prime * result + (heading == null ? 0 : heading.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (imagepath == null ? 0 : imagepath.hashCode());
		result = prime * result + (institute == null ? 0 : institute.hashCode());
		result = prime * result + (likes == null ? 0 : likes.hashCode());
		result = prime * result + (link == null ? 0 : link.hashCode());
		result = prime * result + (reviewed == null ? 0 : reviewed.hashCode());
		result = prime * result + (shared == null ? 0 : shared.hashCode());
		result = prime * result + (subCategoryDropDownDto == null ? 0 : subCategoryDropDownDto.hashCode());
		result = prime * result + (subcategory == null ? 0 : subcategory.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		result = prime * result + (updatedAt == null ? 0 : updatedAt.hashCode());
		result = prime * result + (url == null ? 0 : url.hashCode());
		result = prime * result + (userId == null ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Articles other = (Articles) obj;
		if (active == null) {
			if (other.active != null) {
				return false;
			}
		} else if (!active.equals(other.active)) {
			return false;
		}
		if (addType == null) {
			if (other.addType != null) {
				return false;
			}
		} else if (!addType.equals(other.addType)) {
			return false;
		}
		if (articleType == null) {
			if (other.articleType != null) {
				return false;
			}
		} else if (!articleType.equals(other.articleType)) {
			return false;
		}
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (categoryobj == null) {
			if (other.categoryobj != null) {
				return false;
			}
		} else if (!categoryobj.equals(other.categoryobj)) {
			return false;
		}
		if (getCityName() == null) {
			if (other.getCityName() != null) {
				return false;
			}
		} else if (!getCityName().equals(other.getCityName())) {
			return false;
		}
		if (companyName == null) {
			if (other.companyName != null) {
				return false;
			}
		} else if (!companyName.equals(other.companyName)) {
			return false;
		}
		if (companyWebsite == null) {
			if (other.companyWebsite != null) {
				return false;
			}
		} else if (!companyWebsite.equals(other.companyWebsite)) {
			return false;
		}
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (getCountryName() == null) {
			if (other.getCountryName() != null) {
				return false;
			}
		} else if (!getCountryName().equals(other.getCountryName())) {
			return false;
		}
		if (course == null) {
			if (other.course != null) {
				return false;
			}
		} else if (!course.equals(other.course)) {
			return false;
		}
		if (createdAt == null) {
			if (other.createdAt != null) {
				return false;
			}
		} else if (!createdAt.equals(other.createdAt)) {
			return false;
		}
		if (deletedOn == null) {
			if (other.deletedOn != null) {
				return false;
			}
		} else if (!deletedOn.equals(other.deletedOn)) {
			return false;
		}
		if (faculty == null) {
			if (other.faculty != null) {
				return false;
			}
		} else if (!faculty.equals(other.faculty)) {
			return false;
		}
		if (heading == null) {
			if (other.heading != null) {
				return false;
			}
		} else if (!heading.equals(other.heading)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (imagepath == null) {
			if (other.imagepath != null) {
				return false;
			}
		} else if (!imagepath.equals(other.imagepath)) {
			return false;
		}
		if (institute == null) {
			if (other.institute != null) {
				return false;
			}
		} else if (!institute.equals(other.institute)) {
			return false;
		}
		if (likes == null) {
			if (other.likes != null) {
				return false;
			}
		} else if (!likes.equals(other.likes)) {
			return false;
		}
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		if (reviewed == null) {
			if (other.reviewed != null) {
				return false;
			}
		} else if (!reviewed.equals(other.reviewed)) {
			return false;
		}
		if (shared == null) {
			if (other.shared != null) {
				return false;
			}
		} else if (!shared.equals(other.shared)) {
			return false;
		}
		if (subCategoryDropDownDto == null) {
			if (other.subCategoryDropDownDto != null) {
				return false;
			}
		} else if (!subCategoryDropDownDto.equals(other.subCategoryDropDownDto)) {
			return false;
		}
		if (subcategory == null) {
			if (other.subcategory != null) {
				return false;
			}
		} else if (!subcategory.equals(other.subcategory)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (updatedAt == null) {
			if (other.updatedAt != null) {
				return false;
			}
		} else if (!updatedAt.equals(other.updatedAt)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SeekaArticles [id=").append(id).append(", category=").append(category).append(", subcategory=").append(subcategory).append(", userId=")
				.append(userId).append(", addType=").append(addType).append(", heading=").append(heading).append(", content=").append(content).append(", url=")
				.append(url).append(", imagepath=").append(imagepath).append(", type=").append(type).append(", active=").append(active).append(", deletedOn=")
				.append(deletedOn).append(", createdAt=").append(createdAt).append(", shared=").append(shared).append(", reviewed=").append(reviewed)
				.append(", likes=").append(likes).append(", link=").append(link).append(", updatedAt=").append(updatedAt).append(", country=").append(getCountryName())
				.append(", city=").append(getCityName()).append(", faculty=").append(faculty).append(", institute=").append(institute).append(", courses=")
				.append(course).append(", articleType=").append(articleType).append(", companyName=").append(companyName).append(", companyWebsite=")
				.append(companyWebsite).append(", subCategoryDropDownDto=").append(subCategoryDropDownDto).append(", categoryobj=").append(categoryobj)
				.append(", author=").append(author).append(", postDate=").append(postDate).append(", expireyDate=").append(expireDate).append(", enabled=")
				.append(enabled).append(", published=").append(published).append(", featured=").append(featured).append(", notes=").append(notes)
				.append(", websiteUrl=").append(websiteUrl).append(", seekaRecommended=").append(seekaRecommended).append(", tags=").append(tags)
				.append(", status=").append(status).append("]");
		return builder.toString();
	}

	/**
	 * @return the author
	 */
	@Column(name = "author")
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(final String author) {
		this.author = author;
	}

	/**
	 * @return the postDate
	 */
	@Column(name = "post_date")
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * @param postDate the postDate to set
	 */
	public void setPostDate(final Date postDate) {
		this.postDate = postDate;
	}

	/**
	 * @return the expireyDate
	 */
	@Column(name = "expire_date")
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireyDate the expireyDate to set
	 */
	public void setExpireDate(final Date expireyDate) {
		this.expireDate = expireyDate;
	}

	/**
	 * @return the enabled
	 */
	@Column(name = "enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "published")
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(final Boolean published) {
		this.published = published;
	}

	/**
	 * @return the featured
	 */
	@Column(name = "featured")
	public Boolean getFeatured() {
		return featured;
	}

	/**
	 * @param featured the featured to set
	 */
	public void setFeatured(final Boolean featured) {
		this.featured = featured;
	}

	/**
	 * @return the notes
	 */
	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(final String notes) {
		this.notes = notes;
	}

	/**
	 * @return the websiteUrl
	 */
	@Column(name = "website_url", length = 100)
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	/**
	 * @param websiteUrl the websiteUrl to set
	 */
	public void setWebsiteUrl(final String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	/**
	 * @return the seekaRecommended
	 */
	@Column(name = "seeka_recommended")
	public Boolean getSeekaRecommended() {
		return seekaRecommended;
	}

	/**
	 * @param seekaRecommended the seekaRecommended to set
	 */
	public void setSeekaRecommended(final Boolean seekaRecommended) {
		this.seekaRecommended = seekaRecommended;
	}

	/**
	 * @return the tags
	 */
	@Column(name = "tags", length = 500)
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(final String tags) {
		this.tags = tags;
	}

	/**
	 * @return the status
	 */
	@Column(name = "status", length = 500)
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	@Column(name = "country_name")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "city_name")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}