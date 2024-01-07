package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("`sys_user`")
public class SysUser extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String email;
    private String mobile;
    private String password;
    private String username;
    private String realName;
    private String avatar;
    private Long deptId;
    private Integer superAdmin;
    private String salt;
    private Integer status;
    private Integer gender;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
