package com.yanxuan88.australiacallcenter;

import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuTest {
    @Autowired
    IMenuService menuService;
    @Test
    void testAdd() {
        AddMenuDTO menu = new AddMenuDTO();
        menu.setName("111");
        menuService.add(menu);
    }
}
