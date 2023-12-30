package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户基础信息
 *
 * @author co
 * @since 2023-12-29 09:06:54
 */
@Data
@Accessors(chain = true)
public class UserBaseVO implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private String mobile;
}
