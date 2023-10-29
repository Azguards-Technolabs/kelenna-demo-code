package com.azguards.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.SubCategory;
import com.azguards.app.dao.ICategoryDAO;
import com.azguards.app.dao.ISubCategoryDAO;
import com.azguards.app.dto.SubCategoryDto;
import com.azguards.app.util.IConstant;
import com.azguards.common.lib.util.DateUtil;

@Service
@Transactional
public class SubCategoryService implements ISubCategoryService {

    @Autowired
    private ISubCategoryDAO subCategoryDAO;

    @Autowired
    private ICategoryDAO categoryDAO;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        return subCategoryDAO.getAllSubCategories();
    }

    @Override
    public List<SubCategoryDto> getSubCategoryByCategory(String categoryId) {
        return subCategoryDAO.getSubCategoryByCategory(categoryId);
    }

    @Override
    public SubCategoryDto getSubCategoryById(String subCategoryId) {
        return subCategoryDAO.getSubCategoryById(subCategoryId);
    }

    @Override
    public Map<String, Object> saveSubCategory(SubCategoryDto subCategoryDto) {
        Map<String, Object> response = new HashMap<String, Object>();
        SubCategory subCategory = null;
        boolean status = true;
        if (subCategoryDto != null) {
            subCategory = new SubCategory();
            if (subCategoryDto.getId() != null) {
                subCategory = subCategoryDAO.findById(subCategoryDto.getId());
            }
            subCategory.setName(subCategoryDto.getName());
            subCategory.setCategory(categoryDAO.findCategoryById(subCategoryDto.getCategoryId()));
            subCategory.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
            subCategory.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
            subCategory.setIsDeleted(true);
            status = subCategoryDAO.saveSubCategory(subCategory);
            if (status) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Sub category added successfully");
            } else {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("message", IConstant.SQL_ERROR);
            }
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.SUB_CATEGORY_NOT_FOUND);
        }
        return response;
    }

    @Override
    public boolean deleteSubCategory(String subCategoryId) {
        boolean status = true;
        try {
            if (subCategoryId != null) {
                SubCategory subCategory = subCategoryDAO.findById(subCategoryId);
                if (subCategory != null) {
                    subCategory.setDeletedOn(DateUtil.getUTCdatetimeAsDate());
                    subCategory.setIsDeleted(false);
                    subCategoryDAO.saveSubCategory(subCategory);
                } else {
                    status = false;
                }
            } else {
                status = false;
            }
        } catch (Exception e) {
            status = false;
        }
        return status;
    }
}
