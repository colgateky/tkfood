package com.tk.center.admin.controller;

import com.tk.center.common.ApiErrorCode;
import com.tk.center.controller.UserBaseController;
import com.tk.center.dto.request.ApiEntityDeleteReq;
import com.tk.center.dto.request.auth.ApiUserDeleteReq;
import com.tk.center.dto.request.auth.ApiUserPersistReq;
import com.tk.center.dto.response.ApiResp;
import com.tk.center.dto.response.auth.ApiUserPersistResp;
import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.auth.config.AuthConfig;
import com.magic.springboot.auth.config.Permission;
import com.magic.springboot.dto.RoleQuery;
import com.magic.springboot.dto.UserQuery;
import com.magic.springboot.entity.Role;
import com.magic.springboot.entity.User;
import com.magic.springboot.service.AuthService;
import com.magic.springboot.service.MagicService;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.service.OperationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Mingkun on 2020/02/04.
 */
@RestController
@RequestMapping("/admin/user/")
public class UserController extends UserBaseController {

    @Resource
    protected AuthService authService;
    @Resource
    protected OperationService operationService;
    @Resource
    protected MagicService magicService;
    @Resource
    protected AuthConfig authConfig;

    @RequestMapping("user_persist.{ext}")
    @AuthResource({"user_edit"})
    public ApiUserPersistResp persistUser(@RequestBody ApiUserPersistReq req) {
        ApiUserPersistResp ret = super.persistUser(req);
        User user = req.getUser();
        UserQuery q = new UserQuery();
        if (req.getUser().getId() != null) { //判断是否已存在此用户，旧用户-userId判断，新用户-username判断
            q.setId(user.getId());
        } else {
            q.setUsername(user.getUsername());
        }
        User result = authService.getUser(q); //result = 旧的

        String optUserLog = getUpdateUserInfoLog(user, result);
        operationService.insertOperationLog(OperationLog.create(
                req.getLoginUser().getUserId(),
                user.getUsername(),
                "",
                "",
                optUserLog,
                "",
                req.getIp(),
                User.class
        ));
        return ret;
    }

    private String getUpdateUserInfoLog(User after, User origin) {
        String userOperationLog;
        //先判断新增用户或编辑用户
        RoleQuery roleQuery = new RoleQuery(); //用於查询角色名称
        if (origin == null) {
            userOperationLog = "新增用户" + after.getUsername() + "信息:";
            userOperationLog = userOperationLog + "新增用户id：" + after.getId() + ";";
            userOperationLog = userOperationLog + "新增用户名：" + after.getUsername() + ";";
            userOperationLog = userOperationLog + (after.getUsername() != null ? "新增别名：" + after.getUsername() + ";" : "");
            if (after.getRoleIds() != null) {
                String roleSelect = "角色选择：";
                roleQuery.setRoleIds(after.getRoleIds());
                List<Role> roles = authService.getRoles(roleQuery);
                for (Role role : roles) {
                    roleSelect = roleSelect + role.getName() + ",";
                }
                roleSelect = roleSelect.substring(0, roleSelect.length() - 1);
                userOperationLog = userOperationLog + roleSelect + ";";
            }
            if (after.getPermissionIds() != null) {
                String afterPermissionDesc = "";
                for (String permissionId : after.getPermissionIds()) {
                    Permission permission = authConfig.getPermissionMap().get(permissionId);
                    afterPermissionDesc = afterPermissionDesc + permission.getName() + ",";
                }
                afterPermissionDesc = afterPermissionDesc.substring(0, afterPermissionDesc.length() - 1);
                userOperationLog = userOperationLog + "使用权限：" + afterPermissionDesc + ";";
            }
            userOperationLog = userOperationLog + (after.isRoot() ? "超级管理员状态：" + "启用" + ";" : "");
        } else {
            userOperationLog = "编辑用户" + after.getId() + "信息:";
            if (!origin.getPassword().equals(after.getPassword())) {
                userOperationLog = userOperationLog + "修改密码" + ";";
            }
            if (!origin.getUsername().equals(after.getUsername())) {
                userOperationLog = userOperationLog + "修改用户名：" + origin.getUsername() + ">==>" + after.getUsername() + ";";
            }
            if (origin.getRealName() == null) {
                userOperationLog = userOperationLog + "修改别名：" + "无别名" + ">==>" + after.getRealName() + ";";
            } else {
                if (!origin.getRealName().equals(after.getRealName())) {
                    userOperationLog = userOperationLog + "修改别名：" + origin.getRealName() + ">==>" + after.getRealName() + ";";
                }
            }
            if (origin.getRoleIds() != after.getRoleIds()) {
                String originRole = "", afterRole = "";
                if (origin.getRoleIds() != null) {
                    roleQuery.setRoleIds(origin.getRoleIds());
                    List<Role> originRoles = authService.getRoles(roleQuery);
                    for (Role role : originRoles) {
                        originRole = originRole + role.getName() + ",";
                    }
                    originRole = originRole.substring(0, originRole.length() - 1);
                }
                if (after.getRoleIds() != null) {
                    roleQuery.setRoleIds(after.getRoleIds());
                    List<Role> afterRoles = authService.getRoles(roleQuery);
                    for (Role role : afterRoles) {
                        afterRole = afterRole + role.getName() + ",";
                    }
                    afterRole = afterRole.substring(0, afterRole.length() - 1);
                }
                userOperationLog = userOperationLog + "修改角色选择：" + (origin.getRoleIds() != null ? originRole : "无角色") + ">==>" + (after.getRoleIds() != null ? afterRole : "无角色") + ";";
            }
            if (origin.getPermissionIds() != after.getPermissionIds()) {
                String originPermission = "", afterPermission = "";
                if (origin.getPermissionIds() != null) {
                    for (String permissionId : origin.getPermissionIds()) {
                        Permission permission = authConfig.getPermissionMap().get(permissionId);
                        originPermission = originPermission + permission.getName() + ",";
                    }
                    originPermission = originPermission.substring(0, originPermission.length() - 1);
                }
                if (after.getPermissionIds() != null) {
                    for (String permissionId : after.getPermissionIds()) {
                        Permission permission = authConfig.getPermissionMap().get(permissionId);
                        afterPermission = afterPermission + permission.getName() + ",";
                    }
                    afterPermission = afterPermission.substring(0, afterPermission.length() - 1);
                }
                userOperationLog = userOperationLog + "修改权限：" + (origin.getPermissionIds() != null ? originPermission : "无权限") + ">==>"
                        + (after.getPermissionIds() != null ? afterPermission : "无权限") + ";";
            }
            if (origin.isRoot() != after.isRoot()) {
                userOperationLog = userOperationLog + "超级管理员状态：" + (origin.isRoot() ? "启用>==>禁用;" : "禁用>==>启用;");
            }
        }
        return userOperationLog;
    }

    @RequestMapping("user_delete.{ext}")
    @AuthResource("user_delete")
    public ApiResp deleteUser(@RequestBody ApiUserDeleteReq req) {
        if (!(req.getLoginUser() != null && req.getLoginUser().isRoot())) {
            ApiErrorCode.PERSIST_USER_FAILED.build("权限不足").throwSelf();
        }

        String id = req.getId();
        User user = magicService.get(id, User.class);
        String deleteDesc = "将用户id" + user.getId() + ", 名称" + user.getUsername() + "删除;";
        operationService.insertOperationLog(OperationLog.create(
                req.getLoginUser().getUserId(),
                user.getUsername(),
                "",
                "",
                deleteDesc,
                "",
                req.getIp(),
                User.class
        ));
        super.deleteUser(req);
        return new ApiResp();
    }

    @RequestMapping("kick_off.{ext}")
    public ApiResp kickOff(@RequestBody ApiEntityDeleteReq req) {
        super.kickOff(req);

        String id = req.getId();
        User user = magicService.get(id, User.class);
        String ret = "将用户" + user.getUsername() + "强制下线;";
        operationService.insertOperationLog(OperationLog.create(
                req.getLoginUser().getUserId(),
                user.getUsername(),
                "",
                "",
                ret,
                "",
                req.getIp(),
                User.class
        ));
        return new ApiResp();
    }

}
