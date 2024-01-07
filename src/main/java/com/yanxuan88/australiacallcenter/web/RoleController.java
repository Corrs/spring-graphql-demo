package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.AddRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.AssignPermsDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.vo.RoleVO;
import com.yanxuan88.australiacallcenter.service.IRoleService;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 查询角色数据
     *
     * @param p        分页信息
     * @param roleName 角色名称
     * @return relay
     */
    @Authenticated
    @QueryMapping
    public Connection<RoleVO> roles(@Argument PageDTO p, @Argument String roleName) {
        return RelayUtil.build(roleService.page(p, roleName));
    }

    /**
     * 查询所有的角色
     * @return list
     */
    @Authenticated
    @QueryMapping
    public List<RoleVO> roleList() {
        return roleService.roleList();
    }
    /**
     * 新增角色
     *
     * @param role 角色
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:role:save')")
    @MutationMapping
    public boolean addRole(@Argument @Valid AddRoleDTO role) {
        return roleService.add(role);
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:role:update')")
    @MutationMapping
    public boolean editRole(@Argument @Valid EditRoleDTO role) {
        return roleService.edit(role);
    }

    /**
     * 删除角色
     *
     * @param id 角色标识
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @MutationMapping
    public boolean remRole(@Argument @Valid @NotNull(message = "角色标识不能为空")
                           @Min(value = 1, message = "角色不存在") Long id) {
        return roleService.rem(id);
    }

    /**
     * 分配权限
     *
     * @param perms 参数
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:role:perms')")
    @MutationMapping
    public boolean assignPerms(@Argument @Valid AssignPermsDTO perms) {
        return roleService.assignPerms(perms, SecurityUtil.getUserLoginInfo().getUserId());
    }

    /**
     * 查询角色分配的权限id集合
     *
     * @param roleId 角色id
     * @return [Long]
     */
    @Authenticated
    @QueryMapping
    public List<Long> rolePerms(@Argument @Valid @NotNull(message = "角色标识不能为空")
                                @Min(value = 1, message = "角色不存在") Long roleId) {
        return roleService.rolePerms(roleId);
    }
}
