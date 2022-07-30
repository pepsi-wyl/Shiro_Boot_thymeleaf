package com.pepsiwyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsiwyl.pojo.User;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:13
 */

public interface UserService extends IService<User> {

    /**
     * 注册用户信息
     *
     * @param user
     */
    boolean register(User user);

    /**
     * 根据用户名查询用户 登陆
     *
     * @param userName
     * @return 用户
     */
    User getUserByUserName(String userName);

}
