package com.pepsiwyl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pepsiwyl.pojo.Perms;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:11
 */

@Transactional
@Mapper
public interface PermsMapper extends BaseMapper<Perms> {

    /**
     * 根据角色查询权限集合
     *
     * @param roleName
     * @return list Perms
     */
    List<Perms> getPermsByRoleName(String roleName);

}