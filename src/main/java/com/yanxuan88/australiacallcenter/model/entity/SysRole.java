package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统角色
 *
 * @author co
 * @since 2024-01-06 20:00:00
 */
@Data
@Accessors(chain = true)
@TableName("`sys_role`")
public class SysRole extends BaseEntity {
    @TableId(type = IdType.AUTO, value = "role_id")
    private Long id;
    private String roleName;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
