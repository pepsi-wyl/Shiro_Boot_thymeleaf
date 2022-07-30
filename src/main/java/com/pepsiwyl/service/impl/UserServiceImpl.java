package com.pepsiwyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsiwyl.mapper.UserMapper;
import com.pepsiwyl.pojo.User;
import com.pepsiwyl.service.UserService;
import com.pepsiwyl.utils.ConstUtils;
import com.pepsiwyl.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:16
 */

@Transactional

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 注入UserMapper
     */
    @Resource(name = "userMapper")
    UserMapper userMapper;

    /**
     * 注册用户
     *
     * @param user
     */
    @Override
    public boolean register(User user) {
        // 查询用户名是否存在
        if (userMapper.selectCount(new QueryWrapper<User>().eq("username", user.getUsername())) == 0) {
            // 密码加密 MD5 + salt + hash
            String salt = SaltUtils.getSalt(ConstUtils.SaltNumber);
            user.setPassword(new Md5Hash(user.getPassword(), salt, ConstUtils.HashNumber).toHex());
            user.setSalt(salt);
            // 插入用户
            userMapper.insert(user);
            return true;
        }
        // 注册失败 用户名重复
        return false;
    }

    /**
     * 根据用户名查询用户  登陆
     *
     * @param userName
     * @return 用户
     */
    @Override
    public User getUserByUserName(String userName) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", userName));
    }

}
