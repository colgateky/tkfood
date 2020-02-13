package com.tk.center.service;

import com.tk.center.entity.member.Member;
import com.tk.center.entity.member.MemberTmpInfo;
import com.tk.center.entity.types.TerminalType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Mingkun on 2020/02/06.
 */
@Service
public class RealtimeStatServiceImpl implements RealtimeStatService {

    @Resource
    protected MemberService memberService;

    @Override
    public void onMemberRegister(String playerId, TerminalType type) {
        MemberTmpInfo info = memberService.getMemberTmpInfo(playerId);
        info.setRegisterTime(System.currentTimeMillis());
        info.setLastVisitTime(new Date());
        info.setLastTerminal(type);
        memberService.saveMemberTmpInfo(playerId, info);
    }
}
