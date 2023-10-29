package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.CourseDeliveryModes;

public interface CourseDeliveryModesDao {

	public List<CourseDeliveryModes> getCourseDeliveryModesByCourseId(String courseId);
}
