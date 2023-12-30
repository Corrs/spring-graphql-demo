package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeptVO {
    private Long id;
    private String name;
    private Long pid;
    private String pname;
    private Integer sort;
}
