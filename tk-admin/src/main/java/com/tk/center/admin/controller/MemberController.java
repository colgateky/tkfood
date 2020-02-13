package com.tk.center.admin.controller;

import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.service.MagicService;
import com.magic.utils.CryptUtils;
import com.magic.utils.JsonUtils;
import com.tk.center.admin.dto.*;
import com.tk.center.admin.dto.member.ApiFeedbackPersistReq;
import com.tk.center.admin.dto.member.ApiMemberPersistReq;
import com.tk.center.admin.dto.member.ApiMemberQueryReq;
import com.tk.center.admin.dto.member.ApiMemberFeedbackQueryReq;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.common.Constant;
import com.tk.center.dto.member.MemberQuery;
import com.tk.center.entity.member.Member;
import com.tk.center.entity.push.MemberFeedback;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.entity.types.FeedbackStatus;
import com.tk.center.entity.types.TerminalType;
import com.tk.center.service.*;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by Mingkun on 2020/02/05.
 */
@RestController
@RequestMapping("/admin/member")
public class MemberController {
    @Resource
    protected MemberService memberService;
    @Resource
    protected MagicService magicService;
    @Resource
    protected ConfigService configService;
    @Resource
    protected OperationService operationService;
    @Resource
    protected RecordService recordService;
    @Resource
    protected RealtimeStatService realtimeStatService;
    @Resource
    protected ResourceService resourceService;



    protected void blurMembers(List<Member> members, LoginUser loginUser) {
        boolean hidePhone = !loginUser.check("phone");
        boolean hideEmail = !loginUser.check("email");
        boolean hideRealName = !loginUser.check("realName");
        boolean hideNickName = !loginUser.check("nickName");
        boolean hideReferer = !loginUser.check("referer");
        boolean isRoot = loginUser.isRoot();
        for (Member member : members) {
            memberService.injectMember(member);
            if (!isRoot) {
                if (hidePhone) {
                    String phone = memberService.getHidePhone(member.getPhone());
                    member.setPhone(phone);
                }
                if (hideEmail) {
                    if (!StringUtils.isEmpty(member.getEmail())) {
                        member.setEmail("****");
                    }
                }
                if (hideRealName) {
                    if (!StringUtils.isEmpty(member.getRealName())) {
                        String xing = member.getRealName().substring(0, 1);
                        member.setRealName(xing + "**");
                    }
                }
                if (hideNickName) {
                    if (!StringUtils.isEmpty(member.getNickname())) {
                        member.setNickname("****");
                    }
                }
                if (hideReferer){
                    if (!StringUtils.isEmpty(member.getReferer())){
                        member.setReferer("****");
                    }
                }
            }
        }
    }

    @RequestMapping("upload_avatar.{ext}")
    public ApiResp uploadAvatar(@RequestParam("file") MultipartFile file, String memberId) throws IOException {
        Member member = memberService.getMember(memberId);
        if (member == null) {
            ApiErrorCode.PLAYER_NOT_FOUND.throwSelf();
        } else {
            String avatarPath = resourceService.getAvatarPath(memberId);
            resourceService.upload(avatarPath, file.getInputStream());
            String avatar = resourceService.getAvatarPathWithoutResource(memberId);

            member.setHeadUpdateTime(System.currentTimeMillis());
            member.setHead(avatar + "?v=" + member.getHeadUpdateTime());
            memberService.updateMemberAvatar(memberId, member);
        }
        return new ApiResp();
    }

    @RequestMapping("manual_add_member.{ext}")
    public ApiEntityPersistResp register(@RequestBody ApiMemberPersistReq req) {
        Member member = req.getModel();
        member.setUsername(member.getUsername().trim());
        if (member.getUsername().length() > 11) {
            ApiErrorCode.REGISTER_FAILED.build("用户名不能超过11位").throwSelf();
        }
        if (!member.getUsername().matches("^[0-9a-z_]+$")) {
            ApiErrorCode.REGISTER_FAILED.build("用户名只能小写字母和数字组成").throwSelf();
        }
        if (StringUtils.isEmpty(member.getUsername()) || StringUtils.isEmpty(member.getPassword())) {
            throw ApiErrorCode.REGISTER_FAILED.build("注册信息不全");
        }
        if (configService.getSysConfigAsBoolean(Constant.REGISTER_SHOW_PHONE) && (member.getPhone() == null || member.getPhone().length() < 10)) {
            ApiErrorCode.REGISTER_FAILED.build("手机号码不能为空").throwSelf();
        }
        if (configService.getSysConfigAsBoolean(Constant.REGISTER_SHOW_REALNAME) && (StringUtils.isEmpty(member.getRealName()))) {
            ApiErrorCode.REGISTER_FAILED.build("真实姓名不能为空").throwSelf();
        }

        Member existMember = memberService.getMemberByUserName(member.getUsername());
        if (existMember != null) {
            throw ApiErrorCode.REGISTER_FAILED.build("用户名已存在");
        }
        if (configService.getSysConfigAsBoolean(Constant.REGISTER_SHOW_PHONE)) {
            MemberQuery memberQuery = new MemberQuery();
            memberQuery.setPhone(member.getPhone());
            List<Member> members = memberService.getMembers(memberQuery);
            if (members != null && members.size() > 0) {
                ApiErrorCode.REGISTER_FAILED.build("该手机号已被注册，请更换其他手机号").throwSelf();
            }
        }

        member.setId(Member.createId(member.getUsername()));
        member.setPassword(CryptUtils.getMd5(member.getPassword()));
        if (StringUtils.isEmpty(member.getPayPassword())) {
            member.setPayPassword(member.getPassword());
        }else {
            member.setPayPassword(CryptUtils.getMd5(member.getPayPassword()));
        }
        member.setCreated(new Date());
        member.setRegisterIP(req.getIp());

        memberService.registerMember(member);
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        realtimeStatService.onMemberRegister(member.getId(), TerminalType.PC);
        operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(), member.getId(),
                "", JsonUtils.toJson2(member), "新增", "", req.getIp(), member.getClass()));
        return ret;
    }

    @RequestMapping("members.{ext}")
    public ApiPageResp getPage(@RequestBody ApiMemberQueryReq req) {
        MemberQuery memberQuery = checkMemberQuery(req.getQuery());
        Page<Member> page = memberService.getMembersWithPage(memberQuery);
        blurMembers(page.getContent(), req.getLoginUser());
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("member_counts.{ext}")
    public ApiStatusCountResp getPlayerCount(@RequestBody ApiMemberQueryReq req) {

        MemberQuery memberQuery = checkMemberQuery(req.getQuery());
        ApiStatusCountResp ret = new ApiStatusCountResp();
        ret.setCounts(memberService.getMemberCounts(memberQuery));
        return ret;
    }

    @RequestMapping("member.{ext}")
    public ApiEntityPersistResp getPlayer(@RequestBody ApiEntityDeleteReq req) {
        Member member = magicService.get(req.getId(), Member.class);
        memberService.injectMember(member);
        List<Member> players =new ArrayList<>();
        players.add(member);
        blurMembers(players, req.getLoginUser());
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(member);
        return ret;
    }
    @RequestMapping("member_list.{ext}")
    public ApiListResp getPlayerList(@RequestBody ApiMemberQueryReq req) {
        List<Member> players = memberService.getMembers(req.getQuery());
        blurMembers(players, req.getLoginUser());
        ApiListResp ret = new ApiListResp();
        ret.setList(players);
        return ret;
    }

    private MemberQuery checkMemberQuery(MemberQuery query){
        String memberId = query.getMemberId();
        if (!StringUtils.isEmpty(memberId)){
            String[] ids = memberId.trim().split(",");
            if (ids.length > 1){
                query.setIds(Arrays.asList(ids));
                query.setMemberId("");
            }
        }
        return query;
    }

    @RequestMapping("feedbacks.{ext}")
    public ApiPageResp feedbacks(@RequestBody ApiMemberFeedbackQueryReq req) {
        Page<MemberFeedback> page = magicService.getPage(req.getQuery(), MemberFeedback.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }


    @RequestMapping("feedback_handle.{ext}")
    public ApiEntityPersistResp handleFeedback(@RequestBody ApiFeedbackPersistReq req){
        MemberFeedback fb = req.getModel();
        fb.setStatus(FeedbackStatus.HANDLED);
        fb.setHandledTime(new Date());
        fb.setHandledUserId(req.getLoginUser().getUserId());
        fb.setIp(req.getIp());
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(magicService.persist(fb));
        return ret;
    }

    @RequestMapping("feedback_counts.{ext}")
    public ApiStatusCountResp getFeedBackCount(@RequestBody ApiMemberFeedbackQueryReq req) {
        FeedbackStatus[] ss = {FeedbackStatus.HANDLED, FeedbackStatus.UNHANDLED, null};
        ApiStatusCountResp ret = new ApiStatusCountResp();
        ret.setCounts(new HashMap<>());
        for (FeedbackStatus s : ss) {
            req.getQuery().setStatus(s);
            long count = magicService.count(req.getQuery(), MemberFeedback.class);
            String key = "";
            if (s != null) key = s.toString();
            ret.getCounts().put(key, count);
        }
        return ret;
    }

    @RequestMapping("kick_off.{ext}")
    public ApiResp kickOff(@RequestBody ApiEntityDeleteReq req){
        String id = req.getId();
        String ret = "将会员" + id + "强制下线";
        memberService.kickMemberOff(id);
        operationService.insertOperationLog(OperationLog.create(
                req.getLoginUser().getUserId(),
                req.getId(),
                "",
                "",
                ret,
                "",
                req.getIp(),
                Member.class
        ));
        return new ApiResp();
    }

    @RequestMapping("member_info_update.{ext}")
    public ApiEntityPersistResp updateMemberInfo(@RequestBody ApiMemberPersistReq req) {
        //String optLog = getUpdatePlayerInfoLog(req.getModel(), playerService.getPlayer(req.getModel().getId()));
        String optLog = "fix";
        memberService.updateMemberInfo(req.getModel(), req.getLoginUser());
        if (req.getModel().isDisabled() == true){
            String id = req.getModel().getId();
            memberService.kickMemberOff(id);
            optLog = optLog + "因禁用将会员" + id + "强制下线。";
        }
        operationService.insertOperationLog(OperationLog.create(
                req.getLoginUser().getUserId(),
                req.getModel().getId(),
                "",
                "",
                optLog,
                "",
                req.getIp(),
                Member.class
        ));
        ApiEntityPersistResp resp = new ApiEntityPersistResp();
        resp.setModel(req.getModel());
        return resp;
    }

}
