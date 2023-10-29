package com.azguards.app.endpoint;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azguards.common.lib.dto.institute.GradeDto;

@RequestMapping("/api/v1/grade")
public interface GradeInterface {

	@PostMapping("/calculate")
    public ResponseEntity<?> calculate(@Valid @RequestBody final GradeDto gradeDto) throws Exception;
	
}
