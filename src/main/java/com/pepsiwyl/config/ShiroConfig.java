package com.pepsiwyl.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.pepsiwyl.shiro.cache.RedisCacheManger;
import com.pepsiwyl.shiro.realm.UserRealm;
import com.pepsiwyl.utils.ConstUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:25
 */

@Configuration
public class ShiroConfig {


    /**
     * Realm
     */
    @Bean("realm")
    public AuthorizingRealm getRealm(
            @Qualifier("userRealm") UserRealm realm,
            @Qualifier("redisCacheManger") RedisCacheManger cacheManger
    ) {

        // Realm设置凭证匹配器  MD5
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(ConstUtils.MD5);
        credentialsMatcher.setHashIterations(ConstUtils.HashNumber);
        realm.setCredentialsMatcher(credentialsMatcher);

        // Realm设置缓存管理器
        realm.setCacheManager(cacheManger);
        realm.setCachingEnabled(true);                           // 开启全局缓存管理
        realm.setAuthenticationCachingEnabled(true);             // 开启认证缓存管理
        realm.setAuthorizationCachingEnabled(true);              // 开启授权缓存管理

        return realm;
    }

    /**
     * 安全管理器
     */
    @Bean("defaultWebSecurityManager")
    public DefaultWebSecurityManager getSecurityManager(@Qualifier("realm") AuthorizingRealm realm) {
        // 创建安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 安全管理器设置Realm
        securityManager.setRealm(realm);
        // 安全管理器设置RememberMeManager
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * ShiroFilter 负责拦截请求
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(
            @Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager securityManager) {
        // 创建ShiroFilterFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 给Filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置公共资源  配置受限资源  通配符进行认证
        HashMap<String, String> map = new HashMap<>();
        map.put("/", "anon");
        map.put("/toIndex", "anon");
        map.put("/index.html", "anon");
        map.put("/toLogin", "anon");
        map.put("/login.html", "anon");
        map.put("/toRegister", "anon");
        map.put("/register.html", "anon");
        map.put("/user/login", "anon");
        map.put("/user/register", "anon");
        map.put("/user/getImage", "anon");  // 验证码不受限制
        map.put("/**", "authc");            // 其他所有资源都受限
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        // 默认认证界面路径---当认证不通过时跳转 设置登陆页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        return shiroFilterFactoryBean;
    }

    /**
     * 加入方言处理 避免标签不识别
     *
     * @return
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * cookie管理对象
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     *
     * @return
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();

        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");

        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        cookieRememberMeManager.setCookie(cookie);

        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

}
