package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysUserMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditUserDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.dto.UserQueryDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.enums.UserStatusEnum;
import com.yanxuan88.australiacallcenter.model.vo.UserVO;
import com.yanxuan88.australiacallcenter.service.IUserRoleService;
import com.yanxuan88.australiacallcenter.service.IUserService;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanxuan88.australiacallcenter.common.Constant.BLACK_USER_LIST;
import static com.yanxuan88.australiacallcenter.model.enums.UserStatusEnum.DISABLE;
import static com.yanxuan88.australiacallcenter.model.enums.UserStatusEnum.ENABLED;

@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public SysUser queryByUsername(String username) {
        return getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
    }

    @Override
    public List<SysUser> queryByDept(Long deptId) {
        return list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, deptId));
    }

    @SysLog("重置用户密码")
    @Override
    public boolean resetPassword(Long userId) {
        SysUser record = getById(userId);
        if (record == null) {
            throw new BizException("用户不存在");
        }
        SysUser entity = new SysUser();
        entity.setPassword(passwordEncoder.encode(SecurityUtil.pwd(record.getSalt(), Constant.DEFAULT_PASSWORD_MD5)))
                .setUserId(userId);
        return updateById(entity);
    }

    @SysLog("新增用户")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(AddUserDTO user) {
        String username = user.getUsername().trim();
        if (BLACK_USER_LIST.contains(username)) {
            throw new BizException("非法用户名");
        }
        String salt = SecurityUtil.salt(null);
        SysUser entity = new SysUser()
                .setSalt(salt)
                .setGender(user.getGender())
                .setUsername(username)
                .setDeptId(user.getDeptId())
                .setStatus(UserStatusEnum.ENABLED.getCode())
                .setRealName(user.getRealName().trim())
                .setMobile(Strings.nullToEmpty(user.getMobile()).trim())
                .setEmail(Strings.nullToEmpty(user.getEmail()).trim())
                .setPassword(passwordEncoder.encode(SecurityUtil.pwd(salt, Constant.DEFAULT_PASSWORD_MD5)));
        boolean result = false;
        try {
            result = save(entity);
        } catch (DuplicateKeyException e) {
            throw new BizException("用户名已存在");
        }

        if (result && user.getRoleId() != null) {
            userRoleService.add(entity.getUserId(), user.getRoleId());
        }
        return result;
    }

    @Override
    public Page<UserVO> users(PageDTO p, UserQueryDTO query) {
        if (SecurityUtil.isSuperAdmin()) {
            return baseMapper.allUsers(p.page(), query);
        }
        return baseMapper.users(p.page(), query);
    }

    /**
     * 删除用户，级联删除用户角色
     *
     * @param userId 主键
     * @return true/false
     */
    @SysLog("删除用户")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean rem(Long userId) {
        boolean result = removeById(userId);
        if (result) {
            userRoleService.removeByUserId(userId);
        }
        return result;
    }

    @SysLog("修改用户状态")
    @Override
    public boolean chgUserStatus(Long userId) {
        SysUser record = getById(userId);
        if (record == null) {
            throw new BizException("用户不存在");
        }
        SysUser entity = new SysUser()
                .setUserId(userId)
                .setStatus(record.getStatus() == DISABLE.getCode() ? ENABLED.getCode() : DISABLE.getCode());
        return updateById(entity);
    }

    /**
     * 编辑用户
     *
     * @param user 参数
     * @return true/false
     */
    @SysLog("编辑用户")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean edit(EditUserDTO user) {
        if (!SecurityUtil.isSuperAdmin()) {
            SysUser record = getById(user.getId());
            if (record == null) {
                throw new BizException("用户不存在");
            }
        }
        String username = user.getUsername().trim();
        if (BLACK_USER_LIST.contains(username)) {
            throw new BizException("非法用户名");
        }
        SysUser entity = new SysUser().setUserId(user.getId())
                .setGender(user.getGender())
                .setUsername(username)
                .setDeptId(user.getDeptId())
                .setRealName(user.getRealName().trim())
                .setMobile(Strings.nullToEmpty(user.getMobile()).trim())
                .setEmail(Strings.nullToEmpty(user.getEmail()).trim());
        boolean result = false;
        try {
            result = !SecurityUtil.isSuperAdmin() ? updateById(entity) : SqlHelper.retBool(baseMapper.saUpdate(entity));
        } catch (DuplicateKeyException e) {
            throw new BizException("用户名已存在");
        }
        if (result) {
            if (user.getRoleId() != null && user.getRoleId() > 0) {
                userRoleService.add(entity.getUserId(), user.getRoleId());
            } else {
                userRoleService.removeByUserId(entity.getUserId());
            }
        }
        return result;
    }
}
