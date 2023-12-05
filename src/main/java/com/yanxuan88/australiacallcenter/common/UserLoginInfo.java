package com.yanxuan88.australiacallcenter.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录信息
 *
 * @author co
 * @since 2023/11/30 下午3:11:31
 */
public class UserLoginInfo implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String username;

    /**
     * 角色id
     */
    private List<Long> roleIds;

    /**
     * 角色名
     */
    private List<String> roleNames;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 系统
     */
    private Boolean status;

    /**
     * 密码
     */
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
