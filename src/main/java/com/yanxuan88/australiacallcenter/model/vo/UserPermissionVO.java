package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户权限VO
 *
 * @author co
 * @since 2023-12-29 09:07:40
 */
@Data
@Accessors(chain = true)
public class UserPermissionVO implements Serializable {
    private String url;
    private String perms;
    private Long id;
    private Long parentId;
    private String icon;
    private Integer type;
    private Integer sort;
    private String name;
}
