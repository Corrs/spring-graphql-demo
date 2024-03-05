package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.mysql.JdbcLockClient;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDeptMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDeptDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDept;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;
import com.yanxuan88.australiacallcenter.service.IDeptService;
import com.yanxuan88.australiacallcenter.service.IUserService;
import graphql.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yanxuan88.australiacallcenter.common.Constant.COMMA_SPLIT_REG;

@Service
public class DeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements IDeptService {
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IUserService userService;
    @Autowired
    private JdbcLockClient jdbcLockClient;
    private static final String LOCK_KEY = "dept";

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
    @SysLog("新增机构")
    public boolean addDept(AddDeptDTO dept) {
        return jdbcLockClient.doWithLock(LOCK_KEY, lock -> {
            // 1.
            Long pid = dept.getPid();
            String pids = "0";
            if (pid > 0) {
                SysDept pDept = getById(pid);
                if (pDept == null) {
                    throw new BizException("上级机构不存在");
                }
                pids = pDept.getPids() + "," + pid;
                if (pids.split(COMMA_SPLIT_REG).length > 3) {
                    throw new BizException("只允许创建3级机构");
                }
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
            return true;
        });
    }

    @Override
    public List<DeptVO> depts() {
        return baseMapper.selectDepts();
    }

    /**
     * 删除机构
     * 1. 校验机构数据是否存在
     * 2. 校验是否有子机构
     * 3. 校验是否有用户
     *
     * @param id 机构id
     * @return true/false
     */
    @Override
    @SysLog("删除机构")
    public boolean remDept(Long id) {
        return jdbcLockClient.doWithLock(LOCK_KEY, lock -> {
            SysDept dept = getById(id);
            if (dept == null) {
                throw new BizException("机构数据不存在");
            }
            SysDept child = getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, id).eq(SysDept::getIsDeleted, Boolean.FALSE), false);
            if (child != null) {
                throw new BizException("请先删除子机构");
            }
            // 判断部门下是否有用户
            List<SysUser> users = userService.queryByDept(id);
            if (!CollectionUtils.isEmpty(users)) {
                throw new BizException("请先移除部门下的用户数据");
            }

            return removeById(id);
        });
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
    @SysLog("编辑机构")
    public boolean editDept(EditDeptDTO dept) {
        return jdbcLockClient.doWithLock(LOCK_KEY, lock -> {
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
            String pids = "0";
            if (pid > 0) {
                SysDept pDept = getById(pid);
                if (pDept == null) {
                    throw new BizException("上级机构不存在");
                }
                pids = pDept.getPids() + "," + pid;
            }

            String childrenPidsPrefix = entity.getPids() + "," + id;
            List<SysDept> list = new ArrayList<>();
            if (!Objects.equals(pid, entity.getPid())) {
                String[] pidList = pids.split(COMMA_SPLIT_REG);
                if (Sets.newHashSet(pidList).contains("" + id)) {
                    throw new BizException("上级机构不能为下级机构");
                }
                if (pidList.length > 3) {
                    throw new BizException("机构层级超过3级");
                }
                // 上级机构改变
                list.addAll(baseMapper.selectSubDepts(id));
                if (list.size() > 0) {
                    int maxChildrenLevel = list.stream().map(SysDept::getPids).mapToInt(e -> e.substring(childrenPidsPrefix.length()).split(COMMA_SPLIT_REG).length).max().orElse(0);
                    if (pids.split(COMMA_SPLIT_REG).length + maxChildrenLevel > 3) {
                        throw new BizException("子机构层级超过3级");
                    }
                    String replace = pids + ",";
                    list.forEach(e -> e.setPids(e.getPids().replace(entity.getPids() + ",", replace)));
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
            list.add(entity);
            if (!updateBatchById(list)) {
                throw new BizException("机构编辑失败");
            }
            return true;
        });
    }
}
