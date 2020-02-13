package com.tk.center.service;

import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.dao.CollectionIterator;
import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.service.LockFlow;
import com.magic.springboot.service.MagicService;
import com.magic.springboot.service.RedisCacheService;
import com.magic.utils.CryptUtils;
import com.magic.utils.DateUtils;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.common.Constant;
import com.tk.center.dto.member.MemberInfoQuery;
import com.tk.center.dto.member.MemberQuery;
import com.tk.center.entity.member.*;
import com.tk.center.entity.record.MemberUpgradeRecord;
import com.tk.center.entity.types.CashRecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Mingkun on 2020/02/04.
 */
@Service //Service用来组织多个model的调用
public class MemberServiceImpl implements MemberService {

    @Resource
    protected DaoHelper daoHelper;
    @Resource
    protected LoginUser loginUser;
    @Resource
    protected MagicService magicService;
    @Resource
    protected RedisCacheService redisCacheService;
    @Resource
    protected ConfigService configService;
    @Resource
    protected VariableService variableService;


    @Override
    public boolean updateMemberLoginPwd(String memberId, String password) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("password", password);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean updateMemberPayPwd(String memberId, String password) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("payPassword", password);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean updateMemberAvatar(String memberId, Member member) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("head", member.getHead());
        u.set("headUpdateTime", member.getHeadUpdateTime());
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    private boolean updateMemberInfo(String memberId, String field, String value) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set(field, value);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean updateMemberNickName(String memberId, String nickName) {
        return this.updateMemberInfo(memberId, "nickname", nickName);
    }

    @Override
    public boolean updatePlayerPhone(String memberId, String phone) {
        return this.updateMemberInfo(memberId, "phone", phone);
    }

    @Override
    public boolean updatePlayerEmail(String memberId, String email) {
        return this.updateMemberInfo(memberId, "email", email);
    }

    @Override
    public boolean updatePlayerRealName(String memberId, String realName) {
        return this.updateMemberInfo(memberId, "realName", realName);
    }

    @Override
    public long getMemberCount(MemberQuery query) {
        return daoHelper.getCount(query, Member.class);
    }

    @Override
    public Map<String, Long> getMemberCounts(MemberQuery query) {
        String[] ss = {"ONLINE", "OFFLINE",null};
        Map<String, Long> ret = (new HashMap<>());
        Date date = new Date();
        if (query.getLastVisitToNowStart() == null) {
            for (String s : ss) {
                if (s != null && s.equals("ONLINE")) {
                    query.setLastVisitToNowEnd(DateUtils.addMinute(date, -60));
                    query.setLastVisitToNowStart(null);
                } else if (s != null && s.equals("OFFLINE")) {
                    query.setLastVisitToNowStart(DateUtils.addMinute(date, -60));
                    query.setLastVisitToNowEnd(null);
                } else {
                    query.setLastVisitToNowStart(null);
                    query.setLastVisitToNowEnd(null);
                }

                long count = this.getMemberCount(query);
                String key = "";
                if (s != null) key = s;
                ret.put(key, count);
            }
        } else {
            long count = this.getMemberCount(query);
            ret.put("OFFLINE", count);
            ret.put("ONLINE", 0L);
            ret.put("", count);
        }
        return ret;
    }

    @Override
    public boolean updateMemberLastVisitTime(String memberId, Date lastVisitTime) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("lastVisitTime", lastVisitTime);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean updateMemberLastVisitIp(String memberId, String ip) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("lastVisitIp", ip);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean updateMemberTotalPurchaseAmount(String memberId, BigDecimal amount) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("totalPurchaseAmount", amount);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean disableMember(String memberId, Boolean disabled) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(memberId);
        q.addCriteria(c);
        Update u = new Update();
        u.set("disabled", disabled);
        daoHelper.updateFirst(q, u, Member.class);
        return true;
    }

    @Override
    public boolean registerMember(Member member) {
        return saveMember(member);
    }

    protected boolean saveMember(Member member) {
        daoHelper.save(member);
        return true;
    }

    @Override
    public Member getMember(String memberId) {
        return daoHelper.findById(memberId, Member.class);
    }

    @Override
    public Member getMemberByUserName(String userName){
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("username").is(userName);
        q.addCriteria(c);
        Member member = daoHelper.findOne(q, Member.class);
        return member;
    }

    @Override
    public void fetchMembers(MemberQuery query, CollectionIterator<Member> iter) {
        daoHelper.fetchCollections(query, iter, Member.class);
    }

    @Override
    public Page<Member> getMembersWithPage(MemberQuery query) {
        return daoHelper.getPage(query, Member.class);
    }

    @Override
    public List<Member> getMembers(MemberQuery query) {
        return daoHelper.find(query, Member.class);
    }

    @Override
    public void updateMemberInfo(Member member, LoginUser loginUser) {
        Update update = new Update();
        if (!StringUtils.isEmpty(member.getEmail()) && !member.getEmail().contains("*")){
            update.set("email", member.getEmail());
        }
        if (!StringUtils.isEmpty(member.getPhone()) && !member.getPhone().contains("*")) {
            update.set("phone", member.getPhone());
        }
        if (!StringUtils.isEmpty(member.getRealName()) && !member.getRealName().contains("*")) {
            update.set("realName", member.getRealName());
        }
        if (member.getBirthday() != null) {
            update.set("birthday", member.getBirthday());
        }
        if (member.getMarkIds() != null) {
            update.set("markIds", member.getMarkIds());
        }
        update.set("groupId", member.getGroupId());
        update.set("memberTagIds", member.getMemberTagIds());
        update.set("disabled", member.isDisabled());

        Date originUpdated = this.getMember(member.getId()).getUpdated();
        String originUpdatedString = StringUtils.isEmpty(originUpdated) ? "" : DateUtils.getDateTimeStr(originUpdated);
        String newUpdatedString = StringUtils.isEmpty(member.getUpdated()) ? "" : DateUtils.getDateTimeStr(member.getUpdated());
        if (!newUpdatedString.equals(originUpdatedString)) {
            ApiErrorCode.PERSIST_USER_FAILED.build("此会员信息已被修改，请刷新后重试").throwSelf();
        }
        update.set("updated", new Date());

        //更新等级
        this.updateMemberLevel(member, loginUser.getUserId(), false);
        if (StringUtils.isEmpty(member.getPassword()) == false && member.getPassword().trim().length() > 0) {
            update.set("password", CryptUtils.getMd5(member.getPassword().trim()));
        }
        if (StringUtils.isEmpty(member.getPayPassword()) == false && member.getPayPassword().trim().length() > 0) {
            update.set("payPassword", CryptUtils.getMd5(member.getPayPassword().trim()));
        }
        updateMember(member.getId(), update);
    }

    private void updateMemberLevel(Member member, String userId, boolean auto){
        Member m = magicService.get(member.getId(), Member.class);
        if (m.getLevel() == member.getLevel()){
            return;
        }
        MemberUpgradeRecord record = new MemberUpgradeRecord();
        record.setId(variableService.generateId(MemberUpgradeRecord.class, 8, "LR"));
        record.setMemberId(member.getId());
        record.setAuto(auto);
        record.setDealer(userId);
        record.setFinish(true);
        record.setLevelLast(m.getLevel());
        record.setLevelNow(member.getLevel());
        record.setUpgradeTime(new Date());
        record.setCreated(new Date());
        magicService.persist(record);
        magicService.updateField(member.getId(), "level", member.getLevel(), Member.class);
    }

    protected void updateMember(String id, Update update) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        c.and("id").is(id);
        q.addCriteria(c);
        daoHelper.updateFirst(q, update, Member.class);
    }

    @Override
    public MemberInfo getMemberInfo(String id) {
        return daoHelper.findById(id, MemberInfo.class);
    }

    @Override
    public MemberInfo saveMemberInfo(MemberInfo memberInfo) {
        memberInfo.setUpdated(new Date());
        return daoHelper.save(memberInfo);
    }

    @Override
    public List<MemberInfo> getMemberInfos(MemberInfoQuery query) {
        return daoHelper.find(query, MemberInfo.class);
    }

    @Override
    public long getMemberInfoCount(MemberInfoQuery query) {
        return daoHelper.getCount(query, MemberInfo.class);
    }

    @Override
    public MemberTmpInfo getMemberTmpInfo(String id) {
        MemberTmpInfo ret = redisCacheService.hashGet("member_tmp_info", id, MemberTmpInfo.class);
        if (ret == null) {
            ret = new MemberTmpInfo();
        }
        return ret;
    }

    @Override
    public void saveMemberTmpInfo(String id, MemberTmpInfo memberTmpInfo) {
        redisCacheService.hashSet("member_tmp_info", id, memberTmpInfo);
    }

    @Override
    public void removeMemberTmpInfo(String id) {
        redisCacheService.hashDel("member_tmp_info", id);
    }

    @Override
    public List<Member> getOnlineMembers() {
        Map<String, MemberTmpInfo> m = this.getAllMemberTmpInfos();
        long now = System.currentTimeMillis();
        long daySpan = 24 * 3600000L;
        long expireSpan = (7*daySpan);
        long alive = now - 3600000;
        List<Member> ret = new ArrayList<>();
        for (Map.Entry<String, MemberTmpInfo> e : m.entrySet()) {
            if (e.getValue() == null || e.getValue().getLastVisitTime() == null
                    || now - e.getValue().getLastVisitTime().getTime() > expireSpan) {
                this.removeMemberTmpInfo(e.getKey());
                continue;
            }

            long t = e.getValue().getLastVisitTime().getTime();
            if (t >= alive) {
                Member member = this.getMember(e.getKey());
                if (member != null) {
                    ret.add(member);
                }
            }
        }
        return ret;
    }

    //修改总购买金额 TODO
    //@Override
    //public void changeTotalPurchaseAmount(String memberId, BigDecimal incAmount, String recordId, CashRecordType recordType) {
    //
    //}
    //    final CashRecord record = new CashRecord();
    //    Player[] ret = new Player[1];
    //    redisLockService.lock(playerId, new LockFlow() {
    //        @Override
    //        public void run() {
    //            Player player = daoHelper.findById(playerId, Player.class);
    //            if (player == null) {
    //                ApiErrorCode.PLAYER_NOT_FOUND.throwSelf();
    //                return;
    //            }
    //            if (player.getAmount() == null) {
    //                player.setAmount(new BigDecimal(0));
    //            }
    //            BigDecimal cAmount = player.getAmount().add(incAmount);
    //            if (cAmount.compareTo(new BigDecimal(0)) < 0) {
    //                ApiErrorCode.PLAYER_NOT_ENOUGH.throwSelf();
    //            }
    //
    //            {
    //                record.setId(variableService.generateId(CashRecord.class, 10, "CSH"));
    //                record.setRecordType(recordType);
    //                record.setRelatedId(recordId);
    //                record.setCreated(new Date());
    //                record.setPreAmount(player.getAmount());
    //                record.setCurrentAmount(cAmount);
    //                record.setPlayerId(playerId);
    //                recordService.insertCashRecord(record);
    //            }
    //            player.setAmount(cAmount);
    //
    //            Update update = new Update();
    //            update.set("amount", cAmount);
    //            daoHelper.updateFirst(daoHelper.createIdQuery(playerId), update, Player.class);
    //
    //            ret[0] = player;
    //        }
    //    });
    //    // 禁止把外部调用逻辑放在lock里，避免产生死锁
    //    if (ret[0] != null) {
    //        updatePlayerInfo(ret[0], incAmount, recordType, recordId);
    //    }
    //    PlayerTmpInfo info = this.getPlayerTmpInfo(playerId);
    //    info.setLastPlayerTime(new Date().getTime());
    //    this.savePlayerTmpInfo(playerId, info);
    //}

    //修改积分 TODO
    //@Override
    //public long changePoint(String playerId, long incPoint, String recordId, PointRecordType type) {
    //
    //}
    //    final PointRecord record = new PointRecord();
    //    Player[] ret = new Player[1];
    //    redisLockService.lock("pointer_" + playerId, new LockFlow() {
    //        @Override
    //        public void run() {
    //            Player player = daoHelper.findById(playerId, Player.class);
    //            if (player == null) {
    //                ApiErrorCode.PLAYER_NOT_FOUND.throwSelf();
    //                return;
    //            }
    //            long cPoint = player.getPoint() + incPoint;
    //            if (cPoint < 0) {
    //                ApiErrorCode.PLAYER_NOT_ENOUGH.build(I18nUtil.getMessage(I18nConstant.MEMBER_POINT_NOT_ENOUGH)).throwSelf();
    //            }
    //            {
    //                record.setId(variableService.generateId(PointRecord.class, 10, "PIR"));
    //                record.setRecordType(type);
    //                record.setRelatedId(recordId);
    //                record.setCreated(new Date());
    //                record.setPrePoint(player.getPoint());
    //                record.setCurrentPoint(cPoint);
    //                record.setPlayerId(playerId);
    //                recordService.insertPointRecord(record);
    //            }
    //            player.setPoint(cPoint);
    //
    //            Update update = new Update();
    //            update.set("point", cPoint);
    //            daoHelper.updateFirst(daoHelper.createIdQuery(playerId), update, Player.class);
    //
    //            ret[0] = player;
    //        }
    //    });
    //    return ret[0].getPoint();
    //}

    @Override
    public void punishPlayer(String playerId) {
        // 如api下注 直接踢下线，并禁用会员
        this.kickMemberOff(playerId);
        magicService.updateField(playerId, "disabled", true, Member.class);
    }

    @Override
    public void kickMemberOff(String memberId) {
        boolean withPlatform = configService.getSysConfigAsBoolean(Constant.SINGLE_LOGIN_WITH_PLATFORM);
        if (withPlatform) {
            redisCacheService.hashDel("login_player", memberId + "#" + "PC");
            redisCacheService.hashDel("login_player", memberId + "#" + "WAP");
        } else{
            redisCacheService.hashDel("login_player", memberId);
        }
    }

    @Override
    public void updateLastPurchaseTime(String memberId) {
        MemberTmpInfo info = this.getMemberTmpInfo(memberId);
        info.setLastPurchaseTime(new Date());
        this.saveMemberTmpInfo(memberId, info);
    }

    @Override
    public void updateMemberGroup(Member member) {
        Update update = new Update();
        update.set("groupId", member.getGroupId());
        updateMember(member.getId(), update);
    }

    @Override
    public String getHidePhone(String phone) {
        if (!StringUtils.isEmpty(phone)) {
            if (phone.length() == 11) {
                String pre = phone.substring(0, 3);
                String end = phone.substring(7);
                phone = pre + "****" + end;
            } else {
                phone = "****";
            }
        }
        return phone;
    }

    @Override
    public MemberDto getMemberDto(String memberId) {
        Member member = this.getMember(memberId);
        MemberDto memberDto = new MemberDto();
        if (member != null) {
            String phone = this.getHidePhone(member.getPhone());
            if (loginUser.check("phone")) {
                phone = member.getPhone();
            }
            memberDto.setRealName(member.getRealName());
            memberDto.setPhone(phone);
            memberDto.setGroupId(member.getGroupId());
            memberDto.setLevelId("Level" + member.getLevel());
            memberDto.setNickName(member.getNickname());
            memberDto.setRemark(member.getRemark());
            memberDto.setMemberTagIds(member.getMemberTagIds());
        }
        MemberInfo memberInfo = magicService.get(memberId, MemberInfo.class);
        if (memberInfo != null) {
            memberDto.setTotalPurchaseAmount(memberInfo.getTotalPurchaseAmount());
            memberDto.setTotalPurchaseCount(memberInfo.getTotalPurchaseCount());
        }
        return memberDto;
    }

    @Override
    public void injectMember(Member member) {
        // 隱藏密碼
        member.setPassword("");
        member.setPayPassword("");
        member.setTmpInfo(this.getMemberTmpInfo(member.getId()));
        member.setMemberLevel(magicService.get("Level" + member.getLevel(), MemberLevel.class));
        MemberInfo memberInfo = this.getMemberInfo(member.getId());
        member.setMemberInfo(memberInfo);
        if (member.getUuid() != null) {
            MemberQuery query = new MemberQuery();
            query.setRegisterIP(member.getRegisterIP());
            // ip註冊數
            long count = magicService.count(query, Member.class);
            member.setCount(count);
        }
    }

    public Map<String, MemberTmpInfo> getAllMemberTmpInfos() {
        return redisCacheService.hashGetAll("member_tmp_info", MemberTmpInfo.class);
    }
}
