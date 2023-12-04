package com.yanxuan88.australiacallcenter.common;


/**
 * 字典bean
 * 只有code和text，可用于展示下拉框
 *
 * @author co
 * @since 2023/11/30 下午2:31:30
 */
public class DictBean<T> implements IDict<T> {
    private final T code;
    private final String text;

    public DictBean(T code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public T getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}