package com.azguards.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDto {
	
	private Integer totalCount;
	private Boolean showMore;
	private Object response;
}
