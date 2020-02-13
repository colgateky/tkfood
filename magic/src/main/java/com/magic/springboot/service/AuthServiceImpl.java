package com.magic.springboot.service;

import com.magic.springboot.auth.config.AuthConfig;
import com.magic.springboot.auth.config.Permission;
import com.magic.springboot.auth.config.PermissionGroup;
import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.dto.RoleQuery;
import com.magic.springboot.dto.UserQuery;
import com.magic.springboot.entity.Role;
import com.magic.springboot.entity.User;
import com.magic.utils.CryptUtils;
import com.mongodb.DBObject;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    protected DaoHelper daoHelper;

    @Resource
    protected AuthConfig authConfig;

    private BasicQuery buildUserQuery(UserQuery query) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        if (StringUtils.isEmpty(query.getUsername()) == false) {
            if (query.getExcludedUsernames() != null && query.getExcludedUsernames().size() > 0) {
                c.andOperator(new Criteria().and("username").regex(query.getUsername()),
                        new Criteria().and("username").nin(query.getExcludedUsernames()));
            } else {
                c.and("username").regex(query.getUsername());
            }
        } else if (query.getExcludedUsernames() != null && query.getExcludedUsernames().size() > 0) {
            c.and("username").nin(query.getExcludedUsernames());
        }
        if (StringUtils.isEmpty(query.getRealName()) == false) {
            c.and("realName").regex(query.getRealName());
        }
        if(StringUtils.isEmpty(query.getId()) == false){
            c.and("id").is(query.getId());
        }
        q.addCriteria(c);
        return q;
    }

    @Override
    public User getUser(UserQuery query) {
        BasicQuery q = buildUserQuery(query);
        return daoHelper.findOne(q, User.class);
    }

    @Override
    public Page<User> getUsersWithPage(UserQuery query) {
        BasicQuery q = buildUserQuery(query);
        DBObject sort = daoHelper.createObject();
        sort.put("created", -1);
        q.setSortObject(sort);
        Page<User> page = daoHelper.getPage(query, q, User.class);
        List<User> list = page.getContent();
        for (User user: list) {
            user.setPassword("");
        }
        return page;
    }

    @Override
    public List<User> getUsers(UserQuery query) {
        List<User> list = daoHelper.find(query, User.class);
        for(User user : list){
            user.setPassword("");
        }
        return list;
    }

    @Override
    public boolean removeUser(String id) {
        return daoHelper.removeById(id, User.class);
    }

    @Override
    public User saveUser(User user) {
        return daoHelper.save(user);
    }

    private BasicQuery buildRoleQuery(RoleQuery query) {
        BasicQuery q = daoHelper.createQuery();
        Criteria c = new Criteria();
        if (StringUtils.isEmpty(query.getName()) == false) {
            c.and("name").regex(query.getName());
        }
        if (query.getRoleIds() != null && query.getRoleIds().size() > 0) {
            c.and("id").in(query.getRoleIds());
        }
        q.addCriteria(c);
        return q;
    }

    @Override
    public Role getRole(RoleQuery query) {
        BasicQuery q = buildRoleQuery(query);
        return daoHelper.findOne(q, Role.class);
    }

    @Override
    public Page<Role> getRolesWithPage(RoleQuery query) {
        BasicQuery q = buildRoleQuery(query);
        return daoHelper.getPage(query, q, Role.class);
    }

    @Override
    public List<Role> getRoles(RoleQuery query) {
        BasicQuery q = buildRoleQuery(query);
        return daoHelper.find(q, Role.class);
    }

    @Override
    public boolean removeRole(String id) {
        return daoHelper.removeById(id, Role.class);
    }

    @Override
    public Role saveRole(Role role){
        return daoHelper.save(role);
    }

    @Override
    public User getOneUser(String userId) {
        return daoHelper.findById(userId, User.class);
    }

    @Override
    public List<PermissionGroup> getAllPermissions() {
        return authConfig.getGroups();
    }

    private LoginUser userToLoginUser(User user) {
        LoginUser ret = new LoginUser();
        ret.setUserId(user.getId());
        ret.setUsername(user.getUsername());
        ret.setResources(new HashSet<>());
        ret.setRoot(user.isRoot());
        ret.setRoleNames(new HashSet<>());
        Set<String> permissionIds = user.getPermissionIds();
        if (permissionIds == null) {
            permissionIds = new HashSet<>();
        }
        if (user.getRoleIds() != null && user.getRoleIds().size() > 0) {
            RoleQuery roleQuery = new RoleQuery();
            roleQuery.setRoleIds(user.getRoleIds());
            List<Role> roles = getRoles(roleQuery);
            for (Role role : roles) {
                if (role.getPermissionIds() != null) {
                    permissionIds.addAll(role.getPermissionIds());
                }
                ret.getRoleNames().add(role.getName());
            }
        }
        if (user.isRoot()) {
            ret.getRoleNames().add("超级管理员");
        }
        for (String permissionId : permissionIds) {
            Permission permission = authConfig.getPermissionMap().get(permissionId);
            if (permission != null) {
                ret.getResources().addAll(permission.getResources());
            }
        }
        return ret;
    }
    @Override
    public LoginUser auth(String username, String password) {
        UserQuery query = new UserQuery();
        query.setUsername(username);
        User user = this.getUser(query);
        if (user == null) {
            return null;
        }
        if (CryptUtils.getMd5(password).equals(user.getPassword()) == false) {
            return null;
        }
        LoginUser ret = userToLoginUser(user);
        return ret;
    }

    @Override
    public LoginUser getLoginUser(String userId) {
        User user = daoHelper.findById(userId, User.class);
        if (user == null) {
            return null;
        }
        LoginUser ret = userToLoginUser(user);
        return ret;
    }
}
