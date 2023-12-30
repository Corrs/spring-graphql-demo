package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDeptDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDept;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;

import java.util.List;

public interface IDeptService extends IService<SysDept> {
    DeptVO addDept(AddDeptDTO dept);

    List<DeptVO> depts();

    boolean remDept(Long id);

    DeptVO editDept(EditDeptDTO dept);
}
