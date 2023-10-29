package com.azguards.app.dao;

import java.util.List;

import com.azguards.app.bean.Help;
import com.azguards.app.bean.HelpAnswer;
import com.azguards.app.bean.HelpCategory;
import com.azguards.app.bean.HelpSubCategory;
import com.azguards.common.lib.exception.NotFoundException;

public interface HelpDao {

	public void save(final Help help);

	public HelpCategory getHelpCategory(final String id);

	public HelpSubCategory getHelpSubCategory(final String id);

	public Help get(final String id);

	public void update(final Help help);

	public int findTotalHelpRecord(final String userId, final Boolean isArchive);

	public List<Help> getAll(final Integer startIndex, final Integer pageSize, final String userId,
			final Boolean isArchive);

	public void save(final HelpCategory helpCategory);

	public void save(final HelpSubCategory helpSubCategory);

	public List<HelpSubCategory> getSubCategoryByCategory(final String categoryId, final Integer startIndex,
			final Integer pageSize);

	public List<Help> getHelpByCategory(final String categoryId);

	public Integer findTotalHelpRecordBySubCategory(final String sub_category_id);

	public List<HelpSubCategory> getAllHelpSubCategories();

	public HelpAnswer save(final HelpAnswer helpAnswer);

	public List<HelpAnswer> getAnswerByHelpId(final String userId);

	public List<HelpCategory> getAllCategory(final Integer startIndex, final Integer pageSize);

	public List<Help> findByStatus(final String status, final String categoryId);

	public List<Help> findByMostRecent(final String mostRecent, final String categoryId);

	public List<Help> findByStatusAndMostRecent(final String status, final String mostRecent, final String categoryId);

	public void updateAnwser(final HelpAnswer helpAnswer);

	public void setIsFavouriteFlag(final String id, final boolean isFavourite) throws NotFoundException;

	public int getCategoryCount();

	public int getSubCategoryCount(final String categoryId);

	public List<String> getRelatedSearchQuestions(final List<String> searchKeywords);

}
