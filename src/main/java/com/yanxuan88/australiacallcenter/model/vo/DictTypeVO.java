package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DictTypeVO {
    private Long id;
    private String dictType;
    private String dictName;
    private String remark;
    private Integer sort;
    private LocalDateTime createTime;
}
