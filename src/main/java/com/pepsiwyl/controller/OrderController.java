package com.pepsiwyl.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by pepsi-wyl
 * @date 2022-07-29 21:54
 */

@Slf4j

@Controller
@RequestMapping(name = "订单控制器", path = "/order")
public class OrderController {

    // 代码方式授权
    @ResponseBody
    @GetMapping("/save")
    public String SaveOrder() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("order_manager") || subject.hasRole("admin")) return "保存成功";
        else return "无权访问";
    }

    // 注解方式授权
//    @RequiresRoles(value = {"order_manager", "admin"})  //用来判断角色  同时具有 order_manager admin
    @RequiresRoles(value = {"order_manager", "admin"}, logical = Logical.OR)  //用来判断角色  具有任意 order_manager admin
    @RequiresPermissions("order:delete:*")                                    //用来判断权限字符串
    @ResponseBody
    @GetMapping("/delete")
    public String DeleteOrder() {
        return "进入方法";
    }

}
