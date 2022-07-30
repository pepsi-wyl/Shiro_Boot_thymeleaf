package com.pepsiwyl.shiro.realm;

import com.pepsiwyl.pojo.Perms;
import com.pepsiwyl.pojo.Role;
import com.pepsiwyl.pojo.User;
import com.pepsiwyl.service.PermsService;
import com.pepsiwyl.service.RoleService;
import com.pepsiwyl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:22
 */
@Slf4j

@Component("userRealm")
public class UserRealm extends AuthorizingRealm {

    /**
     * 注入UserService RoleService PermsService模板
     */
    @Resource(name = "userService")
    UserService userService;
    @Resource(name = "roleService")
    RoleService roleService;
    @Resource(name = "permsService")
    PermsService permsService;

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 得到用户名信息
        String principal = (String) authenticationToken.getPrincipal();
        log.info("认证 ------>" + principal + "<------");

        // 用户名认证
        User user = userService.getUserByUserName(principal);
        if (!ObjectUtils.isEmpty(user)) {
                // 密码认证 由SecurityManager 自动认证(需要配置)
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
        }

        // 用户名称错误
        return null;
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // 得到认证通过的用户名信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        log.info("授权 ------>" + primaryPrincipal + "<------");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 数据库查询得到Roles
        List<Role> roles = roleService.getRolesByUserName(primaryPrincipal);
        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());
                // 数据库查询得到Perms
                List<Perms> perms = permsService.getPermsByRoleName(role.getName());
                perms.forEach(perm -> {
                    simpleAuthorizationInfo.addStringPermission(perm.getName());
                });
            });
        }
        // 返回权限信息
        return simpleAuthorizationInfo;
    }

}

