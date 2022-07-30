package com.pepsiwyl.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.pepsiwyl.pojo.User;
import com.pepsiwyl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:29
 */

@Slf4j

@Controller
@RequestMapping(name = "用户控制器", path = "/user")
public class UserController {

    /**
     * 注入UserService
     */
    @Resource(name = "userService")
    UserService userService;

    /**
     * 注入captchaProducer
     */
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;


    /**
     * loginController
     *
     * @param user
     * @return 失败--> 页面  成功---> 页面
     */
    @PostMapping(name = "用户登陆", path = "/login")
    public String login(User user,
                        @RequestParam("vcode") String vcode,
                        Boolean rememberMe,
                        HttpSession session) {
        log.info("username:" + user.getUsername() + "  password:" + user.getPassword() + "  vcode:" + vcode + "  session:" + session.getAttribute(Constants.KAPTCHA_SESSION_KEY) + " rememberMe:" + rememberMe);
        try {
            // 验证码比较
            if (vcode.equalsIgnoreCase((String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {
                // 认证登陆                                                                                          // rememberMe null-->false  true-->true
                SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsername(), user.getPassword(), !ObjectUtils.isEmpty(rememberMe)));
                log.info("认证成功:<------");
                // 跳转首页
                return "redirect:/index.html";
            } else {
                throw new RuntimeException("验证码错误!!!");
            }
        } catch (UnknownAccountException e) {
            log.info("认证失败:------>用户名不存在!");
        } catch (IncorrectCredentialsException e) {
            log.info("认证失败:------>密码错误!");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        // 跳转登陆页面
        return "redirect:/login.html";
    }


    /**
     * registerController
     *
     * @param user
     * @return
     */
    @PostMapping(name = "用户注册", path = "/register")
    public String register(User user) {
        log.info("username:" + user.getUsername() + "  password:" + user.getPassword());
        // 注册业务
        if (userService.register(user)) {
            log.info("注册成功:<------");
            // 跳转登陆页面
            return "redirect:/login.html";
        } else {
            log.info("注册失败:------>用户名已经存在");
            return "redirect:/register.html";
        }
    }


    /**
     * logoutController
     *
     * @return 返回登陆页面
     */
    @GetMapping(name = "用户注销", path = "/logout")
    public String logout() {
        // 主体对象
        Subject subject = SecurityUtils.getSubject();
        // 注销登陆
        subject.logout();
        log.info("注销成功:<------");
        // 跳转登陆页面
        return "redirect:/login.html";
    }


    /**
     * getImage 生成验证码
     *
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping(name = "生成验证码", path = "/getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
        log.info("capText------>" + capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
