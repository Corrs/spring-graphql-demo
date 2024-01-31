package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部门管理表实体类
 *
 * @author co
 * @since 2023-12-28 09:21:17
 */
@Data
@Accessors(chain = true)
@TableName("`sys_dept`")
@EqualsAndHashCode(callSuper = false)
public class SysDept extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 上级ID
     */
    private Long pid;
    /**
     * 所有上级ID，用逗号分开，最多4级
     */
    private String pids;
    /**
     * 部门名称，50字符
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
}
