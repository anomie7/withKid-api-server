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
    
	public void addPageInfo(Page<?> page) {
		this.currentPageNum = page.getNumber();
		this.totalPageSize = page.getTotalPages();
		this.hasNextPage = page.hasNext();
		this.hasPriviousPage = page.hasPrevious();
		this.isLastPage = page.isLast();
		this.isFirstPage = page.isFirst();
	}
}
