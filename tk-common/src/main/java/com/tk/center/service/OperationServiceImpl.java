package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.service.MagicService;
import com.magic.utils.UUIDGenerator;
import com.tk.center.dto.record.OperationLogQuery;
import com.tk.center.dto.record.MemberTagQuery;
import com.tk.center.entity.member.MemberTag;
import com.tk.center.entity.member.MemberTmpInfo;
import com.tk.center.entity.push.MemberMessage;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.entity.types.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Mingkun on 2020/02/03.
 */
@Service
public class OperationServiceImpl implements OperationService {

    @Resource
    protected DaoHelper daoHelper;
    @Resource
    protected MagicService magicService;
    @Resource
    protected MemberService memberService;


    @Override
    public OperationLog insertOperationLog(OperationLog log) {
        if (StringUtils.isEmpty(log.getId())) {
            return null;
        }
        return daoHelper.insert(log);
    }

    @Override
    public Page<OperationLog> getOperationLogsWithPage(OperationLogQuery query) {
        return daoHelper.getPage(query, OperationLog.class);
    }

    @Override
    public Page<MemberTag> getMemberTagsWithPage(MemberTagQuery query) {
        return daoHelper.getPage(query, MemberTag.class);
    }

    @Override
    public List<MemberTag> getMemberTags(MemberTagQuery query) {
        return daoHelper.find(query, MemberTag.class);
    }

    @Override
    public MemberTag getMemberTag(String id) { return daoHelper.findById(id, MemberTag.class); }

    @Override
    public boolean removeMemberTag(String id) {
        return daoHelper.removeById(id, MemberTag.class);
    }

    @Override
    public MemberTag saveMemberTag(MemberTag m) {
        return daoHelper.save(m);
    }

    @Override
    public MemberMessage sendPersonMessage(String memberId, String message) {
        return this.sendPersonMessageWithHref(memberId, message, null);
    }

    @Override
    public MemberMessage sendPersonMessageWithHref(String memberId, String message, String href) {
        MemberMessage ret = this.sendMessage(MessageType.PERSON, memberId, message, href);

        // 个人消息，更新时间
        MemberTmpInfo tmpInfo = memberService.getMemberTmpInfo(memberId);
        tmpInfo.setLastMessageTime(System.currentTimeMillis());
        memberService.saveMemberTmpInfo(memberId, tmpInfo);
        return ret;
    }

    protected MemberMessage sendMessage(MessageType msgType, String memberId, String message, String href) {
        MemberMessage m = new MemberMessage();
        m.setId(UUIDGenerator.generate());
        m.setCreated(new Date());
        m.setUpdated(new Date());
        m.setMessageType(msgType);
        m.setMemberId(memberId);
        m.setMessage(message);
        m.setRead(false);
        m.setHref(href);
        return magicService.persist(m);
    }
}
