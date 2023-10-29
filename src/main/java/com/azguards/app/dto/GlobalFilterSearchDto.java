package com.azguards.app.dto;

import java.io.Serializable;
import java.util.List;

import com.azguards.common.lib.enumeration.EntityTypeEnum;

import lombok.Data;

@Data
public class GlobalFilterSearchDto implements Serializable {

	private static final long serialVersionUID = 8396029582988011591L;

	private List<String> ids;
	private EntityTypeEnum entityType;
	private List<String> faculties;
	private List<String> levelIds;
	private List<String> serviceIds;
	private List<String> countryIds;
	private Double minCost;
	private Double maxCost;
	private Integer minDuration;
	private Integer maxDuration;
	private boolean sortAsscending;
	private String sortBy;
	private Integer maxSizePerPage;
	private Integer pageNumber;
	private String currencyCode;

}
