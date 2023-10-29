package com.azguards.app.dao;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.azguards.app.bean.Level;
import com.azguards.common.lib.dto.institute.LevelDto;

public interface LevelDao {

    public void addUpdateLevel(Level level);
    
    public void addUpdateLevels(List<Level> levels);

    public Level getLevel(String levelId);

    public List<Level> getAll();

    public List<Level> getCourseTypeByCountryId(String countryID);

    public List<Level> getLevelByCountryId(String countryId);

    public List<Level> getAllLevelByCountry();

    public List<LevelDto> getCountryLevel(String countryId);
    
	public List<String> getAllLevelNamesByInstituteId(final String instituteId);

	public List<Level> findByIdIn(List<String> ids);
	
	public Level getLevelByLevelCode(String levelCode);
	
	@Cacheable(value = "cacheLevelMap", unless = "#result == null")
	public Map<String, String> getAllLevelMap();
}
