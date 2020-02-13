package com.tk.center.dto.record;

import com.mongodb.DBObject;
import com.tk.center.entity.types.CashRecordType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class RecordQuery extends TimeScopePageQuery{

    private CashRecordType recordType;
    private List<String> recordTypes;
    private String memberId;
    private String id;
    private List<String> memberIds;

    private String _phone;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(memberId)) {
            c.and("memberId").is(memberId);
        }else if (memberIds != null){
            c.and("memberId").in(memberIds);
        }
        if (recordType != null) {
            c.and("recordType").is(recordType);
        }
        if (recordTypes != null && recordTypes.size() > 0) {
            c.and("recordType").in(recordTypes);
        }
        if (!StringUtils.isEmpty(id)) {
            c.and("id").is(id);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        super.buildSortObject(sort);
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public List<String> getRecordTypes() {
        return recordTypes;
    }

    public void setRecordTypes(List<String> recordTypes) {
        this.recordTypes = recordTypes;
    }

    public CashRecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(CashRecordType recordType) {
        this.recordType = recordType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }
}
