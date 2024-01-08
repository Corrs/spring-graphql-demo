package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.dto.UserQueryDTO;
import com.yanxuan88.australiacallcenter.model.vo.UserVO;
import com.yanxuan88.australiacallcenter.service.IUserService;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 分页查询用户
     *
     * @param p     分页
     * @param query 条件
     * @return relay
     */
//    @PreAuthorize("hasAuthority('sys:user:search')")
    @Authenticated
    @QueryMapping
    public Connection<UserVO> users(@Argument PageDTO p, @Argument UserQueryDTO query) {
        return RelayUtil.build(userService.users(p, query));
    }

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:user:resetpassword')")
    @MutationMapping
    public boolean resetPassword(@Argument @Valid @NotNull(message = "用户标识不能为空")
                                 @Min(value = 1, message = "用户不存在") Long userId) {
        return userService.resetPassword(userId);
    }

    /**
     * 新增用户
     *
     * @param user 用户
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:user:save')")
    @MutationMapping
    public boolean addUser(@Argument @Valid AddUserDTO user) {
        return userService.add(user);
    }

    /**
     * 删除用户，级联删除用户角色
     *
     * @param userId 主键
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @MutationMapping
    public boolean remUser(@Argument @Valid @NotNull(message = "用户标识不能为空")
                           @Min(value = 1, message = "用户不存在") Long userId) {
        return userService.rem(userId);
    }

    /**
     * 切换用户状态
     *
     * @param userId 主键
     * @return true/false
     */
    @PreAuthorize("hasAuthority('sys:user:lock')")
    @MutationMapping
    public boolean chgUserStatus (@Argument @Valid @NotNull(message = "用户标识不能为空")
                                 @Min(value = 1, message = "用户不存在") Long userId) {
        return userService.chgUserStatus(userId);
    }
}
