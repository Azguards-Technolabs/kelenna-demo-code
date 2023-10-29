package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.InstituteWorldRankingHistory;

public interface InstituteWorldRankingHistoryDao {

	public void save(InstituteWorldRankingHistory worldRanking);

	public List<InstituteWorldRankingHistory> getHistoryOfWorldRanking(String instituteId);

}
