package com.azguards.app.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.azguards.app.bean.CareerJob;
import com.azguards.app.bean.CareerJobCourseSearchKeyword;
import com.azguards.app.bean.CareerJobSkill;
import com.azguards.app.bean.CareerJobSubject;
import com.azguards.app.bean.CareerJobType;
import com.azguards.app.bean.CareerJobWorkingStyle;
import com.azguards.app.bean.RelatedCareer;
import com.azguards.app.dao.CareerTestDao;
import com.azguards.app.dto.JobIdProjection;
import com.azguards.app.repository.CareerJobCourseSearchKeywordRepository;
import com.azguards.app.repository.CareerJobRepository;
import com.azguards.app.repository.CareerJobSkillRepository;
import com.azguards.app.repository.CareerJobSubjectRepository;
import com.azguards.app.repository.CareerJobTypeRepository;
import com.azguards.app.repository.CareerJobWorkingStyleRepository;
import com.azguards.app.repository.RelatedCareerRepository;

@Component
public class CareerTestDaoImpl implements CareerTestDao {

	@Autowired
	private CareerJobWorkingStyleRepository careerJobWorkingStyleRepository;

	@Autowired
	private CareerJobSubjectRepository careerJobSubjectRepository;

	@Autowired
	private CareerJobTypeRepository careerJobTypeRepository;

	@Autowired
	private CareerJobRepository careerJobRepository;

	@Autowired
	private RelatedCareerRepository relatedCareerRepository;

	@Autowired
	private CareerJobSkillRepository careerJobSkillRepository;

	@Autowired
	private CareerJobCourseSearchKeywordRepository careerJobCourseSearchKeywordRepository;

	@Override
	public Page<CareerJobSkill> getCareerJobSkills(String levelId, String jobId, Pageable pageable) {
		return careerJobSkillRepository.findByLevelIdAndJobId(levelId, jobId, pageable);
	}

	@Override
	public Page<CareerJobWorkingStyle> getCareerJobWorkingStyle(List<String> jobIds, Pageable pageable) {
		return careerJobWorkingStyleRepository.findByCareerJobsIdInOrderByWorkStyle(jobIds, pageable);
	}

	@Override
	public Page<CareerJobSubject> getCareerJobSubject(List<String> jobIds, Pageable pageable) {
		return careerJobSubjectRepository.findByCareerJobsIdInOrderBySubject(jobIds, pageable);
	}

	@Override
	public Page<CareerJobType> getCareerJobType(List<String> jobIds, Pageable pageable) {
		return careerJobTypeRepository.findByCareerJobsIdInOrderByJobType(jobIds, pageable);
	}
	
	@Override
	public List<JobIdProjection> getCareerJobIdsByJobTypeId(String jobTypeId){
		return careerJobTypeRepository.findJobIdsById(jobTypeId);
	}

	@Override
	public Page<CareerJob> getCareerJob(List<String> jobIds, Pageable pageable) {
		return careerJobRepository.findByIdIn(jobIds, pageable);
	}

	@Override
	public Page<RelatedCareer> getRelatedCareers(List<String> carrerIds, Pageable pageable) {
		return relatedCareerRepository.findByCareersIdInOrderByRelatedCareer(carrerIds, pageable);
	}

	@Override
	public List<CareerJobCourseSearchKeyword> getCareerJobCourseSearchKeyword(List<String> jobIds) {
		return careerJobCourseSearchKeywordRepository.findByCareerJobsIdInOrderByCourseSearchKeyword(jobIds);
	}

	@Override
	public Optional<CareerJob> getCareerJob(String careerJobId) {
		return careerJobRepository.findById(careerJobId);
	}

	@Override
	public Page<CareerJob> getCareerJobByName(String name, Pageable pageable) {
		return careerJobRepository.findByJobContainingIgnoreCaseOrderByJob(name, pageable);
	}
	
	@Override
	public Page<CareerJobSkill> getJobSkills(List<String> jobNames, Pageable pageable) {
		return careerJobSkillRepository.findByCareerJobs_JobIn(jobNames, pageable);
	}
}
