package com.tk.center.entity.record;


import com.tk.center.entity.BaseModel;
import com.tk.center.entity.member.Member;
import com.tk.center.entity.member.MemberDto;
import org.springframework.data.annotation.Transient;

/**
 * Created by xm on 2017/5/21.
 */
public class BaseRecord extends BaseModel {
    protected String memberId;
    @Transient
    protected Member member;
    @Transient
    protected MemberDto memberDto;

    protected String ip;
    protected String version;
    protected String uuid;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
