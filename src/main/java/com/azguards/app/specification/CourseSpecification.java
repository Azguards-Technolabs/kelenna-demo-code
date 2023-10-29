package com.azguards.app.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseLanguage;
import com.azguards.app.bean.Institute;
import com.yuzee.common.lib.enumeration.SortingOnEnum;
import com.yuzee.common.lib.enumeration.SortingTypeEnum;

public class CourseSpecification {
	private CourseSpecification() {}
	
	public static Specification < Course > findCourseByFilters(Boolean isNotDeleted, Boolean isActive, SortingOnEnum sortingOnEnum, SortingTypeEnum sortingTypeEnum,
			 String instituteId, List<String> languages, Integer minRanking, Integer maxRanking, String countryName, String searchKey) {

		return new Specification < Course > () {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root < Course > root, CriteriaQuery < ? > query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);
				List < Predicate > predicates = new ArrayList < > ();
				
				if (!StringUtils.isEmpty(searchKey)) {
					
					Join<Course, Institute> courseInstituteJoin = root.join("institute");
					Predicate institutePredicate = criteriaBuilder.or(
							criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + searchKey.toLowerCase() + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(courseInstituteJoin.get("name")),"%" + searchKey.toLowerCase() + "%"));
					predicates.add(institutePredicate);
				}

				if (!ObjectUtils.isEmpty(isActive)) {
					predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
				}
				
				if (!ObjectUtils.isEmpty(isNotDeleted)) {
					if(isNotDeleted) {
						predicates.add(root.get("deletedOn").isNull());

					}else {
						predicates.add(root.get("deletedOn").isNotNull());
					}
				}
				
				if (!StringUtils.isEmpty(countryName)) {
					Join<Course, Institute> courseInstituteJoin = root.join("institute");
					Predicate predicateInstitute = (criteriaBuilder.equal(courseInstituteJoin.get("countryName"),countryName));
					predicates.add(predicateInstitute);
				}
				
				if (!StringUtils.isEmpty(instituteId)) {
					Join<Course, Institute> courseInstituteJoin = root.join("institute");
					Predicate predicateInstitute = (criteriaBuilder.equal(courseInstituteJoin.get("id"),instituteId));
					predicates.add(predicateInstitute);
				}
				
				if (!CollectionUtils.isEmpty(languages)) {
					Join<Course, CourseLanguage> courseLanguage = root.join("courseLanguages");
					Predicate predicateCourseLanguage = (criteriaBuilder.in(courseLanguage.get("language")).value(languages));
					predicates.add(predicateCourseLanguage);
				}
				
				if (!ObjectUtils.isEmpty(minRanking)&& minRanking >= 0) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("worldRanking"), minRanking));
				}
				
				if (!ObjectUtils.isEmpty(maxRanking)&& maxRanking >= 0) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("worldRanking"), maxRanking));
				}
				
				if (!ObjectUtils.isEmpty(sortingTypeEnum) && !ObjectUtils.isEmpty(sortingOnEnum)) {
					if(sortingTypeEnum.name().equals(SortingTypeEnum.ASC.name())){
						query.orderBy(criteriaBuilder.asc(root.get(sortingOnEnum.getDisplayName())));
					}
					if(sortingTypeEnum.name().equals(SortingTypeEnum.DESC.name())){
						query.orderBy(criteriaBuilder.desc(root.get(sortingOnEnum.getDisplayName())));
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
