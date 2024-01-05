package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictDataQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDictData;
import com.yanxuan88.australiacallcenter.model.vo.DictDataVO;

import java.util.List;

public interface IDictDataService extends IService<SysDictData> {
    Page<DictDataVO> page(PageDTO p, DictDataQueryDTO query);
    boolean add(AddDictDataDTO dictData);

    boolean rem(List<Long> ids);

    boolean edit(EditDictDataDTO dictData);
}
