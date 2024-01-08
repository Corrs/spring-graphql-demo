package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogQueryDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
