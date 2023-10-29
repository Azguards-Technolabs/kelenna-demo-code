package com.azguards.app.dao;

import java.util.List;
import java.util.Optional;

import com.azguards.app.bean.AccrediatedDetail;

public interface AccrediatedDetailDao {

	public AccrediatedDetail addAccrediatedDetail (AccrediatedDetail accrediatedDetail);
	
	public List<AccrediatedDetail> getAccrediationDetailByEntityId (String entityId);
	
	public void deleteAccrediationDetailByEntityId (String entityId);
	
	public AccrediatedDetail getAccrediationBiNameAndEntityId (String accrediationName, String entityId);
	
	public Optional<AccrediatedDetail> getAccrediatedDetailById (String id);
	
	public void deleteAccrediationDetailById (String id);
	
	public List<AccrediatedDetail> getAllAccrediationDetails();
	
}
