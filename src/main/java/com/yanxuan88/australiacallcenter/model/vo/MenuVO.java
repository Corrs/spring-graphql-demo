package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuVO {
    private Long id;
    private String name;
    private Long parentId;
    private String url;
    private String perms;
    private String icon;
    private Integer type;
    private Integer sort;
}
