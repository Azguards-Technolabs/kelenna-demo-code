package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.RelatedCareer;
import com.azguards.app.dto.JobIdProjection;

public interface CareerTestDao {

	public Page<CareerJobSkill> getCareerJobSkills(String levelId, String jobId, Pageable pageable);
	
	
	public Page<CareerJobWorkingStyle> getCareerJobWorkingStyle(List<String> jobIds, Pageable pageable);
	
	public Page<CareerJobSubject> getCareerJobSubject(List<String> jobIds, Pageable pageable);
	
	public Page<CareerJobType> getCareerJobType(List<String> jobIds, Pageable pageable);

	public List<JobIdProjection> getCareerJobIdsByJobTypeId(String jobTypeId);
		
	public Page<CareerJob> getCareerJob(List<String> jobIds, Pageable pageable);
	
	public Page<CareerJob> getCareerJobByName(String name, Pageable pageable);
	
	public Page<RelatedCareer> getRelatedCareers(List<String> carrerIds, Pageable pageable);
	
	public List<CareerJobCourseSearchKeyword> getCareerJobCourseSearchKeyword(List<String> jobIds);
	
	public Optional<CareerJob> getCareerJob(String careerJobId); 
	
	public Page<CareerJobSkill> getJobSkills(List<String> jobNames, Pageable pageable);
}
