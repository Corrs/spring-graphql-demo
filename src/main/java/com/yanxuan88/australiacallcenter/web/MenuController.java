package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

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



}
