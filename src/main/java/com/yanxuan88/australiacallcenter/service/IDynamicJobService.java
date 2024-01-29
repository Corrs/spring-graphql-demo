package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.DynamicJobQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDynamicJob;
import com.yanxuan88.australiacallcenter.model.vo.DynamicJobVO;

public interface IDynamicJobService extends IService<SysDynamicJob> {
    boolean rem(Long id);

    boolean add(AddDynamicJobDTO job);

    boolean edit(EditDynamicJobDTO job);

    boolean switchJob(Long id);

    Page<DynamicJobVO> jobs(PageDTO page, DynamicJobQueryDTO query);
}
