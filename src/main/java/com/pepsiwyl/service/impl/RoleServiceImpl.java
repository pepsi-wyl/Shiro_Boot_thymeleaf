package com.pepsiwyl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsiwyl.mapper.RoleMapper;
import com.pepsiwyl.pojo.Role;
import com.pepsiwyl.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:15
 */

@Transactional

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 注入RoleMapper
     */
    @Resource(name = "roleMapper")
    RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserName(String username) {
        return roleMapper.getRolesByUserName(username);
    }

}
