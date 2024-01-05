package com.yanxuan88.australiacallcenter.event.model;

import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLoginLogEvent {
    private SysLogLogin loginLog;
}
