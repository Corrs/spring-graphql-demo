package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDictDataMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.DictDataQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDictDataDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDictData;
import com.yanxuan88.australiacallcenter.model.vo.DictDataVO;
import com.yanxuan88.australiacallcenter.service.IDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements IDictDataService {
    @Override
    public Page<DictDataVO> page(PageDTO p, DictDataQueryDTO query) {
        Page<SysDictData> page = page(p.page(), Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictTypeId, query.getDictTypeId())
                .eq(StringUtils.hasText(query.getDictValue()), SysDictData::getDictValue, query.getDictValue())
                .like(StringUtils.hasText(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
                .orderByAsc(SysDictData::getSort, SysDictData::getId)
        );

        Page<DictDataVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            voPage.setRecords(page.getRecords().stream()
                    .map(e -> new DictDataVO()
                            .setId(e.getId())
                            .setDictLabel(e.getDictLabel())
                            .setDictValue(e.getDictValue())
                            .setSort(e.getSort())
                            .setDictTypeId(e.getDictTypeId())
                            .setRemark(e.getRemark())
                            .setCreateTime(e.getCreateTime()))
                    .collect(Collectors.toList()));
        }
        return voPage;
    }

    @Override
    public synchronized boolean add(AddDictDataDTO dictData) {
        log.info("\n新增字典数据\n参数：{}", dictData);
        String dictValue = dictData.getDictValue().trim();
        Long dictTypeId = dictData.getDictTypeId();
        SysDictData record = getOne(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictTypeId, dictTypeId).eq(SysDictData::getDictValue, dictValue), false);
        if (record != null) {
            log.info("字典值为{}的字典数据已存在", dictValue);
            throw new BizException("字典值重复");
        }
        SysDictData entity = new SysDictData();
        entity.setDictLabel(dictData.getDictLabel().trim());
        entity.setDictTypeId(dictTypeId);
        entity.setDictValue(dictValue);
        entity.setSort(dictData.getSort());
        entity.setRemark(Optional.ofNullable(dictData.getRemark()).map(String::trim).orElse(""));
        boolean result = save(entity);
        log.info("处理结果：{}", result);
        return result;
    }

    @Override
    public synchronized boolean rem(List<Long> ids) {
        log.info("\n批量删除字典数据\n参数：{}", ids);
        boolean result = removeBatchByIds(ids);
        log.info("处理结果：{}", result);
        return result;
    }

    @Override
    public synchronized boolean edit(EditDictDataDTO dictData) {
        log.info("\n修改字典数据\n参数：{}", dictData);
        Long id = dictData.getId();
        SysDictData entity = getById(id);
        if (entity == null) {
            throw new BizException("字典数据不存在");
        }
        String dictValue = dictData.getDictValue().trim();
        if (!entity.getDictValue().equals(dictValue)) {
            log.info("修改了字典值，需要校验字典值是否唯一");
            SysDictData record = getOne(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictTypeId, entity.getDictTypeId()).eq(SysDictData::getDictValue, dictValue), false);
            if (record != null) {
                log.info("字典值为{}的字典数据已存在", dictValue);
                throw new BizException("字典值重复");
            }
            entity.setDictValue(dictValue);
        }
        entity.setSort(dictData.getSort());
        entity.setDictLabel(dictData.getDictLabel().trim());
        entity.setRemark(Optional.ofNullable(dictData.getRemark()).map(String::trim).orElse(""));
        boolean result = updateById(entity);
        log.info("处理结果：{}", result);
        return result;
    }
}
