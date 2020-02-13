package com.tk.center.dto.member;

import com.tk.center.dto.record.TimeScopePageQuery;
import com.tk.center.entity.types.MessageType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class MemberMessageQuery extends TimeScopePageQuery {
    protected String id;
    protected String memberId;
    protected MessageType messageType;
    protected Boolean read;
    protected String _phone;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (StringUtils.isEmpty(id) == false) {
            c.and("id").is(id);
        }
        if (StringUtils.isEmpty(memberId) == false) {
            c.and("memberId").is(memberId);
        }
        if (StringUtils.isEmpty(messageType) == false) {
            c.and("messageType").is(messageType);
        }
        if (read != null) {
            c.and("read").is(read);
        }
    }
}
