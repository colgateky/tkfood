package com.tk.center.dto.resource;

import com.magic.springboot.dto.PageQuery;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class FileResourceLibraryQuery extends PageQuery {
    protected String name;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        buildLike(c, "name", name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
