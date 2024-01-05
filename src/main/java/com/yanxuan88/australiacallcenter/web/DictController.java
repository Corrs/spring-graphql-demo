package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.config.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.AddDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictTypeQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.vo.DictTypeVO;
import com.yanxuan88.australiacallcenter.service.IDictTypeService;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * 字典控制器
 *
 * @author co
 * @since 2024-01-03 15:31:34
 */
@Controller
public class DictController {
    @Autowired
    private IDictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     *
     * @param p     分页信息
     * @param query 查询条件
     * @return relay
     */
    @Authenticated
    @QueryMapping
    public Connection<DictTypeVO> dictTypes(@Argument PageDTO p, @Argument DictTypeQueryDTO query) {
        return RelayUtil.build(dictTypeService.page(p, query));
    }

    /**
     * 新增字典
     *
     * @param dictType 参数
     * @return true/false
     */
    @Authenticated
    @MutationMapping
    public boolean addDictType(@Valid @Argument AddDictTypeDTO dictType) {
        return dictTypeService.add(dictType);
    }

    /**
     * 编辑字典
     *
     * @param dictType 参数
     * @return true/false
     */
    @Authenticated
    @MutationMapping
    public boolean editDictType(@Valid @Argument EditDictTypeDTO dictType) {
        return dictTypeService.edit(dictType);
    }

    /**
     * 删除字典
     *
     * @param id 字典标识
     * @return true/false
     */
    @Authenticated
    @MutationMapping
    public boolean remDictType(@Argument Long id) {
        return dictTypeService.rem(id);
    }
}
