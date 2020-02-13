package com.tk.center.entity.log;

import com.magic.utils.UUIDGenerator;
import com.tk.center.entity.BaseModel;
import com.tk.center.entity.member.MemberDto;
import com.tk.center.entity.types.MemberOperateLogType;
import com.tk.center.entity.types.TerminalType;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberOperateLog extends BaseModel {
    protected String memberId;
    protected MemberOperateLogType memberOperateLogType;
    protected String detail;

    protected String ip;
    protected TerminalType terminalType;
    protected String uuid;

    @Transient
    protected MemberDto memberDto;

    public static MemberOperateLog create(String playerId,
                                          MemberOperateLogType memberOperateLogType,
                                          String detail,
                                          String ip,
                                          TerminalType terminalType,
                                          String uuid,
                                          String siteId) {
        MemberOperateLog log = new MemberOperateLog();
        log.setId(UUIDGenerator.generate());
        log.setMemberId(playerId);
        log.setMemberOperateLogType(memberOperateLogType);
        log.setDetail(detail);
        log.setIp(ip);
        log.setTerminalType(terminalType);
        log.setUuid(uuid);
        log.setCreated(new Date());
        return log;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public void setMemberDto(MemberDto memberDto) {
        this.memberDto = memberDto;
    }
}
