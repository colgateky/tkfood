package com.tk.center.dto.record;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberUpgradeRecordQuery extends RecordQuery {
    protected Integer level;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (level != null) {
            c.and("levelNow").is(level);
        }
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}