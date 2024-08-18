package com.lyk.dto;


import com.lyk.util.Sort;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author Sunhan
 * @date 2015年12月18日 上午10:02:45
 */
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalCount = 0L;

    private long pageNum = 1L;

    private long pageSize = 20L;

    private Sort sort;

    public PageRequest() {
    }

    public PageRequest(long pageNum, long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static PageRequest of(long totalCount, long pageSize, long pageNum) {
        final PageRequest request = new PageRequest();
        request.setTotalCount(totalCount);
        request.setPageSize(pageSize);
        request.setPageNum(pageNum);
        return request;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageNum() {
        if (pageNum < 1) {
            return 1;
        }
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        if (pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public long getPageSize() {
        if (pageSize < 1) {
            return 1;
        }
        return pageSize > 1000 ? 1000 : pageSize;
    }

    public long getRealPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        if (pageSize < 1) {
            this.pageSize = 1;
        } else {
            this.pageSize = pageSize;
        }
    }

    public long getOffset() {
        return (getPageNum() - 1) * getPageSize();
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
