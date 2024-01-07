package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("`sys_user_role`")
public class SysUserRole extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long roleId;
}
