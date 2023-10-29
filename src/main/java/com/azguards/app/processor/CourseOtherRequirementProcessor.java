package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.azguards.app.bean.Course;
import com.azguards.app.bean.CourseResearchProposalRequirement;
import com.azguards.app.bean.CourseVaccineRequirement;
import com.azguards.app.bean.CourseWorkExperienceRequirement;
import com.azguards.app.bean.CourseWorkPlacementRequirement;
import com.azguards.app.dao.CourseDao;
import com.azguards.app.dao.CourseVaccineRequirementDao;
import com.azguards.app.dao.CourseWorkExperienceRequirementDao;
import com.azguards.app.dao.CourseWorkPlacementRequirementDao;
import com.azguards.app.dto.CourseOtherRequirementDto;
import com.azguards.app.dto.CourseResearchProposalRequirementDto;
import com.azguards.app.dto.CourseVaccineRequirementDto;
import com.azguards.app.dto.CourseWorkExperienceRequirementDto;
import com.azguards.app.dto.CourseWorkPlacementRequirementDto;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.common.VaccinationDto;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.CommonHandler;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseOtherRequirementProcessor {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CommonProcessor commonProcessor;

	@Autowired
	private CourseVaccineRequirementDao vaccineDao;
	
	@Autowired
	private CourseWorkExperienceRequirementDao workExperienceDao;
	
	@Autowired
	private CourseWorkPlacementRequirementDao workPlacementDao;
	
	@Autowired
	private CommonHandler commonHandler;
	
	@Autowired
	MessageTranslator messageTranslator;
	
	@Transactional
	public void saveOrUpdateOtherRequirements(String userId, String courseId,
			@Valid CourseOtherRequirementDto courseOtherRequirementDto) {
		log.info("inside CourseOtherRequirementProcessor.saveOrUpdateOtherRequirements");
		Course course = courseDao.get(courseId);
		if (!ObjectUtils.isEmpty(course)) {

			CourseVaccineRequirement vaccine = course.getCourseVaccineRequirement();
			
			if (!ObjectUtils.isEmpty(courseOtherRequirementDto.getVaccine())) {
				if (ObjectUtils.isEmpty(vaccine)) {
					vaccine = new CourseVaccineRequirement();
				}
				vaccine.setAuditFields(userId);
				vaccine.setCourse(course);
				vaccine.setDescription(courseOtherRequirementDto.getVaccine().getDescription());
				if(!CollectionUtils.isEmpty(courseOtherRequirementDto.getVaccine().getVaccination())) {
					Set<String>  vaccinationIds = courseOtherRequirementDto.getVaccine().getVaccination().stream().map(vaccination -> {
						return vaccination.get_id().toString();
					}).collect(Collectors.toSet());
					PaginationResponseDto<List<VaccinationDto>> vaccinationPaggination = commonHandler.getVaccinationByFilters(1, vaccinationIds.size(), vaccinationIds);
					if(CollectionUtils.isEmpty(vaccinationPaggination.getResponse()) || 
							vaccinationPaggination.getResponse().size() != vaccinationIds.size()) {
						log.error(messageTranslator.toLocale("vaccination.ids.invalid", Locale.US));
						throw new ValidationException(messageTranslator.toLocale("vaccination.ids.invalid"));
					}
					vaccine.setVaccinationIds(vaccinationIds);
				}else if(!CollectionUtils.isEmpty(vaccine.getVaccinationIds())) {
					vaccine.getVaccinationIds().clear();
				}
				course.setCourseVaccineRequirement(vaccine);
			} else {
				if (!ObjectUtils.isEmpty(vaccine)) {
					vaccineDao.deleteById(vaccine.getId());;
				}
				course.setCourseVaccineRequirement(null);
			}
			
			CourseWorkExperienceRequirement workExperience = course.getCourseWorkExperienceRequirement();
			if (!ObjectUtils.isEmpty(courseOtherRequirementDto.getWorkExperience())) {
				if (ObjectUtils.isEmpty(workExperience)) {
					workExperience = new CourseWorkExperienceRequirement();
				}
				workExperience.setAuditFields(userId);
				workExperience.setCourse(course);
				workExperience.setDescription(courseOtherRequirementDto.getWorkExperience().getDescription());
				workExperience.setDuration(courseOtherRequirementDto.getWorkExperience().getDuration());
				workExperience.setDurationType(courseOtherRequirementDto.getWorkExperience().getDurationType());
				workExperience.setFields(courseOtherRequirementDto.getWorkExperience().getFields());
				course.setCourseWorkExperienceRequirement(workExperience);
			} else {
				if (!ObjectUtils.isEmpty(workExperience)) {
					workExperienceDao.deleteById(workExperience.getId());;
				}
				course.setCourseWorkExperienceRequirement(null);
			}

			CourseWorkPlacementRequirement workPlacement = course.getCourseWorkPlacementRequirement();
			if (!ObjectUtils.isEmpty(courseOtherRequirementDto.getWorkPlacement())) {
				if (ObjectUtils.isEmpty(workPlacement)) {
					workPlacement = new CourseWorkPlacementRequirement();
				}
				workPlacement.setAuditFields(userId);
				workPlacement.setCourse(course);
				workPlacement.setDescription(courseOtherRequirementDto.getWorkPlacement().getDescription());
				workPlacement.setDuration(courseOtherRequirementDto.getWorkPlacement().getDuration());
				workPlacement.setDurationType(courseOtherRequirementDto.getWorkPlacement().getDurationType());
				workPlacement.setFields(courseOtherRequirementDto.getWorkPlacement().getFields());
				course.setCourseWorkPlacementRequirement(workPlacement);
			} else {
				if (!ObjectUtils.isEmpty(workPlacement)) {
					workPlacementDao.deleteById(workPlacement.getId());;
				}
				course.setCourseWorkPlacementRequirement(null);
			}

			if (!ObjectUtils.isEmpty(courseOtherRequirementDto.getResearchProposal())) {
				CourseResearchProposalRequirement researchProposal = course.getCourseResearchProposalRequirement();
				if (ObjectUtils.isEmpty(researchProposal)) {
					researchProposal = new CourseResearchProposalRequirement();
				}
				researchProposal.setAuditFields(userId);
				researchProposal.setCourse(course);
				researchProposal.setDescription(courseOtherRequirementDto.getResearchProposal().getDescription());
				course.setCourseResearchProposalRequirement(researchProposal);
			} else {
				course.setCourseResearchProposalRequirement(null);
			}

			List<Course> coursesToBeSavedOrUpdated = new ArrayList<>();
			coursesToBeSavedOrUpdated.add(course);
//			if (!CollectionUtils.isEmpty(courseIntakeDto.getLinkedCourseIds())) {
//				List<CourseIntakeDto> dtosToReplicate = courseIntakes.stream()
//						.map(e -> modelMapper.map(e, CourseIntakeDto.class)).collect(Collectors.toList());
//				coursesToBeSavedOrUpdated
//						.addAll(replicateCourseIntakes(userId, coursePaymentDto.getLinkedCourseIds(), dtosToReplicate));
//			}
			courseDao.saveAll(coursesToBeSavedOrUpdated);

			log.info("Send notification for course content updates");
			commonProcessor.notifyCourseUpdates("COURSE_CONTENT_UPDATED", coursesToBeSavedOrUpdated);

			commonProcessor.saveElasticCourses(coursesToBeSavedOrUpdated);
		} else {
			log.error("invalid course id: {}", courseId);
			throw new NotFoundException("invalid course id: " + courseId);
		}
	}

	@Transactional
	public CourseOtherRequirementDto getOtherRequirements(String userId, String courseId) {
		log.info("inside CourseOtherRequirementProcessor.getOtherRequirements");
		Course course = courseDao.get(courseId);
		CourseOtherRequirementDto otherRequirementDto = new CourseOtherRequirementDto();
		if (!ObjectUtils.isEmpty(course)) {

			if (!ObjectUtils.isEmpty(course.getCourseVaccineRequirement())) {
				CourseVaccineRequirement model = course.getCourseVaccineRequirement();
				CourseVaccineRequirementDto dto = new CourseVaccineRequirementDto();
				dto.setId(model.getId());
				dto.setDescription(model.getDescription());
				if(!CollectionUtils.isEmpty(model.getVaccinationIds())) {
					var wrapperObject = new Object() {
						Map<UUID, String> mapOfVaccinationIdwithName =  null;
					};
					PaginationResponseDto<List<VaccinationDto>> vaccinationPaggination = commonHandler.getVaccinationByFilters(1, model.getVaccinationIds().size(), model.getVaccinationIds());

					if (!CollectionUtils.isEmpty(vaccinationPaggination.getResponse())) {
						wrapperObject.mapOfVaccinationIdwithName = vaccinationPaggination.getResponse().stream()
								.collect(Collectors.toMap(VaccinationDto::get_id, VaccinationDto::getName));
					}

					dto.setVaccination(model.getVaccinationIds().stream().map(vaccine -> {
						VaccinationDto vaccinationDto = new VaccinationDto();
						vaccinationDto.set_id(UUID.fromString(vaccine));
						if(!MapUtils.isEmpty(wrapperObject.mapOfVaccinationIdwithName) 
								&& wrapperObject.mapOfVaccinationIdwithName.containsKey(UUID.fromString(vaccine))) {
							vaccinationDto.setName(wrapperObject.mapOfVaccinationIdwithName.get(UUID.fromString(vaccine)));
						}

						return vaccinationDto;
					}).collect(Collectors.toSet()));
				}
				
				otherRequirementDto.setVaccine(dto);
			}

			if (!ObjectUtils.isEmpty(course.getCourseWorkExperienceRequirement())) {
				CourseWorkExperienceRequirement model = course.getCourseWorkExperienceRequirement();
				CourseWorkExperienceRequirementDto dto = new CourseWorkExperienceRequirementDto();
				dto.setId(model.getId());
				dto.setDescription(model.getDescription());
				dto.setDuration(model.getDuration());
				dto.setDurationType(model.getDurationType());
				dto.setFields(model.getFields());
				otherRequirementDto.setWorkExperience(dto);
			}

			if (!ObjectUtils.isEmpty(course.getCourseWorkPlacementRequirement())) {
				CourseWorkPlacementRequirement model = course.getCourseWorkPlacementRequirement();
				CourseWorkPlacementRequirementDto dto = new CourseWorkPlacementRequirementDto();
				dto.setId(model.getId());
				dto.setDescription(model.getDescription());
				dto.setDuration(model.getDuration());
				dto.setDurationType(model.getDurationType());
				dto.setFields(model.getFields());
				otherRequirementDto.setWorkPlacement(dto);
			}

			if (!ObjectUtils.isEmpty(course.getCourseResearchProposalRequirement())) {
				CourseResearchProposalRequirement model = course.getCourseResearchProposalRequirement();
				CourseResearchProposalRequirementDto dto = new CourseResearchProposalRequirementDto();
				dto.setId(model.getId());
				dto.setDescription(model.getDescription());
				otherRequirementDto.setResearchProposal(dto);
			}
		} else {
			log.error("invalid course id: {}", courseId);
			throw new NotFoundException("invalid course id: " + courseId);
		}
		return otherRequirementDto;
	}

//	private List<Course> replicateCourseIntakes(String userId, List<String> courseIds,
//			List<CourseIntakeDto> courseIntakeDtos) throws ValidationException, NotFoundException {
//		log.info("inside courseProcessor.replicateCourseIntakes");
//		List<Date> intakeDates = courseIntakeDtos.stream().map(CourseIntakeDto::getIntakeDate)
//				.collect(Collectors.toList());
//		if (!CollectionUtils.isEmpty(courseIds)) {
//			List<Course> courses = courseProcessor.validateAndGetCourseByIds(courseIds);
//			courses.stream().forEach(course -> {
//				List<CourseIntake> courseIntakes = course.getCourseIntakes();
//				if (CollectionUtils.isEmpty(courseIntakeDtos)) {
//					courseIntakes.clear();
//				} else {
//					courseIntakes.removeIf(e -> !Utils.contains(intakeDates, e.getIntakeDate()));
//					courseIntakeDtos.stream().forEach(dto -> {
//						Optional<CourseIntake> existingIntakeOp = courseIntakes.stream().filter(
//								e -> e.getIntakeDate().toInstant().compareTo(dto.getIntakeDate().toInstant()) == 0)
//								.findAny();
//						CourseIntake courseIntake = null;
//						if (existingIntakeOp.isPresent()) {
//							courseIntake = existingIntakeOp.get();
//						} else {
//							courseIntake = new CourseIntake();
//							courseIntake.setCourse(course);
//							courseIntake.setIntakeDate(dto.getIntakeDate());
//							courseIntakes.add(courseIntake);
//						}
//						courseIntake.setAuditFields(userId);
//					});
//				}
//			});
//			return courses;
//		}
//		return new ArrayList<>();
//	}
}