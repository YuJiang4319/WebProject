package cn.kawauso.controller;

import cn.kawauso.service.ResetPasswordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link ResetPasswordController}是重置密码服务相关请求的控制器
 *
 * @author RealDragonking
 */
@RestController
@RequestMapping(path = "/help")
public final class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    /**
     * 启动重置密码的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 已经进行过注册的邮箱
     * @return 响应结果
     */
    @RequestMapping(path = "/send-email-code", method = RequestMethod.POST)
    public Object sendEmailCode(String email) {

        if (! email.matches("[\\w.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            throw new RuntimeException("邮箱格式错误！");
        }

        return resetPasswordService.sendEmailCode(email);
    }

    /**
     * 提交申请重置密码的邮箱和接收到的验证码，校验是否合法
     *
     * @param email 已经进行过注册的邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    @RequestMapping(path = "/auth-email-code", method = RequestMethod.POST)
    public Object authEmailCode(String email, String emailCode) {
        return resetPasswordService.authEmailCode(email, emailCode);
    }

    /**
     * 提交ticket和新设置的密码，重置账号密码
     *
     * @param ticket 为账号密码重置提供鉴权支持的一次性ticket
     * @param newPassword 新设置的密码
     * @return 响应结果
     */
    @RequestMapping(path = "/reset-password", method = RequestMethod.POST)
    public Object resetPassword(long ticket, String newPassword) {
        return resetPasswordService.resetPassword(ticket, newPassword);
    }

}
