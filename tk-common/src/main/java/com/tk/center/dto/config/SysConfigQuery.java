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
public class SysConfigQuery extends PageQuery {
    protected String id;
    protected String name;
    protected SysConfigType type;
    protected Boolean visible;
    protected String categoryId;
    protected Boolean open;
    protected List<String> ids;
    protected String idLike;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

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

    public SysConfigType getType() {
        return type;
    }

    public void setType(SysConfigType type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getIdLike() {
        return idLike;
    }

    public void setIdLike(String idLike) {
        this.idLike = idLike;
    }

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (ids != null && ids.size() > 0) {
            c.and("id").in(ids);
        } else if ( StringUtils.isEmpty(id) == false) {
            c.and("id").is(id);
        } else if (!StringUtils.isEmpty(idLike)){
            c.and("id").regex(idLike);
        }
        if (StringUtils.isEmpty(name) == false) {
            c.and("name").regex(name);
        }
        if (StringUtils.isEmpty(categoryId) == false) {
            c.and("categoryId").is(categoryId);
        }
        if (open != null) {
            c.and("open").is(open);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        sort.put("order", -1);
    }
}
