package com.tk.center.dto.log;

import com.tk.center.dto.record.TimeScopePageQuery;
import com.tk.center.entity.types.MemberOperateLogType;
import com.tk.center.entity.types.TerminalType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberOperateLogQuery extends TimeScopePageQuery {
    protected String memberId;
    protected MemberOperateLogType memberOperateLogType;

    protected String ip;
    protected TerminalType terminalType;
    protected String uuid;
    protected String _phone;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(memberId)) {
            c.and("memberId").is(memberId);
        }
        if (memberOperateLogType != null) {
            c.and("memberOperateLogType").is(memberOperateLogType);
        }
        if (!StringUtils.isEmpty(ip)) {
            c.and("ip").is(ip);
        }
        if (terminalType != null) {
            c.and("terminalType").is(terminalType);
        }
        if (!StringUtils.isEmpty(uuid)) {
            c.and("uuid").is(uuid);
        }
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MemberOperateLogType getMemberOperateLogType() {
        return memberOperateLogType;
    }

    public void setMemberOperateLogType(MemberOperateLogType memberOperateLogType) {
        this.memberOperateLogType = memberOperateLogType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public TerminalType getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalType terminalType) {
        this.terminalType = terminalType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }
}
