package com.tk.center.controller;

import com.tk.center.common.ApiErrorCode;
import com.tk.center.dto.request.*;
import com.tk.center.dto.request.auth.*;
import com.tk.center.dto.response.*;
import com.tk.center.dto.response.auth.*;
import com.tk.center.entity.record.LoginRecord;
import com.tk.center.framework.TokenHelper;
import com.tk.center.util.IpHelper;
import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.auth.annotation.NoAuth;
import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.dto.RoleQuery;
import com.magic.springboot.dto.UserQuery;
import com.magic.springboot.entity.Role;
import com.magic.springboot.entity.User;
import com.magic.springboot.service.AuthService;
import com.magic.springboot.service.MagicService;
import com.magic.springboot.service.RedisCacheService;
import com.magic.utils.CryptUtils;
import com.magic.utils.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * Created by xm on 2017/5/19.
 */
public class UserBaseController {

    @Resource
    protected AuthService authService;
    @Resource
    protected RedisCacheService redisCacheService;
    @Resource
    protected MagicService magicService;

    @AuthResource("auth")
    @RequestMapping("permissions.{ext}")
    public ApiPermissionQueryResp getAllPermissions(@RequestBody ApiPermissionQueryReq req) {
        ApiPermissionQueryResp ret = new ApiPermissionQueryResp();
        ret.setPgs(authService.getAllPermissions());
        return ret;
    }

    @RequestMapping("init.{ext}")
    @NoAuth
    public ApiResp init() {
        User user = new User();
        user.setId("root");
        user.setUsername("root");
        user.setRealName("超级管理员");
        user.setRoot(true);
        user.setCreated(new Date());
        user.setPassword(CryptUtils.getMd5("suadmin123!@#"));
        authService.saveUser(user);
        return new ApiResp();
    }

    @RequestMapping("login.{ext}")
    @NoAuth
    public ApiLoginResp login(@RequestBody ApiLoginReq req, HttpServletRequest request, HttpServletResponse response) {
        LoginUser user = authService.auth(req.getUsername(), req.getPassword());
        if (user == null) {
            ApiErrorCode.LOGIN_FAILED.build("用户名或密码错误").throwSelf();
        }
        LoginRecord record = new LoginRecord();
        record.setId(UUIDGenerator.generate());
        record.setUserName(user.getUsername());
        record.setCreated(new Date());
        record.setIp(IpHelper.getIp(request));
        magicService.persist(record);

        user.setUuid(req.getUuid());
        ApiLoginResp ret = new ApiLoginResp();
        ret.setUserId(user.getUserId());
        ret.setRoot(user.isRoot());
        ret.setUsername(user.getUsername());
        ret.setRoleNames(user.getRoleNames());
        ret.setResources(user.getResources());
        redisCacheService.hashSet("model", user.getUserId(), user);

        int hour = 2;
        if ("root".equals(ret.getUsername())){
            hour = 24;
        }
        Cookie c = new Cookie("token", TokenHelper.generateToken(user.getUserId()));
        c.setMaxAge(hour * 60 * 60);
        c.setPath("/");
        response.addCookie(c);
        return ret;
    }

    @RequestMapping("users.{ext}")
    @AuthResource("user")
    public ApiUserQueryResp getUsers(@RequestBody ApiUserQueryReq req) {
        Page<User> page = authService.getUsersWithPage(req.getQuery());
        ApiUserQueryResp ret = new ApiUserQueryResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("all_users.{ext}")
    public ApiListResp getAllUsers(@RequestBody ApiUserQueryReq req) {
        List<User> list = authService.getUsers(req.getQuery());
        ApiListResp ret = new ApiListResp();
        ret.setList(list);
        return ret;
    }

    @RequestMapping("user_persist.{ext}")
    @AuthResource({"user_edit"})
    public ApiUserPersistResp persistUser(@RequestBody ApiUserPersistReq req){
        User user = req.getUser();
        if(user == null || StringUtils.isEmpty(user.getUsername())){
            ApiErrorCode.PERSIST_USER_FAILED.build("用户信息缺失，更新用户信息失败").throwSelf();
        }
        UserQuery q = new UserQuery();
        q.setUsername(user.getUsername());
        User result = authService.getUser(q);
        if(StringUtils.isEmpty(user.getId())){
            if(result != null){
                ApiErrorCode.PERSIST_USER_FAILED.build("用户名已存在").throwSelf();
            }
            user.setId(UUIDGenerator.generate());
            user.setPassword(CryptUtils.getMd5(user.getPassword()));
        }else{
            if (result != null && !user.getId().equals(result.getId())){
                ApiErrorCode.PERSIST_USER_FAILED.build("用户名已存在").throwSelf();
            }
            if(StringUtils.isEmpty(user.getPassword())){
                q = new UserQuery();
                q.setId(user.getId());
                User old = authService.getUser(q);
                user.setPassword(old.getPassword());
            }else{
                user.setPassword(CryptUtils.getMd5(user.getPassword()));
            }
        }
        if(user.getCreated() == null){
            user.setCreated(new Date());
        }
        ApiUserPersistResp ret = new ApiUserPersistResp();
        ret.setModel(authService.saveUser(user));
        return ret;
    }

    @RequestMapping("user_delete.{ext}")
    @AuthResource("user_delete")
    public ApiResp deleteUser(@RequestBody ApiUserDeleteReq req){
        String id = req.getId();
        if(StringUtils.isEmpty(id)){
            ApiErrorCode.PERSIST_USER_FAILED.build("删除用户信息缺失").throwSelf();
        }
        authService.removeUser(id);
        ApiResp ret = new ApiResp();
        return ret;
    }


    @RequestMapping("roles.{ext}")
    @AuthResource("role")
    public ApiRoleQueryResp getRoles(@RequestBody ApiRoleQueryReq req){
        Page<Role> page = authService.getRolesWithPage(req.getQuery());
        ApiRoleQueryResp ret = new ApiRoleQueryResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("all_roles.{ext}")
    @AuthResource("role")
    public ApiAllRolesResp getAllRoles(@RequestBody ApiAllRolesReq req){
        ApiAllRolesResp ret = new ApiAllRolesResp();
        ret.setList(authService.getRoles(req.getQuery()));
        return ret;
    }

    @RequestMapping("role_persist.{ext}")
    @AuthResource("role_edit")
    public ApiRolePersistResp persistRole(@RequestBody ApiRolePersistReq req){
        Role role = req.getRole();
        if(role == null || StringUtils.isEmpty(role.getName())){
            ApiErrorCode.PERSIST_ROLE_FAILED.build("角色信息缺失，更新角色信息失败").throwSelf();
        }
        RoleQuery q = new RoleQuery();
        q.setName(role.getName());
        Role result = authService.getRole(q);
        if(StringUtils.isEmpty(role.getId())){
            if(result != null){
                ApiErrorCode.PERSIST_ROLE_FAILED.build("角色名称已存在").throwSelf();
            }
            role.setId(UUIDGenerator.generate());
        }else{
            if (result != null && !role.getId().equals(result.getId())){
                ApiErrorCode.PERSIST_ROLE_FAILED.build("角色名称已存在").throwSelf();
            }
        }
        if(role.getCreated() == null){
            role.setCreated(new Date());
        }
        ApiRolePersistResp ret = new ApiRolePersistResp();
        ret.setModel(authService.saveRole(role));
        return ret;
    }

    @RequestMapping("role_delete.{ext}")
    @AuthResource("role_delete")
    public ApiRoleDeleteResp deleteRole(@RequestBody ApiRoleDeleteReq req){
        String id = req.getId();
        if(StringUtils.isEmpty(id)){
            ApiErrorCode.PERSIST_ROLE_FAILED.build("删除角色的信息缺失").throwSelf();
        }
        authService.removeRole(id);
        ApiRoleDeleteResp ret = new ApiRoleDeleteResp();
        return ret;
    }

    @RequestMapping("change_password.{ext}")
    public ApiChangePasswordResp changePassword (@RequestBody ApiChangePasswordReq req){
        String userId = req.getUserId();
        if(userId == null){
            ApiErrorCode.PERSIST_USER_FAILED.build("用户ID不存在").throwSelf();
        }
        String oldPassword = CryptUtils.getMd5(req.getOldPassword());
        String newPassword = CryptUtils.getMd5(req.getNewPassword());
        UserQuery q = new UserQuery();
        q.setId(userId);
        User user = authService.getUser(q);
        if(user == null){
            ApiErrorCode.PERSIST_USER_FAILED.build("用户不存在").throwSelf();
        }
        String password = user.getPassword();
        if(!oldPassword.equals(password)){
            ApiErrorCode.PERSIST_USER_FAILED.build("原密码不正确").throwSelf();
        }
        user.setPassword(newPassword);
        authService.saveUser(user);
        return new ApiChangePasswordResp();
    }

    @RequestMapping("kick_off.{ext}")
    public ApiResp kickOff(@RequestBody ApiEntityDeleteReq req){
        String id = req.getId();
        redisCacheService.hashDel("model", id);
        return new ApiResp();
    }

}
