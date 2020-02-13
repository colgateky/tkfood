package com.tk.center.dto.resource;

import com.magic.springboot.dto.PageQuery;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class FileResourceQuery extends PageQuery {
    protected String name;
    protected String id;
    protected String libraryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (StringUtils.isEmpty(libraryId) == false) {
            c.and("libraryId").is(libraryId);
        }
        if (StringUtils.isEmpty(id) == false) {
            c.and("id").is(id);
        }
        if (StringUtils.isEmpty(name) == false) {
            c.and("name").regex(name);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        super.buildSortObject(sort);
        sort.put("created", -1);
    }
}
