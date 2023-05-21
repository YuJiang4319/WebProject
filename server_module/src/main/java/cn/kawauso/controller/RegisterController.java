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
     * 使用给定的邮箱，启动注册新账号的流程
     *
     * @param mail 邮箱
     * @return 响应结果
     */
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public Object registerNewAccount(String mail) {

        if (! mail.matches("[\\w.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            throw new RuntimeException("邮箱格式错误！");
        }

        return registerService.registerNewAccount(mail);
    }

}
