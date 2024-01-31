package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanxuan88.australiacallcenter.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据
 *
 * @author co
 * @since 2024-01-03 15:09:37
 */
@Data
@TableName("`sys_dict_data`")
@EqualsAndHashCode(callSuper = false)
public class SysDictData extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 外键，关联sys_dict_type的id
     */
    private Long dictTypeId;
    private String dictLabel;
    private String dictValue;
    private String remark;
    private Integer sort;
}
