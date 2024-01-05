package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

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

    /**
     * 新增菜单
     *
     * @param menu 参数
     * @return true/false
     */
    @Authenticated
    @MutationMapping
    public boolean addMenu(@Argument @Valid AddMenuDTO menu) {
        return menuService.add(menu);
    }


}
