package com.tk.center.dto.config;

import com.magic.springboot.dto.PageQuery;
import com.mongodb.DBObject;
import com.tk.center.entity.types.SysConfigType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class SysConfigCategoryQuery extends PageQuery {
    protected String name;
    protected Boolean shortCut;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        this.buildIs(c, "shortCut", shortCut);
        this.buildLike(c, "name", name);
    }

    @Override
    public void buildSortObject(DBObject sort) {
        sort.put("order", -1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShortCut() {
        return shortCut;
    }

    public void setShortCut(Boolean shortCut) {
        this.shortCut = shortCut;
    }
}
