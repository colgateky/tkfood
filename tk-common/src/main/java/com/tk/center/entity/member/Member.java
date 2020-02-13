package com.tk.center.entity.member;

import com.tk.center.entity.BaseModel;
import com.tk.center.entity.types.SexType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tt on 2017/3/7.
 */
@Document
@CompoundIndex(name = "member_created", def = "{'created': -1}")
public class Member extends BaseModel {
    protected String username;
    protected String realName;
    protected String password;
    protected String payPassword;
    protected String email;
    protected String phone;
    protected long point; // 积分
    protected Integer level;    //等级
    protected String head;      //头像
    protected long headUpdateTime;
    protected String referer;
    protected String remark; //備註
    protected Date birthday;
    protected Set<String> markIds;

    @Indexed
    protected String groupId;   //分组id
    protected String nickname;  //别名
    protected String registerIP;//注册IP
    protected String webSite;//注册域名
    protected Date lastLoginTime;//最后登录时间

    protected BigDecimal totalPurchaseAmount;

    @Indexed
    protected String uuid;

    protected boolean disabled; //禁用
    @Transient
    protected MemberTmpInfo tmpInfo;
    @Transient
    protected MemberInfo memberInfo;
    @Transient
    protected long count; // uuid设备注册数量
    @Transient
    protected MemberLevel memberLevel;
    @Transient
    protected MemberGroup memberGroup;

    protected List<String> memberTagIds;

    protected String country;
    protected String city;
    protected SexType sex;

    protected String lastVisitIp;
    protected Date lastVisitTime;

    public static String createId(String username) {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHead() {
        return head;
    }

    public String getAvatar() {
        if (StringUtils.isEmpty(head)) {
            return null;
        }
        return "/resource/" + head;
    }

    public Set<String> getMarkIds() {
        return markIds;
    }

    public void setMarkIds(Set<String> markIds) {
        this.markIds = markIds;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNotNullNickName() {
        if (StringUtils.isEmpty(nickname)) {
            return username;
        } else {
            return nickname;
        }
    }

    public long getHeadUpdateTime() {
        return headUpdateTime;
    }

    public void setHeadUpdateTime(long headUpdateTime) {
        this.headUpdateTime = headUpdateTime;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisterIP() {
        return registerIP;
    }

    public void setRegisterIP(String registerIP) {
        this.registerIP = registerIP;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public MemberTmpInfo getTmpInfo() {
        return tmpInfo;
    }

    public void setTmpInfo(MemberTmpInfo tmpInfo) {
        this.tmpInfo = tmpInfo;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public List<String> getMemberTagIds() {
        return memberTagIds;
    }

    public void setMemberTagIds(List<String> memberTagIds) {
        this.memberTagIds = memberTagIds;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public MemberGroup getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(MemberGroup memberGroup) {
        this.memberGroup = memberGroup;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public String getLastVisitIp() {
        return lastVisitIp;
    }

    public void setLastVisitIp(String lastVisitIp) {
        this.lastVisitIp = lastVisitIp;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
}
