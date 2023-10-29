package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.InstituteDomesticRankingHistory;

public interface InstituteDomesticRankingHistoryDao {

	public void save(InstituteDomesticRankingHistory domesticRanking);

	public List<InstituteDomesticRankingHistory> getHistoryOfDomesticRanking(String instituteId);

}
