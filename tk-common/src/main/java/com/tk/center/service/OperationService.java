package com.tk.center.service;

import com.tk.center.dto.record.OperationLogQuery;
import com.tk.center.dto.record.MemberTagQuery;
import com.tk.center.entity.member.MemberTag;
import com.tk.center.entity.push.MemberMessage;
import com.tk.center.entity.record.OperationLog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OperationService {

    OperationLog insertOperationLog(OperationLog log);
    Page<OperationLog> getOperationLogsWithPage(OperationLogQuery query);

    Page<MemberTag> getMemberTagsWithPage(MemberTagQuery query);

    List<MemberTag> getMemberTags(MemberTagQuery query);

    MemberTag getMemberTag(String id);

    boolean removeMemberTag(String id);

    MemberTag saveMemberTag(MemberTag m);

    MemberMessage sendPersonMessage(String memberId, String message);

    MemberMessage sendPersonMessageWithHref(String memberId, String message, String href);
}
