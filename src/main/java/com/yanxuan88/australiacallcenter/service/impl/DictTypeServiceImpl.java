package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDictTypeMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictTypeQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictTypeDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDictType;
import com.yanxuan88.australiacallcenter.model.vo.DictTypeVO;
import com.yanxuan88.australiacallcenter.service.IDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class DictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements IDictTypeService {
    @Override
    public Page<DictTypeVO> page(PageDTO p, DictTypeQueryDTO query) {
        Page<SysDictType> page = page(p.page(), Wrappers.<SysDictType>lambdaQuery()
                .nested(query != null, wrapper -> wrapper
                        .apply("1=1")
                        .eq(StringUtils.hasText(query.getDictType()), SysDictType::getDictType, query.getDictType())
                        .like(StringUtils.hasText(query.getDictName()), SysDictType::getDictName, query.getDictName()))
                .orderByAsc(SysDictType::getSort, SysDictType::getId)
        );
        Page<DictTypeVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            voPage.setRecords(
                    page.getRecords().stream().map(e -> new DictTypeVO()
                            .setId(e.getId())
                            .setDictType(e.getDictType())
                            .setDictName(e.getDictName())
                            .setRemark(e.getRemark())
                            .setSort(e.getSort())
                            .setCreateTime(e.getCreateTime())).collect(Collectors.toList())
            );
        }
        return voPage;
    }

    @Override
    @SysLog("新增字典")
    public synchronized boolean add(AddDictTypeDTO dictType) {
        String type = Strings.nullToEmpty(dictType.getDictType()).trim();
        SysDictType record = getOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, type), false);
        if (record != null) {
            throw new BizException("字典类型重复");
        }
        SysDictType entity = new SysDictType();
        entity.setDictType(type);
        entity.setDictName(dictType.getDictName().trim());
        entity.setSort(dictType.getSort());
        entity.setRemark(dictType.getRemark().trim());
        return save(entity);
    }

    @Override
    @SysLog("修改字典")
    public synchronized boolean edit(EditDictTypeDTO dictType) {
        SysDictType entity = getById(dictType.getId());
        if (entity == null) {
            throw new BizException("字典数据不存在");
        }
        String type = Strings.nullToEmpty(dictType.getDictType()).trim();
        if (!entity.getDictType().equals(type)) {
            SysDictType record = getOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, type), false);
            if (record != null) {
                throw new BizException("字典类型重复");
            }
        }
        entity.setDictType(type);
        entity.setDictName(dictType.getDictName().trim());
        entity.setSort(dictType.getSort());
        entity.setRemark(dictType.getRemark().trim());
        return updateById(entity);
    }

    @Override
    @SysLog("删除字典")
    public synchronized boolean rem(Long id) {
        // 暂时不级联删除 字典项数据
        return removeById(id);
    }
}
