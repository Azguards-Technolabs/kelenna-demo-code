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

import com.azguards.app.dto.SubCategoryDto;
import com.azguards.app.service.ISubCategoryService;
import com.azguards.app.util.IConstant;

@RestController("subCategoryControllerV1")
@RequestMapping("/api/v1/subCategory")
public class SubCategoryController {

    @Autowired
    private ISubCategoryService subCategoryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestHeader(value = IConstant.CORRELATION_ID, required = false, defaultValue = "") String correlationId,
                    @RequestHeader(value = IConstant.USER_ID, required = false) String userId, @RequestHeader(value = IConstant.SESSION_ID, required = false) String sessionId,
                    @RequestHeader(value = IConstant.TENANT_CODE, required = false) String tenantCode) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<SubCategoryDto> subCategoryDtos = subCategoryService.getAllSubCategories();
        if (subCategoryDtos != null && !subCategoryDtos.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Sub category fetched successfully");
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.SUB_CATEGORY_NOT_FOUND);
        }
        response.put("data", subCategoryDtos);
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSubCategoryByCategory(@RequestHeader(value = IConstant.CORRELATION_ID, required = false, defaultValue = "") String correlationId,
                    @RequestHeader(value = IConstant.USER_ID, required = false) String userId, @RequestHeader(value = IConstant.SESSION_ID, required = false) String sessionId,
                    @RequestHeader(value = IConstant.TENANT_CODE, required = false) String tenantCode, @PathVariable String categoryId) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<SubCategoryDto> subCategoryDtos = subCategoryService.getSubCategoryByCategory(categoryId);
        if (subCategoryDtos != null && !subCategoryDtos.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Sub category fetched successfully");
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.SUB_CATEGORY_NOT_FOUND);
        }
        response.put("data", subCategoryDtos);
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSubCategoryById(@RequestHeader(value = IConstant.CORRELATION_ID, required = false, defaultValue = "") String correlationId,
                    @RequestHeader(value = IConstant.USER_ID, required = false) String userId, @RequestHeader(value = IConstant.SESSION_ID, required = false) String sessionId,
                    @RequestHeader(value = IConstant.TENANT_CODE, required = false) String tenantCode, @PathVariable String id) {
        Map<String, Object> response = new HashMap<String, Object>();
        SubCategoryDto subCategoryDto = subCategoryService.getSubCategoryById(id);
        if (subCategoryDto != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Sub category fetched successfully");
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.SUB_CATEGORY_NOT_FOUND);
        }
        response.put("data", subCategoryDto);
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveArticle(@RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.accepted().body(subCategoryService.saveSubCategory(subCategoryDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSuCategory(@PathVariable String id) {
        Map<String, Object> response = new HashMap<String, Object>();
        boolean status = subCategoryService.deleteSubCategory(id);
        if (status) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Sub category deleted successfully");
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", IConstant.SUB_CATEGORY_NOT_FOUND);
        }
        return ResponseEntity.accepted().body(response);
    }
}
