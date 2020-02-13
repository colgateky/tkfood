package com.magic.springboot.dto;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xm on 2017/5/19.
 */
public class PageQuery {
    public static class SortField {
        public String field;
        public int sort;
    }
    protected int pageNo = 1;
    protected int pageSize = 10;

    protected List<SortField> sortFields;

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void buildCriteria(Criteria c) {

    }
    public void buildSortObject(DBObject sort) {

    }
    public void addSort(String key, int sort) {
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        SortField field = new SortField();
        field.field = key;
        field.sort = sort;
        sortFields.add(field);
    }
    public final void buildSort(DBObject sort) {
        if (sortFields != null && sortFields.size() > 0) {
            for (SortField field : sortFields) {
                sort.put(field.field, field.sort);
            }
        } else {
            this.buildSortObject(sort);
        }
    }

    protected void buildIs(Criteria c, String name, Object value) {
        if (value == null || value.toString().isEmpty()) {
            return;
        }
        c.and(name).is(value);
    }
    protected void buildTrue(Criteria c, String name, Boolean value) {
        if (value != null && value) {
            c.and(name).is(value);
        }
    }
    protected void buildIn(Criteria c, String name, Collection value) {
        if (value != null) {
            c.and(name).in(value);
        }
    }
    protected void buildIn(Criteria c, String name, Object value) {
        if (value != null) {
            c.and(name).in(value);
        }
    }
    protected void buildLike(Criteria c, String name, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        c.and(name).regex(value);
    }
}
