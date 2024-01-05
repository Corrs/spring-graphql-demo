package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DictDataVO {
    private Long id;
    private String dictValue;
    private String dictLabel;
    private String remark;
    private Integer sort;
    private Long dictTypeId;
    private LocalDateTime createTime;
}
