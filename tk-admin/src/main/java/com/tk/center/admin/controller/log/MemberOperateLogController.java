package com.tk.center.admin.controller.log;

import com.tk.center.admin.controller.BaseController;
import com.tk.center.admin.dto.ApiQueryReq;
import com.tk.center.dto.log.MemberOperateLogQuery;
import com.tk.center.entity.log.MemberOperateLog;
import com.tk.center.entity.member.MemberDto;
import com.tk.center.service.MemberService;
import com.tk.center.service.RecordService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Mingkun on 2020/02/04.
 */
@RestController
@RequestMapping("/admin/member_operate_log")
public class MemberOperateLogController extends BaseController<MemberOperateLog, MemberOperateLogQuery> {
    @Resource
    protected MemberService memberService;
    @Resource
    protected RecordService recordService;

    @Override
    protected Class<MemberOperateLog> getModelCls() {
        return MemberOperateLog.class;
    }

    @Override
    protected void onBeforePage(ApiQueryReq<MemberOperateLogQuery> req) {
        super.onBeforePage(req);
        recordService.setCommonQueryPlayerIdByPhone(req.getQuery());
    }

    @Override
    protected void onAfterPage(Page<MemberOperateLog> page) {
        super.onAfterPage(page);
        for (MemberOperateLog log: page.getContent()){
            MemberDto memberDto = memberService.getMemberDto(log.getMemberId());
            log.setMemberDto(memberDto);
        }
    }
}
