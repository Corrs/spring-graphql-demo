package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogLoginQueryDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String username;
    private Integer status;
    private Long current;
    private Long limit;
}
