package cn.kawauso.controller;

import cn.kawauso.service.RegisterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link RegisterController}是注册服务相关请求的控制器
 *
 * @author RealDragonking
 */
@RestController
@RequestMapping(path = "/register")
public final class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * 使用给定的邮箱，发送邮箱验证码，启动注册新账号的流程
     *
     * @param email 邮箱
     * @return 响应结果
     */
    @RequestMapping(path = "/send-email-code", method = RequestMethod.POST)
    public Object sendEmailCode(String email) {

        if (! email.matches("[\\w.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            throw new RuntimeException("邮箱格式错误！");
        }

        return registerService.sendEmailCode(email);
    }

    /**
     * 验证邮箱及对应的邮箱验证码是否有效，有效的话进入账号初始化流程
     *
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    @RequestMapping(path = "/auth-email-code", method = RequestMethod.POST)
    public Object authEmailCode(String email, String emailCode) {
        return registerService.authEmailCode(email, emailCode);
    }

}
