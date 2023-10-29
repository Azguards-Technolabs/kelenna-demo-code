package com.azguards.app.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1")
public interface CareerUploadInterface {

	@PostMapping("/upload/career")
	public ResponseEntity<?> uploadCareer(@RequestParam("careers") final MultipartFile multipartFile);
	
}
