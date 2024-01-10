package com.yanxuan88.australiacallcenter.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutEvent {
    private String sessionCacheKey;
}
