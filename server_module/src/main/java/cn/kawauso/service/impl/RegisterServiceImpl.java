package cn.kawauso.service.impl;

import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.RegisterService;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * {@link RegisterServiceImpl}是{@link RegisterService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final String SETTING_TOKEN_PREFIX = "token:";
    private static final String EMAIL_CODE_PREFIX = "code:";
    private static final long EMAIL_CODE_TIMEOUT = 20 * 60 * 1000;

    private final RedisCommands<String, String> redisCommands;
    private final UserInfoMapper userInfoMapper;
    private final JavaMailSender mailSender;

    public RegisterServiceImpl(RedisCommands<String, String> redisCommands,
                               UserInfoMapper userInfoMapper,
                               JavaMailSender mailSender) {
        this.redisCommands = redisCommands;
        this.userInfoMapper = userInfoMapper;
        this.mailSender = mailSender;
    }

    /**
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 邮箱
     * @return 响应结果
     */
    @Override
    public Object sendEmailCode(String email) {

        if (userInfoMapper.getUsersWithMail(email) > 0) {
            throw new RuntimeException("此邮箱已被注册！");
        }

        String emailCode = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        redisCommands.setex(EMAIL_CODE_PREFIX + email, EMAIL_CODE_TIMEOUT, emailCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("我们正在注册账号，需要验证您的邮箱！");
        message.setFrom("2146844288@qq.com");
        message.setTo(email);
        message.setText("您的验证码为 " + emailCode + " ，有效期为20分钟！");

        mailSender.send(message);

        return null;
    }

    /**
     * 提交申请注册的邮箱和接收到的验证码，检验是否合法
     *
     * @param email      邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    @Override
    public Object authEmailCode(String email, String emailCode) {

        String answer = redisCommands.get(EMAIL_CODE_PREFIX + email);

        if (answer == null || ! answer.equals(emailCode)) {
            throw new RuntimeException("验证码已经失效！");
        }

        String token = UUID.randomUUID().toString();

        redisCommands.setnx(SETTING_TOKEN_PREFIX + email, token);

        return token;
    }

}
