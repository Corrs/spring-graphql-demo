package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictTypeQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDictType;
import com.yanxuan88.australiacallcenter.model.vo.DictTypeVO;

public interface IDictTypeService extends IService<SysDictType> {
    Page<DictTypeVO> page(PageDTO page, DictTypeQueryDTO query);
    boolean add(AddDictTypeDTO dictType);

    boolean edit(EditDictTypeDTO dictType);

    boolean rem(Long id);
}
