package com.azguards.app.controller.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.bean.Category;
import com.azguards.app.dto.CategoryDto;
import com.azguards.app.processor.CategoryProcessor;
import com.azguards.app.util.IConstant;

@RestController("categoryControllerV1")
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryProcessor categoryProcessor;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestHeader(value = IConstant.CORRELATION_ID, required = false, defaultValue = "") String correlationId,
                    @RequestHeader(value = IConstant.USER_ID, required = false) String userId, @RequestHeader(value = IConstant.SESSION_ID, required = false) String sessionId,
                    @RequestHeader(value = IConstant.TENANT_CODE, required = false) String tenantCode) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<CategoryDto> categoryDtos = categoryProcessor.getAllCategories();
        if (categoryDtos != null && !categoryDtos.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", IConstant.CATEGORY_GET_SUCCESS);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.CATEGORY_NOT_FOUND);
        }
        response.put("data", categoryDtos);
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCategoryById(@RequestHeader(value = IConstant.CORRELATION_ID, required = false, defaultValue = "") String correlationId,
                    @RequestHeader(value = IConstant.USER_ID, required = false) String userId, @RequestHeader(value = IConstant.SESSION_ID, required = false) String sessionId,
                    @RequestHeader(value = IConstant.TENANT_CODE, required = false) String tenantCode, @PathVariable String id) {
        Map<String, Object> response = new HashMap<String, Object>();
        CategoryDto categoryDto = categoryProcessor.getCategoryById(id);
        if (categoryDto != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", IConstant.CATEGORY_GET_SUCCESS);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.CATEGORY_NOT_FOUND);
        }
        response.put("data", categoryDto);
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        return ResponseEntity.accepted().body(categoryProcessor.saveCategory(category));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSuCategory(@PathVariable String id) {
        Map<String, Object> response = new HashMap<String, Object>();
        boolean status = categoryProcessor.deleteCategory(id);
        if (status) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", IConstant.CATEGORY_DELETE_SUCCESS);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.CATEGORY_NOT_FOUND);
        }
        return ResponseEntity.accepted().body(response);
    }
}
