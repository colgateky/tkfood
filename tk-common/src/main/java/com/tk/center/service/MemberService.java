package com.tk.center.service;

import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.dao.CollectionIterator;
import com.tk.center.dto.member.MemberInfoQuery;
import com.tk.center.dto.member.MemberQuery;
import com.tk.center.entity.member.Member;
import com.tk.center.entity.member.MemberDto;
import com.tk.center.entity.member.MemberInfo;
import com.tk.center.entity.member.MemberTmpInfo;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MemberService {

    boolean updateMemberLoginPwd(String memberId, String password);

    boolean updateMemberPayPwd(String memberId, String password);

    boolean updateMemberAvatar(String memberId, Member member);

    boolean updateMemberNickName(String memberId, String nickName);

    boolean updatePlayerPhone(String memberId, String phone);

    boolean updatePlayerEmail(String memberId, String email);

    boolean updatePlayerRealName(String memberId, String realName);

    long getMemberCount(MemberQuery query);

    Map<String, Long> getMemberCounts(MemberQuery query);

    boolean updateMemberLastVisitTime(String memberId, Date lastVisitTime);

    boolean updateMemberLastVisitIp(String memberId, String ip);

    boolean updateMemberTotalPurchaseAmount(String memberId, BigDecimal amount);

    boolean disableMember(String memberId, Boolean disabled);

    boolean registerMember(Member member);

    Member getMember(String memberId);

    Member getMemberByUserName(String userName);

    void fetchMembers(MemberQuery query, CollectionIterator<Member> iter);

    Page<Member> getMembersWithPage(MemberQuery query);

    List<Member> getMembers(MemberQuery query);

    void updateMemberInfo(Member member, LoginUser loginUser);

    MemberInfo getMemberInfo(String id);

    MemberInfo saveMemberInfo(MemberInfo memberInfo);

    List<MemberInfo> getMemberInfos(MemberInfoQuery query);

    long getMemberInfoCount(MemberInfoQuery query);

    MemberTmpInfo getMemberTmpInfo(String id);

    void saveMemberTmpInfo(String id, MemberTmpInfo memberTmpInfo);

    void removeMemberTmpInfo(String id);

    List<Member> getOnlineMembers();

    void punishPlayer(String playerId);

    void kickMemberOff(String memberId);

    void updateLastPurchaseTime(String memberId);

    void updateMemberGroup(Member member);

    String getHidePhone(String phone);

    MemberDto getMemberDto(String memberId);

    void injectMember(Member member);
}
