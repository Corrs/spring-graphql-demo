package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("`sys_role_menu`")
public class SysRoleMenu {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long roleId;
    private Long menuId;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
