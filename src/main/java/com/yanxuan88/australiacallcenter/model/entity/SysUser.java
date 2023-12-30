package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;

@Data
@TableName("`sys_user`")
public class SysUser extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String email;
    private String mobile;
    private String password;
    private String username;
    private String salt;
    private Boolean status;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
