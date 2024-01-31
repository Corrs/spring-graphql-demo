package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统菜单
 *
 * @author co
 * @since 2024-01-05 10:24:53
 */
@Data
@Accessors(chain = true)
@TableName("`sys_menu`")
@EqualsAndHashCode(callSuper = false)
public class SysMenu extends BaseEntity {
    @TableId(type = IdType.AUTO, value = "menu_id")
    private Long id;
    private Long parentId;
    private String name;
    private String url;
    private String perms;
    private Integer type;
    private String icon;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
