package com.yanxuan88.australiacallcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysDept;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    List<DeptVO> selectDepts();
}
