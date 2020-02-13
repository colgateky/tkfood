package com.tk.center.dto.member;

import com.mongodb.DBObject;
import com.tk.center.dto.record.TimeScopePageQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class MemberQuery extends TimeScopePageQuery {
    protected String memberId;
    protected String username;
    protected String email;
    protected String phone;
    protected String groupId;
    protected String uuid;
    protected List<String> orMemberTagIds;
    protected String registerIP;
    protected Date lastVisitToNowStart;
    protected Date lastVisitToNowEnd;
    protected String recommendUserId;
    protected Boolean disabled;
    protected Boolean rubbished;
    protected boolean noSort;
    protected String referer;
    protected List<String> ids;
    protected List<String> notInIds;
    protected Integer level;
    protected String lastVisitIp;
    protected BigDecimal minAmount;
    protected BigDecimal maxAmount;
    protected Boolean forRealName; // true时realName完整匹配，非模糊
    protected String path0;
    protected String nickname;
    protected String realName;
    protected String webSite;//注册域名

    protected String userTrackId;


    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(memberId)) {
            c.and("id").is(memberId);
        }
        if (!StringUtils.isEmpty(username)) {
            c.and("username").regex(username);
        }
        if (!StringUtils.isEmpty(realName)) {
            if (forRealName != null && forRealName == true) {
                c.and("realName").is(realName);
            } else {
                c.and("realName").regex(realName);
            }
        }
        if (!StringUtils.isEmpty(email)) {
            c.and("email").is(email);
        }
        if (!StringUtils.isEmpty(phone)) {
            c.and("phone").is(phone);
        }
        if (!StringUtils.isEmpty(groupId)) {
            c.and("groupId").is(groupId);
        }
        if (!StringUtils.isEmpty(uuid)) {
            c.and("uuid").is(uuid.trim());
        }
        if (!StringUtils.isEmpty(referer)) {
            c.and("referer").regex(referer);
        }
        if (!StringUtils.isEmpty(webSite)) {
            c.and("webSite").regex(webSite);
        }
        if (orMemberTagIds != null && orMemberTagIds.size() > 0) {
            c.and("memberTagIds").in(orMemberTagIds);
        }

        if (ids != null && ids.size() > 0) {
            c.and("id").in(ids);
        }

        if (notInIds != null) {
            c.and("id").nin(notInIds);
        }

        if (StringUtils.isEmpty(registerIP) == false) {
            c.and("registerIP").is(registerIP);
        }

        if (lastVisitToNowStart != null) {
            c.orOperator(new Criteria().and("lastVisitTime").lte(lastVisitToNowStart),
                    new Criteria().and("lastVisitTime").is(null));
        }
        if (lastVisitToNowEnd != null) {
            c.and("lastVisitTime").gte(lastVisitToNowEnd);
        }

        if (!StringUtils.isEmpty(recommendUserId)) {
            c.and("recommendUserId").is(recommendUserId);
        }

        if (disabled != null) {
            c.and("disabled").is(disabled);
        }
        if (level != null) {
            c.and("level").is(level);
        }
        if (!StringUtils.isEmpty(lastVisitIp)) {
            c.and("lastVisitIp").is(lastVisitIp);
        }

        if (minAmount != null && maxAmount != null) {
            c.and("amount").lt(maxAmount).gte(minAmount);
        } else if (minAmount != null) {
            c.and("amount").gte(minAmount);
        } else if (maxAmount != null) {
            c.and("amount").lt(maxAmount);
        }

        if (!StringUtils.isEmpty(userTrackId)) {
            c.and("userTrackId").regex(userTrackId);
        }
        this.buildIs(c, "path.0", path0);
        this.buildLike(c, "nickname", nickname);
    }

    @Override
    public void buildSortObject(DBObject sort) {
        super.buildSortObject(sort);
        if (!noSort) {
            sort.put("created", -1);
        }
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getOrMemberTagIds() {
        return orMemberTagIds;
    }

    public void setOrMemberTagIds(List<String> orMemberTagIds) {
        this.orMemberTagIds = orMemberTagIds;
    }

    public String getRegisterIP() {
        return registerIP;
    }

    public void setRegisterIP(String registerIP) {
        this.registerIP = registerIP;
    }

    public Date getLastVisitToNowStart() {
        return lastVisitToNowStart;
    }

    public void setLastVisitToNowStart(Date lastVisitToNowStart) {
        this.lastVisitToNowStart = lastVisitToNowStart;
    }

    public Date getLastVisitToNowEnd() {
        return lastVisitToNowEnd;
    }

    public void setLastVisitToNowEnd(Date lastVisitToNowEnd) {
        this.lastVisitToNowEnd = lastVisitToNowEnd;
    }

    public String getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(String recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getRubbished() {
        return rubbished;
    }

    public void setRubbished(Boolean rubbished) {
        this.rubbished = rubbished;
    }

    public boolean isNoSort() {
        return noSort;
    }

    public void setNoSort(boolean noSort) {
        this.noSort = noSort;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getNotInIds() {
        return notInIds;
    }

    public void setNotInIds(List<String> notInIds) {
        this.notInIds = notInIds;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLastVisitIp() {
        return lastVisitIp;
    }

    public void setLastVisitIp(String lastVisitIp) {
        this.lastVisitIp = lastVisitIp;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Boolean getForRealName() {
        return forRealName;
    }

    public void setForRealName(Boolean forRealName) {
        this.forRealName = forRealName;
    }

    public String getPath0() {
        return path0;
    }

    public void setPath0(String path0) {
        this.path0 = path0;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getUserTrackId() {
        return userTrackId;
    }

    public void setUserTrackId(String userTrackId) {
        this.userTrackId = userTrackId;
    }
}
