package cn.kawauso.service.impl;

import cn.kawauso.auth.TokenVerifier;
import cn.kawauso.entity.UserInfo;
import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.ResetPasswordService;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link ResetPasswordServiceImpl}是{@link ResetPasswordService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private static final String EMAIL_CODE_PREFIX = "reset:email:";
    private static final long EMAIL_CODE_TIMEOUT = 20 * 60 * 1000;

    private final RedisCommands<String, String> redisCommands;
    private final UserInfoMapper userInfoMapper;
    private final TokenVerifier tokenVerifier;
    private final JavaMailSender mailSender;

    public ResetPasswordServiceImpl(RedisCommands<String, String> redisCommands,
                                    UserInfoMapper userInfoMapper,
                                    TokenVerifier tokenVerifier,
                                    JavaMailSender mailSender) {
        this.redisCommands = redisCommands;
        this.userInfoMapper = userInfoMapper;
        this.tokenVerifier = tokenVerifier;
        this.mailSender = mailSender;
    }

    /**
     * 启动重置密码的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 已经进行过注册的邮箱
     * @return 响应结果
     */
    @Override
    public Object sendEmailCode(String email) {

        if (userInfoMapper.getUsersWithEmail(email) == 0) {
            throw new RuntimeException("对应此邮箱的用户不存在！");
        }

        String emailCode = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        redisCommands.setex(EMAIL_CODE_PREFIX + email, EMAIL_CODE_TIMEOUT, emailCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("我们正在重置账号的密码，需要验证您的邮箱！");
        message.setFrom("2146844288@qq.com");
        message.setTo(email);
        message.setText("您的验证码为 " + emailCode + " ，有效期为20分钟！");

        mailSender.send(message);

        return "ok";
    }

    /**
     * 提交申请重置密码的邮箱和接收到的验证码，校验是否合法
     *
     * @param email     已经进行过注册的邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    @Override
    public Object authEmailCode(String email, String emailCode) {

        String answer = redisCommands.getdel(EMAIL_CODE_PREFIX + email);

        if (answer == null || ! answer.equals(emailCode)) {
            throw new RuntimeException("验证码已经失效！");
        }

        String token = tokenVerifier.generateToken(Map.of("email", email));

        return Map.of("token", token);
    }

    /**
     * 提交用户id和新设置的密码，重置账号密码
     *
     * @param token    为账号密码重置提供鉴权支持的token
     * @param userInfo {@link UserInfo}，仅包含新设置的密码
     * @return 响应结果
     */
    @Override
    public Object resetPassword(String token, UserInfo userInfo) {
        //
        return null;
    }

}
