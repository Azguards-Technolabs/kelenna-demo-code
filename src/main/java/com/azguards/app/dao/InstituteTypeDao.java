package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.InstituteType;
import com.azguards.app.dto.InstituteTypeDto;

public interface InstituteTypeDao {

    public void save(InstituteType obj);

    public void update(InstituteType obj);

    public InstituteType get(String id);

    public List<InstituteTypeDto> getAll();
    
    public List<InstituteType> getByCountryName(String countryName);
    
    public InstituteType getInstituteTypeByNameAndCountry(String name, String countryName);

    public List<InstituteType> findAll();
}
