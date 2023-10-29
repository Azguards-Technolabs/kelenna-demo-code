package com.azguards.app.dao;import java.util.List;

import com.azguards.app.bean.Category;
import com.azguards.app.dto.CategoryDto;

public interface ICategoryDAO {

     List<CategoryDto> getAllCategories();

     CategoryDto getCategoryById(String categoryId);

     Category findCategoryById(String category);

     Category findById(String id);

     boolean saveCategory(Category category);

}
