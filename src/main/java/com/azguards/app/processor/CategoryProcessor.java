package com.azguards.app.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azguards.app.bean.Category;
import com.azguards.app.dao.ICategoryDAO;
import com.azguards.app.dto.CategoryDto;
import com.azguards.app.util.IConstant;

@Service
@Transactional
public class CategoryProcessor {

    @Autowired
    private ICategoryDAO categoryDAO;
    
    public List<CategoryDto> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public CategoryDto getCategoryById(String categoryId) {
        return categoryDAO.getCategoryById(categoryId);
    }

    public Map<String, Object> saveCategory(Category categoryRequest) {
        Map<String, Object> response = new HashMap<String, Object>();
        Category category = null;
        boolean status = true;
        if (categoryRequest != null) {
            category = new Category();
            if (categoryRequest.getId() != null) {
                category = categoryDAO.findById(categoryRequest.getId());
            } else {
                category.setActive(true);
            }
            category.setName(categoryRequest.getName());
            status = categoryDAO.saveCategory(category);
            if (status) {
                response.put("status", IConstant.SUCCESS_CODE);
                response.put("message", IConstant.SUCCESS_MESSAGE);
            } else {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("message", IConstant.SUB_CATEGORY_ERROR);
            }
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.CATEGORY_NOT_FOUND);
        }
        return response;
    }

    
    public boolean deleteCategory(String categoryId) {
        boolean status = true;
        try {
            if (categoryId != null) {
                Category subCategory = categoryDAO.findById(categoryId);
                if (subCategory != null) {
                    subCategory.setActive(false);
                    categoryDAO.saveCategory(subCategory);
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
