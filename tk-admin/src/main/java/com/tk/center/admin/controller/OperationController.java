package com.tk.center.admin.controller;

import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.entity.User;
import com.magic.springboot.service.AuthService;
import com.magic.springboot.service.MagicService;
import com.magic.utils.JsonUtils;
import com.magic.utils.UUIDGenerator;
import com.tk.center.admin.dto.*;
import com.tk.center.admin.dto.member.ApiMemberGroupPersistReq;
import com.tk.center.admin.dto.member.ApiMemberGroupQueryReq;
import com.tk.center.admin.dto.member.ApiMessagePersistReq;
import com.tk.center.admin.dto.member.ApiMessageQueryReq;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.dto.member.MemberQuery;
import com.tk.center.dto.record.OperationLogQuery;
import com.tk.center.dto.record.MemberTagQuery;
import com.tk.center.entity.member.Member;
import com.tk.center.entity.member.MemberGroup;
import com.tk.center.entity.member.MemberLevel;
import com.tk.center.entity.member.MemberTag;
import com.tk.center.entity.push.MemberMessage;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.service.BaseService;
import com.tk.center.service.OperationService;
import com.tk.center.service.RecordService;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/admin/operation")
public class OperationController {

    @Resource
    protected MagicService magicService;
    @Resource
    protected BaseService baseService;
    @Resource
    protected OperationService operationService;
    @Resource
    protected AuthService authService;
    @Resource
    protected RecordService recordService;

    @RequestMapping("member_groups.{ext}")
    @AuthResource("member_group")
    public ApiPageResp getPage(@RequestBody ApiMemberGroupQueryReq req) {
        Page<MemberGroup> page = magicService.getPage(req.getQuery(), MemberGroup.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("member_group_list.{ext}")
    public ApiListResp getList(@RequestBody ApiMemberGroupQueryReq req) {
        List<MemberGroup> page = magicService.getList(req.getQuery(), MemberGroup.class);
        ApiListResp ret = new ApiListResp();
        ret.setList(page);
        return ret;
    }

    @RequestMapping("member_group_persist.{ext}")
    @AuthResource({"member_group_edit"})
    public ApiEntityPersistResp persist(@RequestBody ApiMemberGroupPersistReq req){
        MemberGroup model = req.getModel();
        MemberGroup originalModel = magicService.get(model.getId(), MemberGroup.class);
        boolean isAdd = StringUtils.isEmpty(model.getId());
        String originalModelJson = JsonUtils.toJsonWithNull(originalModel);
        Map<String, Object> originalChangeMap = new HashMap<>();
        Map<String, Object> newChangeMap = new HashMap<>();
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(UUIDGenerator.generate());
        }
        if (model.getCreated() == null) {
            model.setCreated(new Date());
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        List<MemberGroup> groups = magicService.getList(new MemberQuery(), MemberGroup.class);
        boolean hasDefault = model.isDefaultGroup();
        for (MemberGroup group : groups) {
            if (group.isDefaultGroup()) {
                if (model.isDefaultGroup() && model.getId().equals(group.getId()) == false) {
                    group.setDefaultGroup(false);
                    magicService.persist(group);
                }
                hasDefault = true;
            }
            if (model.getLevel() == group.getLevel() && !model.getId().equals(group.getId())) {
                ApiErrorCode.PERSIST_FAILED.build("等級不得與其他用戶組相同").throwSelf();
            }
        }
        if (!hasDefault) {
            model.setDefaultGroup(true);
        }
        ret.setModel(magicService.persist(model));

        String newModelJson = JsonUtils.toJsonWithNull(model);
        Map<String, Object> newModelMap = JsonUtils.jsonToMap(new JSONObject(newModelJson));
        boolean hasChange = !newModelJson.equals(originalModelJson);

        if (!isAdd) {
            List<Map<String, Object>> maps = baseService.getLogStatusMaps(originalModelJson, newModelJson);
            originalChangeMap = maps.get(0);
            newChangeMap = maps.get(1);
        }
        if (isAdd || hasChange) {
            operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(), model.getName(),
                    isAdd ? "" : JsonUtils.toJsonWithNull(originalChangeMap),
                    isAdd ? JsonUtils.toJson2(newModelMap) : JsonUtils.toJsonWithNull(newChangeMap),
                    isAdd ? "新增" : "修改", "", req.getIp(), model.getClass()));
        }
        return ret;
    }

    @RequestMapping("member_tags.{ext}")
    @AuthResource("member_tag")
    public ApiPageResp getMemberTagPage(@RequestBody ApiQueryReq<MemberTagQuery> req) {
        Page<MemberTag> page = operationService.getMemberTagsWithPage(req.getQuery());
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("member_tag_list.{ext}")
    public ApiListResp getMemberTagList(@RequestBody ApiQueryReq<MemberTagQuery> req) {
        List<MemberTag> page = operationService.getMemberTags(req.getQuery());
        ApiListResp ret = new ApiListResp();
        ret.setList(page);
        return ret;
    }

    @RequestMapping("member_tag_persist.{ext}")
    @AuthResource({"member_tag_edit"})
    public ApiEntityPersistResp persistMemberTag(@RequestBody ApiPersistReq<MemberTag> req){
        MemberTag model = req.getModel();
        MemberTag originalModel = operationService.getMemberTag(model.getId());
        boolean isAdd = StringUtils.isEmpty(model.getId());
        String originalModelJson = JsonUtils.toJsonWithNull(originalModel);
        Map<String, Object> originalChangeMap = new HashMap<>();
        Map<String, Object> newChangeMap = new HashMap<>();
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(UUIDGenerator.generate());
        }
        if (model.getCreated() == null) {
            model.setCreated(new Date());
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(operationService.saveMemberTag(model));

        String newModelJson = JsonUtils.toJsonWithNull(model);
        Map<String, Object> newModelMap = JsonUtils.jsonToMap(new JSONObject(newModelJson));
        boolean hasChange = !newModelJson.equals(originalModelJson);

        if (!isAdd) {
            List<Map<String, Object>> maps = baseService.getLogStatusMaps(originalModelJson, newModelJson);
            originalChangeMap = maps.get(0);
            newChangeMap = maps.get(1);
        }
        if (isAdd || hasChange) {
            operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(), model.getName(),
                    isAdd ? "" : JsonUtils.toJsonWithNull(originalChangeMap),
                    isAdd ? JsonUtils.toJson2(newModelMap) : JsonUtils.toJsonWithNull(newChangeMap),
                    isAdd ? "新增" : "修改", "", req.getIp(), model.getClass()));
        }
        return ret;
    }

    @RequestMapping("member_tag_delete.{ext}")
    @AuthResource("member_tag_delete")
    public ApiEntityDeleteResp deleteMemberTag(@RequestBody ApiEntityDeleteReq req) {
        MemberTag originModel = operationService.getMemberTag(req.getId());
        String originModelJson = JsonUtils.toJson2(originModel);
        operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(), originModel.getName(),
                originModelJson, "", "删除", "", req.getIp(), originModel.getClass()));

        operationService.removeMemberTag(req.getId());
        ApiEntityDeleteResp ret = new ApiEntityDeleteResp();
        return ret;
    }

    @RequestMapping("operation_logs.{ext}")
    public ApiPageResp getOperationLogs(@RequestBody ApiQueryReq<OperationLogQuery> req) {
        Page<OperationLog> page = operationService.getOperationLogsWithPage(req.getQuery());
        for (int i=0; i<page.getContent().size(); i++) {
            User user = authService.getOneUser(page.getContent().get(i).getUserId());
            if (user != null) {
                page.getContent().get(i).setUserName(user.getUsername());
            }
        }
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("messages.{ext}")
    @AuthResource("message")
    public ApiPageResp getPage(@RequestBody ApiMessageQueryReq req) {
        recordService.setCommonQueryPlayerIdByPhone(req.getQuery());
        Page<MemberMessage> page = magicService.getPage(req.getQuery(), MemberMessage.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("message_persist.{ext}")
    @AuthResource({"message_edit"})
    public ApiEntityPersistResp persist(@RequestBody ApiMessagePersistReq req){
        MemberMessage model = req.getModel();
        if (StringUtils.isEmpty(model.getId())) {
            List<String> memberIds = new ArrayList<>();
            if (model.isAll()) {//所有用户
                List<Member> result = magicService.getList(new MemberQuery(), Member.class);
                for (Member member : result) {
                    memberIds.add(member.getId());
                }
            } else {//筛选用户
                MemberQuery query = new MemberQuery();
                if (!StringUtils.isEmpty(model.getMemberId())){
                    String memberId = model.getMemberId().trim();
                    if (!StringUtils.isEmpty(memberId)) {
                        String ids[] = memberId.split(",");
                        memberIds.addAll(Arrays.asList(ids));
                    }
                }
                if (!StringUtils.isEmpty(model.getGroupId())) {
                    query.setGroupId(model.getGroupId());
                    List<Member> result = magicService.getList(query, Member.class);
                    for (Member member : result) {
                        memberIds.add(member.getId());
                    }
                }
                if (!StringUtils.isEmpty(model.getLevelId())) {
                    MemberLevel memberLevel = magicService.get(model.getLevelId(), MemberLevel.class);
                    query = new MemberQuery();
                    query.setLevel(memberLevel.getLevel());
                    List<Member> result = magicService.getList(query, Member.class);
                    for (Member member : result) {
                        memberIds.add(member.getId());
                    }
                }
            }
            for (String id : memberIds) {
                MemberMessage msg = operationService.sendPersonMessage(id, model.getMessage());
                msg.setCustomerServiceId(req.getModel().getCustomerServiceId());
                magicService.persist(msg);
            }
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(model);
        return ret;
    }

    @RequestMapping("message_delete.{ext}")
    @AuthResource("message_delete")
    public ApiEntityDeleteResp deleteMessage(@RequestBody ApiEntityDeleteReq req) {
        String id = req.getId();
        magicService.delete(id, MemberMessage.class);
        ApiEntityDeleteResp ret = new ApiEntityDeleteResp();
        return ret;
    }
}
