package com.azguards.app.dao;import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Category;
import com.azguards.app.dto.CategoryDto;

@Repository
@SuppressWarnings("unchecked")
public class CategoryDAO implements ICategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<CategoryDto> getAllCategories() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("SELECT c.id, c.name as name FROM category c where c.active=1 ORDER BY c.name");
        List<Object[]> rows = query.list();
        
        List<CategoryDto> categoryDtos = new ArrayList<CategoryDto>();
        CategoryDto categoryDto = null;
        for (Object[] row : rows) {
            categoryDto = new CategoryDto();
            categoryDto.setId(row[0].toString());
            categoryDto.setName(row[1].toString());
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.get(Category.class, categoryId);
        CategoryDto categoryDto = null;
        if (category != null) {
            categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
        }
        return categoryDto;
    }

    @Override
    public Category findCategoryById(String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.get(Category.class, categoryId);
        return category;
    }

    @Override
    public Category findById(String id) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.get(Category.class, id);
        return category;
    }

    @Override
    public boolean saveCategory(Category category) {
        boolean status = true;
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(category);
        } catch (Exception exception) {
            status = false;
        }
        return status;
    }
}
