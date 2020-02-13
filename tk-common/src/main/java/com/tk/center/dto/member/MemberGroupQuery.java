package com.tk.center.dto.member;

import com.magic.springboot.dto.PageQuery;
import org.springframework.data.mongodb.core.query.Criteria;

public class MemberGroupQuery extends PageQuery {
    protected String name;
    protected Boolean defaultGroup;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        this.buildLike(c, "name", name);
        this.buildIs(c, "defaultGroup", defaultGroup);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
}
