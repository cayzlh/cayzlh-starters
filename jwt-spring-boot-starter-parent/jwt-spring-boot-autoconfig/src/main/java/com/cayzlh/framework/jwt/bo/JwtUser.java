package com.cayzlh.framework.jwt.bo;

import java.util.Date;
import java.util.List;
import org.assertj.core.util.Lists;

@Deprecated
public interface JwtUser {

    /**
     * 获取用户名
     */
    String getUsername();

    /**
     * 获取昵称
     */
    default String getNickname(){return "";}

    /**
     * 获取邮箱
     */
    default String getEmail(){return "";}

    /**
     * 获取手机号
     */
    default String getMobile(){return "";}

    /**
     * 获取最后重置密码的时间
     */
    Date getLastPasswordReset();

    /**
     * 获取用户角色列表
     */
    default List<String> getRoles() {
        return Lists.newArrayList();
    }

    /**
     * 获取用户权限列表
     */
    default List<String> getAuthority() {
        return Lists.newArrayList();
    }

}
