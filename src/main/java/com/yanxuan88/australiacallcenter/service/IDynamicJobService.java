package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.entity.SysDynamicJob;

public interface IDynamicJobService extends IService<SysDynamicJob> {
    boolean rem(Long id);

    boolean pause(Long id);

    boolean resume(Long id);
}
