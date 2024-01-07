package com.yanxuan88.australiacallcenter.event.model;

import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志事件
 *
 * @author co
 * @since 2024-01-05 16:24:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogEvent {
    private SysLogOperation operationLog;
}
