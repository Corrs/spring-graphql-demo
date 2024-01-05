package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDeptDTO;
import com.yanxuan88.australiacallcenter.model.vo.DeptVO;
import com.yanxuan88.australiacallcenter.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 部门管理 控制器
 *
 * @author co
 * @since 2023-12-28 10:12:27
 */
@Controller
public class DeptController {
    @Autowired
    private IDeptService deptService;

    /**
     * 新增部门数据
     *
     * @param dept 部门数据
     * @return DeptVO
     */
    @Authenticated
    @MutationMapping
    public DeptVO addDept(@Argument @Valid AddDeptDTO dept) {
        return deptService.addDept(dept);
    }

    /**
     * 查询部门列表
     *
     * @return list
     */
    @Authenticated
    @QueryMapping
    public List<DeptVO> depts() {
        return deptService.depts();
    }

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return true/false
     */
    @Authenticated
    @MutationMapping
    public boolean remDept(@Argument @Valid @Min(value = 1L, message = "机构数据不存在") Long id) {
        return deptService.remDept(id);
    }

    /**
     * 编辑部门
     *
     * @param dept 部门数据
     * @return DeptVO
     */
    @Authenticated
    @MutationMapping
    public DeptVO editDept(@Argument @Valid EditDeptDTO dept) {
        return deptService.editDept(dept);
    }

}
