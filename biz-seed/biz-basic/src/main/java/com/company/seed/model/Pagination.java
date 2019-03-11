package com.company.seed.model;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页排序类
 * Created by yoara on 2015/12/21.
 */
public class Pagination<T> implements Serializable {
	public final static int DEFAULT_PAGE = 1;
	public final static int DEFAULT_PAGE_SIZE = 20;
	private static final long serialVersionUID = -2526095345442029669L;

	protected List<T> items;
	protected int recordCount;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected int currentPageStartIndex = 0;
	protected int currentPage = DEFAULT_PAGE;
	protected Sort sort = Sort.DESC; // 排序方式 DESC/ASC
	protected String sortField; // 排序字段
	private boolean queryRecordCount = true; //是否查询总记录数

	public static enum Sort {
		DESC, ASC
	}

	public Pagination() {
		this(DEFAULT_PAGE_SIZE, DEFAULT_PAGE);
	}
	public Pagination(int pageSize, int page) {
		this(pageSize,page,true);
	}
	
	public Pagination(int pageSize, int page, boolean queryRecordCount) {
		if (pageSize < 1 || page < 1) {
			throw new IllegalArgumentException("pageSize or page should be greater than zero!");
		}
		this.pageSize = pageSize;
		this.currentPage = page;
		this.queryRecordCount = queryRecordCount;
	}
	
	public static boolean hasResults(Pagination<?> page) {
		if (null != page && null != page.getItems() 
				&& !page.getItems().isEmpty()) {
				return true;			
		}
		return false;
	}
	
	public void setPageSize(int countOnEachPage) {
		this.pageSize = countOnEachPage;
	}

	public List<T> getItems() {
		return items;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public int getCurrentPageStartIndex() {
		currentPageStartIndex = (currentPage - 1) * pageSize;
		return currentPageStartIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public void setRecordCount(int totalCount) {
		this.recordCount = totalCount;
	}

	public int getPageCount() {
		return (recordCount % pageSize == 0) ? (recordCount / pageSize)
				: (recordCount / pageSize) + 1;
	}

	public int getPreviousPage() {
		if(currentPage > 1) return currentPage - 1;
		else return DEFAULT_PAGE;
	}
	
	public int getNextPage() {
		if(currentPage < getPageCount()) return currentPage + 1;
		else return getPageCount();
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public boolean isQueryRecordCount() {
		return queryRecordCount;
	}
	
	public void setQueryRecordCount(boolean queryRecordCount) {
		this.queryRecordCount = queryRecordCount;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
}