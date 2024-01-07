package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.AddDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictDataQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.vo.DictDataVO;
import com.yanxuan88.australiacallcenter.service.IDictDataService;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 字典数据控制器
 *
 * @author co
 * @since 2024-01-04 13:48:30
 */
@Controller
public class DictDataController {
    @Autowired
    private IDictDataService dictDataService;

    /**
     * 字典数据分页查询
     *
     * @param p     分页
     * @param query 条件
     * @return relay
     */
    @Authenticated
    @QueryMapping
    public Connection<DictDataVO> dictDatas(@Argument PageDTO p, @Argument DictDataQueryDTO query) {
        return RelayUtil.build(dictDataService.page(p, query));
    }

    /**
     * 新增字典数据
     *
     * @param dictData 参数
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:dictdata:save')")
    @MutationMapping
    public boolean addDictData(@Argument @Valid AddDictDataDTO dictData) {
        return dictDataService.add(dictData);
    }

    /**
     * 编辑字典数据
     *
     * @param dictData 参数
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:dictdata:update')")
    @MutationMapping
    public boolean editDictData(@Argument @Valid EditDictDataDTO dictData) {
        return dictDataService.edit(dictData);
    }

    /**
     * 删除字典数据
     *
     * @param ids 字典数据标识
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:dictdata:delete')")
    @MutationMapping
    public boolean remDictData(@Argument @Valid
                               @Size(max = 100, message = "一次性可删除1-100个字典数据")
                               @NotEmpty(message = "字典数据标识不能为空") List<Long> ids) {
        return dictDataService.rem(ids);
    }
}
