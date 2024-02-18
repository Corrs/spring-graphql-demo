package com.yanxuan88.australiacallcenter.common;

import java.io.Serializable;
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
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 所属机构id
     */
    private Long deptId;

    /**
     * 是否吵架管理员
     */
    private Integer superAdmin;

    /**
     * 用户登录redis缓存key
     */
    private String sessionCacheKey;

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

    public String getSessionCacheKey() {
        return sessionCacheKey;
    }

    public void setSessionCacheKey(String sessionCacheKey) {
        this.sessionCacheKey = sessionCacheKey;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Integer superAdmin) {
        this.superAdmin = superAdmin;
    }
}
