package com.yanxuan88.australiacallcenter;

import com.yanxuan88.australiacallcenter.model.dto.AddDeptDTO;
import com.yanxuan88.australiacallcenter.service.IDeptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeptTest {
    @Autowired
    IDeptService deptService;

    @Test
    void testAddDept() {
        AddDeptDTO dept = new AddDeptDTO();
        dept.setName("技术部");
        dept.setPid(1L);
        deptService.addDept(dept);
    }

    @Test
    void testDepts() {
        System.out.println(deptService.depts());
    }

    @Test
    void testRemDept() {
        System.out.println(deptService.remDept(19L));
    }
}
