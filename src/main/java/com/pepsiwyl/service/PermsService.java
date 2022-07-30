package com.pepsiwyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsiwyl.pojo.Perms;

import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:14
 */

public interface PermsService extends IService<Perms> {

    /**
     * 根据角色查询权限集合
     *
     * @param roleName
     * @return list Perms
     */
    List<Perms> getPermsByRoleName(String roleName);

}
