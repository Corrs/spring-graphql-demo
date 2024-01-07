package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
}
