package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.CourseInstitute;

@Repository
public interface CourseInstituteRepository extends JpaRepository<CourseInstitute, String> {

	@Query("select courseInstitute from CourseInstitute courseInstitute where courseInstitute.id in "
			+ "( select id from CourseInstitute ci where ci.sourceCourse.id = :courseId or ci.destinationCourse.id = :courseId )"
			+ " OR " + " courseInstitute.id in (select id from CourseInstitute ci where ci.sourceCourse in"
			+ " (select sourceCourse from CourseInstitute ciSub where ciSub.destinationCourse.id = :courseId))")
	List<CourseInstitute> findLinkedInstitutes(String courseId);

	CourseInstitute findByDestinationCourseId(String destinationCourseId);
}
