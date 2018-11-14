package com.withkid.api.web.response;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageResponse<T> {
    private int currentPageNum;
    private int totalPageSize;
    private boolean hasNextPage;
    private boolean hasPriviousPage;
    private boolean isLastPage;
    private boolean isFirstPage;
    
	public void addPageInfo(Page<T> entity) {
		this.currentPageNum = entity.getNumber();
		this.totalPageSize = entity.getTotalPages();
		this.hasNextPage = entity.hasNext();
		this.hasPriviousPage = entity.hasPrevious();
		this.isLastPage = entity.isLast();
		this.isFirstPage = entity.isFirst();
	}
}
