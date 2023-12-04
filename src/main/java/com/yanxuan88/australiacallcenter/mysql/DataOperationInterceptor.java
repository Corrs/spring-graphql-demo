package com.yanxuan88.australiacallcenter.mysql;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yanxuan88.australiacallcenter.common.UserContext;
import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 公共字段自动补全 拦截器
 *
 * @author co
 * @since 2023/11/30 下午1:56:33
 */
public class DataOperationInterceptor implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String CREATE_USER = "createUser";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_USER = "updateUser";
    private static final String IS_DELETED = "isDeleted";

    private Long getCurrentUserId() {
        return Optional.ofNullable(UserContext.get()).map(UserLoginInfo::getId).orElse(0L);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, CREATE_USER, this::getCurrentUserId, Long.class);
        this.strictInsertFill(metaObject, IS_DELETED, () -> Boolean.FALSE, Boolean.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, UPDATE_USER, this::getCurrentUserId, Long.class);
    }
}
