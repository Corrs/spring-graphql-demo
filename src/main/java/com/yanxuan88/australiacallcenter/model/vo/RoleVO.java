package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class RoleVO {
    private Long id;
    private String roleName;
    private String remark;
    private LocalDateTime createTime;
}
