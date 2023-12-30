package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDeptMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDeptDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDept;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;
import com.yanxuan88.australiacallcenter.service.IDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements IDeptService {
    @Autowired
    private TransactionTemplate transactionTemplate;

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
        log.info("创建机构数据，参数={}", dept);
        // 1.
        Long pid = dept.getPid();
        String pids = "0";
        String pname = "";
        if (pid > 0) {
            SysDept pDept = getById(pid);
            if (pDept == null) {
                throw new BizException("上级机构不存在");
            }
            pids = pDept.getPids() + "," + pid;
            if (pids.split("\\s*,\\s*").length > 3) {
                throw new BizException("只允许创建3级机构");
            }
            pname = pDept.getName();
        }
        // 2.
        String name = dept.getName().trim();
        SysDept oDept = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, pid).eq(SysDept::getName, name).eq(SysDept::getIsDeleted, Boolean.FALSE), false);
        if (oDept != null) {
            throw new BizException("名称重复，请更换名称");
        }


        SysDept entity = new SysDept()
                .setName(name)
                .setPid(pid)
                .setPids(pids)
                .setSort(dept.getSort());

        if (!save(entity)) {
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

    /**
     * 删除机构
     * 1. 校验机构数据是否存在
     * 2. 校验是否有子机构
     *
     * @param id 机构id
     * @return true/false
     */
    @Override
    public synchronized boolean remDept(Long id) {
        log.info("删除机构数据，id={}", id);
        SysDept dept = getById(id);
        if (dept == null) {
            throw new BizException("机构数据不存在");
        }
        SysDept child = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, id).eq(SysDept::getIsDeleted, Boolean.FALSE), false);
        if (child != null) {
            throw new BizException("请先删除子机构");
        }
        return removeById(id);
    }

    /**
     * 编辑部门
     * 1. 校验机构数据是否存在
     * 2. 如果上级机构有变化且>0，校验上级机构是否存在；校验层级；校验是否有子机构
     * 3. 如果名称有变化，校验名称
     * 4. 修改数据
     *
     * @param dept 部门数据
     * @return DeptVO
     */
    @Override
    public synchronized DeptVO editDept(EditDeptDTO dept) {
        log.info("编辑机构数据，dept={}", dept);
        // 1.
        Long id = dept.getId();
        Long pid = dept.getPid();
        if (id.equals(pid)) {
            throw new BizException("上级机构与本机构相同");
        }
        SysDept entity = getById(id);
        if (entity == null) {
            throw new BizException("机构数据不存在");
        }
        // 2.
        String pids = entity.getPids();
        String pname = "";
        if (pid > 0) {
            SysDept pDept = getById(pid);
            if (pDept == null) {
                throw new BizException("上级机构不存在");
            }
            pids = pDept.getPids() + "," + pid;
            if (pids.split("\\s*,\\s*").length > 3) {
                throw new BizException("只允许创建3级机构");
            }
            pname = pDept.getName();
            // todo 暂时不允许修改有子机构的数据。
            SysDept child = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, id).eq(SysDept::getIsDeleted, Boolean.FALSE), false);
            if (child != null) {
                throw new BizException("拥有子机构，无法修改上级机构");
            }
        }

        String name = dept.getName().trim();
        if (!name.equals(entity.getName())) {
            SysDept oDept = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, pid).eq(SysDept::getName, name).eq(SysDept::getIsDeleted, Boolean.FALSE), false);
            if (oDept != null) {
                throw new BizException("名称重复，请更换名称");
            }
        }

        entity.setPid(pid).setPids(pids).setName(name).setSort(dept.getSort());
        if (!updateById(entity)) {
            throw new BizException("机构编辑失败");
        }
        return  new DeptVO().setId(id).setPid(pid).setName(name).setPname(pname).setSort(dept.getSort());
    }
}
