package com.magic.springboot.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by xm on 2017/5/19.
 */
public class PageInfo implements Pageable {
    private int pageNumber;
    private int pageSize = 20;

    public PageInfo(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getOffset() {
        return (this.pageNumber - 1) * this.pageSize;
    }

    public Sort getSort() {
        return null;
    }

    public Pageable next() {
        return null;
    }

    public Pageable previousOrFirst() {
        return null;
    }

    public Pageable first() {
        return null;
    }

    public boolean hasPrevious() {
        return false;
    }
}