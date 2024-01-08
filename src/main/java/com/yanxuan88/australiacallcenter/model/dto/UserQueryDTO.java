package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserQueryDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String username;
    private Long deptId;
    private Integer gender;
}
