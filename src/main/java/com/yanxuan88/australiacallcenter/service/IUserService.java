package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.dto.UserQueryDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<SysUser> {
    SysUser queryByUsername(String username);

    List<SysUser> queryByDept(Long deptId);

    boolean resetPassword(Long userId);

    boolean add(AddUserDTO user);

    Page<UserVO> users(PageDTO p, UserQueryDTO query);

    boolean rem(Long userId);

    boolean chgUserStatus(Long userId);
}
