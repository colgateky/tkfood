package com.magic.springboot.service;

import com.magic.springboot.auth.config.PermissionGroup;
import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.dto.RoleQuery;
import com.magic.springboot.dto.UserQuery;
import com.magic.springboot.entity.Role;
import com.magic.springboot.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by xm on 2017/5/19.
 */
public interface AuthService {
    User getUser(UserQuery query);
    Page<User> getUsersWithPage(UserQuery query);
    List<User> getUsers(UserQuery query);
    boolean removeUser(String id);
    User saveUser(User user);
    Role getRole(RoleQuery query);
    Page<Role> getRolesWithPage(RoleQuery query);
    List<Role> getRoles(RoleQuery query);
    boolean removeRole(String id);
    List<PermissionGroup> getAllPermissions();
    LoginUser auth(String username, String password);
    LoginUser getLoginUser(String userId);
    Role saveRole(Role role);
    User getOneUser(String userId);
}
