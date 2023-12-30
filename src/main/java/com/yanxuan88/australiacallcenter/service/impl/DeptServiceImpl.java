package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDeptMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDept;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;
import com.yanxuan88.australiacallcenter.service.IDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements IDeptService {
    /**
     * 创建部门，暂时先不加分布式锁
     * 1. 生成pids，校验部门层级
     * 2. 校验pid下是否已经有相同名称的部门，名称重复提示错误信息
     * 3. 保存
     *
     * @param dept 部门数据
     * @return true/false
     */
    @Override
    public synchronized DeptVO addDept(AddDeptDTO dept) {
        log.info("创建部门，参数={}", dept);
        // 1.
        Long pid = dept.getPid();
        String pids = "0";
        String pname = "";
        if (pid > 0) {
            SysDept pDept = getById(pid);
            pids = pDept.getPids() + "," + pid;
            if (pids.split("\\s*,\\s*").length > 3) {
                throw new BizException("只允许创建3级机构");
            }
            pname = pDept.getName();
        }
        // 2.
        String name = dept.getName().trim();
        SysDept oDept = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, pid).eq(SysDept::getName, name), false);
        if (oDept != null) {
            throw new BizException("名称重复，请更换名称");
        }


        SysDept entity = new SysDept()
                .setName(name)
                .setPid(pid)
                .setPids(pids)
                .setSort(dept.getSort());
        save(entity);
        if (entity.getId() == null) {
            throw new BizException("机构创建失败");
        }
        return new DeptVO()
                .setId(entity.getId())
                .setPname(pname)
                .setName(name)
                .setSort(dept.getSort())
                .setPid(pid);
    }

    @Override
    public List<DeptVO> depts() {
        log.info("查询机构数据");
        return baseMapper.selectDepts();
    }

    @Override
    public synchronized boolean remDept(Long id) {
        log.info("删除机构数据");
        SysDept dept = getById(id);
        if (dept == null) {
            throw new BizException("机构数据不存在");
        }

        return removeById(id);
    }
}
