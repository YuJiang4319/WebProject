package cn.kawauso.controller;

import cn.kawauso.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link LoginController}是登录服务相关请求的控制器
 *
 * @author RealDragonking
 */
@RestController
@RequestMapping(path = "/login")
public final class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 使用 邮箱+密码 的方式进行登录，对提交的登录请求进行校验，然后返回后续鉴权使用的token
     *
     * @param email 用户注册的邮箱
     * @param password 用户的密码
     * @return 响应结果
     */
    @RequestMapping(path = "/from-email", method = RequestMethod.POST)
    public Object loginFromEmail(String email, String password) {
        return loginService.authLoginRequest(email, password);
    }

}
