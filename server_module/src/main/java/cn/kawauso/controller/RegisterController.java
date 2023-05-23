package cn.kawauso.controller;

import cn.kawauso.entity.UserInfo;
import cn.kawauso.service.RegisterService;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
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
     * 提交申请注册的邮箱和接收到的验证码，检验是否合法
     *
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    @RequestMapping(path = "/auth-email-code", method = RequestMethod.POST)
    public Object authEmailCode(String email, String emailCode) {
        return registerService.authEmailCode(email, emailCode);
    }

    /**
     * 提交ticket和账号初始化信息，注册一个新的账号
     *
     * @param ticket 为账号初始化提供鉴权支持的一次性ticket
     * @param userInfo {@link UserInfo}，包含了用户的初始化信息
     * @return 响应结果
     */
    @RequestMapping(path = "/new-account", method = RequestMethod.POST)
    public Object registerNewAccount(long ticket, @RequestBody UserInfo userInfo) {

        if (userInfo.getPassword() == null) {
            throw new RuntimeException("提交参数不完整！");
        }

        return registerService.registerNewAccount(ticket, userInfo);
    }

}
