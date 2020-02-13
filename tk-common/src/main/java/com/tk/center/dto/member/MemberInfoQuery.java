package com.tk.center.dto.member;

import com.magic.springboot.dto.PageQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class MemberInfoQuery extends PageQuery {
    protected Date firstDepositStart;
    protected Date firstDepositFinish;
    protected Date secondDepositStart;
    protected Date secondDepositFinish;
    protected Date thirdDepositStart;
    protected Date thirdDepositFinish;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (firstDepositStart != null && firstDepositFinish != null) {
            c.and("firstDepositTime").gte(firstDepositStart).lt(firstDepositFinish);
        } else if (firstDepositStart != null) {
            c.and("firstDepositTime").gte(firstDepositStart);
        } else if (firstDepositFinish != null) {
            c.and("firstDepositTime").lt(firstDepositFinish);
        }

        if (secondDepositStart != null && secondDepositFinish != null) {
            c.and("secondDepositTime").gte(secondDepositStart).lt(secondDepositFinish);
        } else if (secondDepositStart != null) {
            c.and("secondDepositTime").gte(secondDepositStart);
        } else if (secondDepositFinish != null) {
            c.and("secondDepositTime").lt(secondDepositFinish);
        }

        if (thirdDepositStart != null && thirdDepositFinish != null) {
            c.and("thirdDepositTime").gte(thirdDepositStart).lt(thirdDepositFinish);
        } else if (thirdDepositStart != null) {
            c.and("thirdDepositTime").gte(thirdDepositStart);
        } else if (thirdDepositFinish != null) {
            c.and("thirdDepositTime").lt(thirdDepositFinish);
        }
    }

    public Date getFirstDepositStart() {
        return firstDepositStart;
    }

    public void setFirstDepositStart(Date firstDepositStart) {
        this.firstDepositStart = firstDepositStart;
    }

    public Date getFirstDepositFinish() {
        return firstDepositFinish;
    }

    public void setFirstDepositFinish(Date firstDepositFinish) {
        this.firstDepositFinish = firstDepositFinish;
    }

    public Date getSecondDepositStart() {
        return secondDepositStart;
    }

    public void setSecondDepositStart(Date secondDepositStart) {
        this.secondDepositStart = secondDepositStart;
    }

    public Date getSecondDepositFinish() {
        return secondDepositFinish;
    }

    public void setSecondDepositFinish(Date secondDepositFinish) {
        this.secondDepositFinish = secondDepositFinish;
    }

    public Date getThirdDepositStart() {
        return thirdDepositStart;
    }

    public void setThirdDepositStart(Date thirdDepositStart) {
        this.thirdDepositStart = thirdDepositStart;
    }

    public Date getThirdDepositFinish() {
        return thirdDepositFinish;
    }

    public void setThirdDepositFinish(Date thirdDepositFinish) {
        this.thirdDepositFinish = thirdDepositFinish;
    }
}
