package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 登录成功 返回给前端的VO
 *
 * @author co
 * @since 2023-12-27 14:46:04
 */
@Data
@Accessors(chain = true)
public class UserLoginInfoVO implements Serializable {
    private String authenticationToken;
    private UserBaseVO user;
    private List<UserPermissionVO> permissions;
}
