package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysUserMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
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
        SysUser entity = getById(userId);
        if (entity == null) {
            throw new BizException("用户不存在");
        }
        entity.setPassword(passwordEncoder.encode(SecurityUtil.pwd(entity.getSalt(), Constant.DEFAULT_PASSWORD_MD5)));
        return updateById(entity);
    }

    @SysLog("新增用户")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(AddUserDTO user) {
        String salt = SecurityUtil.salt(null);
        String username = user.getUsername().trim();
        if (BLACK_USER_LIST.contains(username)) {
            throw new BizException("非法用户名");
        }
        SysUser entity = new SysUser()
                .setSalt(salt)
                .setGender(user.getGender())
                .setUsername(username)
                .setDeptId(user.getDeptId())
                .setRealName(user.getRealName().trim())
                .setMobile(Strings.nullToEmpty(user.getMobile()).trim())
                .setEmail(Strings.nullToEmpty(user.getEmail()).trim())
                .setPassword(passwordEncoder.encode(SecurityUtil.pwd(salt, Constant.DEFAULT_PASSWORD_MD5)));
        try {
            boolean result = save(entity);
            if (result && user.getRoleId() != null) {
                userRoleService.add(entity.getUserId(), user.getRoleId());
            }
            return result;
        } catch (DuplicateKeyException e) {
            throw new BizException("用户名已存在");
        }
    }
}
