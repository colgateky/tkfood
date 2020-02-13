package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.service.MagicService;
import com.tk.center.dto.log.MemberOperateLogQuery;
import com.tk.center.dto.member.MemberQuery;
import com.tk.center.entity.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by Mingkun on 2020/02/04.
 */
@Service
public class RecordServiceImpl implements RecordService{

    @Resource
    protected DaoHelper daoHelper;

    @Resource
    protected MemberService memberService;
    @Resource
    protected ConfigService configService;
    @Resource
    protected BaseService baseService;

    @Resource
    protected MagicService magicService;

    @Resource
    protected OperationService operationService;

    @Override
    public void setCommonQueryPlayerIdByPhone(Object query) {
        //if (query instanceof ThirdBetRecordQuery) {
        //    ThirdBetRecordQuery q = (ThirdBetRecordQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else if (query instanceof CashRebateRecordQuery) {
        //    CashRebateRecordQuery q = (CashRebateRecordQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else if (query instanceof AuditRecordQuery) {
        //    AuditRecordQuery q = (AuditRecordQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else if (query instanceof MemberMessageQuery) {
        //    MemberMessageQuery q = (MemberMessageQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else if (query instanceof BetRecordQuery) {
        //    BetRecordQuery q = (BetRecordQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else if (query instanceof PlayerDailyReportQuery) {
        //    PlayerDailyReportQuery q = (PlayerDailyReportQuery)query;
        //    if (!StringUtils.isEmpty(q.getPlayerId()) || StringUtils.isEmpty(q.get_phone())) {
        //        return;
        //    }
        //    q.setPlayerId(getPlayerIdByPhone(q.get_phone()));
        //} else
        if (query instanceof MemberOperateLogQuery) {
            MemberOperateLogQuery q = (MemberOperateLogQuery) query;
            if (!StringUtils.isEmpty(q.getMemberId()) || StringUtils.isEmpty(q.get_phone())) {
                return;
            }
            q.setMemberId(getPlayerIdByPhone(q.get_phone()));
        }
    }

    private String getPlayerIdByPhone(String phone) {
        MemberQuery playerQuery = new MemberQuery();
        playerQuery.setPhone(phone.trim());
        Member member = magicService.getOne(playerQuery, Member.class);
        if (member != null) {
            return member.getId();
        } else {
            return "notexistplayerppppppppppppppp";
        }
    }

}
