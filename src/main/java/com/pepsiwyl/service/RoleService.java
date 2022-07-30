package com.pepsiwyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsiwyl.pojo.Role;

import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:14
 */

public interface RoleService  extends IService<Role> {

    /**
     * 根据用户名查询角色
     *
     * @param username
     * @return list Role
     */
    List<Role> getRolesByUserName(String username);

}
