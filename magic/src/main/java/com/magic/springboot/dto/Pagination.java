package com.magic.springboot.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;

/**
 * Created by xm on 2017/5/19.
 */
public class Pagination<T> implements Page<T> {
    private int pageSize = 20;
    private int pageNo;
    private long totalCount;
    private int totalPage;
    private List<T> datas;
    public Pagination() {}
    public Pagination(int pageNo, int pageSize, long totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if (this.pageSize > 0) {
            this.totalPage = (int) (totalCount / (long) this.pageSize);
            if (totalCount % (long) this.pageSize != 0L) {
                ++this.totalPage;
            }
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setContent(List<T> datas) {
        this.datas = datas;
    }

    public int getTotalPages() {
        return this.totalPage;
    }

    public long getTotalElements() {
        return this.totalCount;
    }
    public void setTotalElements(long t) {
        this.totalCount = t;
    }

    public int getNumber() {
        return this.pageNo;
    }

    public int getSize() {
        return this.pageSize;
    }

    public int getNumberOfElements() {
        return this.datas == null?0:this.datas.size();
    }

    public List<T> getContent() {
        return this.datas;
    }

    public boolean hasContent() {
        return this.datas != null && this.datas.size() > 0;
    }

    public Sort getSort() {
        return null;
    }

    public boolean isFirst() {
        return this.pageNo <= 1;
    }

    public boolean isLast() {
        return this.pageNo == this.totalPage;
    }

    public boolean isHasNext() {
        return this.pageNo < this.totalPage;
    }

    public boolean isHasPrevious() {
        return this.pageNo > 1;
    }

    public boolean hasNext() {
        return this.pageNo < this.totalPage;
    }

    public boolean hasPrevious() {
        return this.pageNo > 1;
    }

    public Pageable nextPageable() {
        return null;
    }

    public Pageable previousPageable() {
        return null;
    }

    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
        return null;
    }

    public Iterator<T> iterator() {
        return null;
    }
}
