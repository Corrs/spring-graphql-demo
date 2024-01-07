package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditMenuDTO;
import com.yanxuan88.australiacallcenter.model.vo.MenuVO;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
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

/**
 * 菜单控制器
 *
 * @author co
 * @since 2024-01-05 10:41:41
 */
@Controller
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @Authenticated
    @QueryMapping
    public List<MenuVO> menus() {
        return menuService.menus(SecurityUtil.getUserLoginInfo().getUserId());
    }

    /**
     * 新增菜单
     *
     * @param menu 参数
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:menu:save')")
    @MutationMapping
    public boolean addMenu(@Argument @Valid AddMenuDTO menu) {
        return menuService.add(menu);
    }

    /**
     * 编辑菜单
     *
     * @param menu 菜单
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @MutationMapping
    public boolean editMenu(@Argument @Valid EditMenuDTO menu) {
        return menuService.edit(menu);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单id
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @MutationMapping
    public boolean remMenu(@Argument @Valid @NotNull(message = "菜单标识不能为空")
                           @Min(value = 1, message = "菜单不存在") Long id) {
        return menuService.rem(id);
    }
}
