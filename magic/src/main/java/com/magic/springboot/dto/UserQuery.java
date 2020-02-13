package com.magic.springboot.dto;

import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
public class UserQuery extends PageQuery {
    protected String username;
    protected String realName;
    protected String id;
    protected Set<String> excludedUsernames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Set<String> getExcludedUsernames() {
        return excludedUsernames;
    }

    public void setExcludedUsernames(Set<String> excludedUsernames) {
        this.excludedUsernames = excludedUsernames;
    }
}
