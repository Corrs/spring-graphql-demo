package com.yanxuan88.australiacallcenter.model.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PageDTO {
    private Long current;
    private Long limit;
    private String column;
    private Boolean asc;

    public <T> Page<T> page() {
        Page<T> page = new Page<>(current, limit);
        if (column!=null && column.trim().length()>0) {
            OrderItem orderItem = Boolean.TRUE.equals(asc) ? OrderItem.asc(column) : OrderItem.desc(column);
            page.addOrder(orderItem);
        }
        // tip mybatis-plus中存在bug，一直报 optimize this sql to a count sql has exception 警告
        page.setOptimizeCountSql(false);
        return page;
    }
}
