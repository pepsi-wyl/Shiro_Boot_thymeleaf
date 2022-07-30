package com.pepsiwyl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsiwyl.mapper.PermsMapper;
import com.pepsiwyl.pojo.Perms;
import com.pepsiwyl.service.PermsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:15
 */

@Transactional

@Service("permsService")
public class PermsServiceImpl extends ServiceImpl<PermsMapper, Perms> implements PermsService {

    /**
     * 注入PermsMapper
     */
    @Resource(name = "permsMapper")
    PermsMapper permsMapper;

    @Override
    public List<Perms> getPermsByRoleName(String roleName) {
        return permsMapper.getPermsByRoleName(roleName);
    }

}