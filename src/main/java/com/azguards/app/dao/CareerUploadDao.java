package com.azguards.app.dao;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobLevel;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingActivity;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.Careers;
import com.azguards.app.bean.RelatedCareer;

public interface CareerUploadDao {

	public void saveCareerList(Careers careerList);
	
	public void saveRelatedCareers(RelatedCareer relatedCareer);
	
	public void saveJobsData(CareerJob careerJobs);
	
	public void saveJobsCourseSearchKeyword(CareerJobCourseSearchKeyword careerJobCourseSearchKeyword);
	
	public void saveJobsLevel(CareerJobLevel careerJobLevel);
	
	public void saveJobsSkill(CareerJobSkill careerJobSkill);
	
	public void saveJobSubject(CareerJobSubject careerJobSubject);
	
	public void saveJobType(CareerJobType careerJobType);
	
	public void saveJobWorkingActivity(CareerJobWorkingActivity careerJobWorkingActivity);
	
	public void saveJobWorkingStyle(CareerJobWorkingStyle careerJobWorkingStyle);
	
	public Careers getCareer(String career);
	
	public RelatedCareer getRelatedCareer(String careerId, String relatedCareer);
	
	public CareerJob getJob(String job);
	
	public CareerJobCourseSearchKeyword getJobCourseSearchKeyword(String jobId, String courseSearchKeyword);
	
	public CareerJobLevel getJobLevel(String jobId, String levelId);
	
	public CareerJobSkill getJobSkill(String jobId, String skillName);
	
	public CareerJobSubject getJobSubject(String jobId, String subjectName);
	
	public CareerJobType getJobType(String jobId, String jobType);
	
	public CareerJobWorkingActivity getJobWorkingActivity(String jobId, String workingActivity);
	
	public CareerJobWorkingStyle getJobWorkingStyle(String jobId, String workingStyle);
}
