package com.magic.springboot.auth.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by xm on 2017/5/19.
 */
public class AuthConfig {
    //编写查询公共方法
    public static ResultSet execute(Connection connection, String sql, Object[] params, ResultSet resultSet, PreparedStatement preparedStatement) throws SQLException {
        //预编译的sql，在后面直接执行就可以了
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject，占位符从1开始，但我们的数组是从0开始
            preparedStatement.setObject(i+1, params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //编写增删改公共方法
    public static int execute(Connection connection, String sql, Object[] params, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    //释放资源
    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        boolean flag = true;
        if (resultSet != null) {
            try {
                resultSet.close();
                //资源回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
                //资源回收
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                //资源回收
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }


    protected List<PermissionGroup> groups;

    public List<PermissionGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PermissionGroup> groups) {
        this.groups = groups;
        for (PermissionGroup group : groups) {
            appendPermissionGroup(group, new HashSet<>());
        }
    }

    private void appendPermissionGroup(PermissionGroup group, Set<String> bases) {
        List<Permission> basePermissions = new ArrayList<>();

        if (group.getPermissions() != null) {
            for (Permission permission : group.getPermissions()) {
                if (permission.isBase()) {
                    String[] rs = permission.getResources().toArray(new String[] {});
                    bases.addAll(permission.getResources());
                    if ("base".equals(permission.getId())) {
                        permission.setId(rs[0]);
                    }
                    basePermissions.add(permission);
                }
                permissionMap.put(permission.getId(), permission);
            }
        }
        if (group.getGroups() != null) {
            for (PermissionGroup g : group.getGroups()) {
                Set<String> basesIn = new HashSet<>();
                basesIn.addAll(bases);
                appendPermissionGroup(g, basesIn);
            }
        }
//        group.getPermissions().removeAll(basePermissions);
        for (Permission permission : group.getPermissions()) {
            permission.getResources().addAll(bases);
        }
    }

    private Map<String, Permission> permissionMap = new HashMap<>();

    public Map<String, Permission> getPermissionMap() {
        return permissionMap;
    }
}
