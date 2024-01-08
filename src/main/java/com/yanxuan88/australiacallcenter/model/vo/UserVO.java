package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserVO {
    private Long id;
    private Long roleId;
    private Long deptId;
    private String username;
    private String realName;
    private LocalDateTime createTime;
    private String email;
    private String mobile;
    private Integer gender;
    private Integer status;
}
