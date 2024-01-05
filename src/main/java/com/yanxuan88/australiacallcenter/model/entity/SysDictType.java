package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典类型
 *
 * @author co
 * @since 2024-01-03 15:06:46
 */
@Data
@TableName("`sys_dict_type`")
public class SysDictType extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 字典类型，唯一
     */
    private String dictType;
    private String dictName;
    private String remark;
    private Integer sort;
}
