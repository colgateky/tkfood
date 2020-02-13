package com.tk.center.dto.record;

import com.magic.springboot.dto.PageQuery;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberTagQuery extends PageQuery {
    protected String name;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(name)) {
            c.and("name").regex(name);
        }
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
}
