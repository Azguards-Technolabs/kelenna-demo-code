package com.azguards.app.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
	
public class LevelCategoryUploadDto {
	@CsvBindByName(column = "Level Category")
	private String category;
	
	@CsvBindByName(column = "Level Type")
	private String code;

}
