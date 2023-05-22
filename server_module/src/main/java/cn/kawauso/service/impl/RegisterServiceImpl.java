package cn.kawauso.service.impl;

import cn.kawauso.auth.TokenVerifier;
import cn.kawauso.entity.UserInfo;
import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.RegisterService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.yitter.idgen.YitIdHelper;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link RegisterServiceImpl}是{@link RegisterService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final String EMAIL_CODE_PREFIX = "register:email:";
    private static final long EMAIL_CODE_TIMEOUT = 20 * 60 * 1000;

    private final RedisCommands<String, String> redisCommands;
    private final UserInfoMapper userInfoMapper;
    private final TokenVerifier tokenVerifier;
    private final JavaMailSender mailSender;

    public RegisterServiceImpl(RedisCommands<String, String> redisCommands,
                               UserInfoMapper userInfoMapper,
                               TokenVerifier tokenVerifier,
                               JavaMailSender mailSender) {
        this.redisCommands = redisCommands;
        this.userInfoMapper = userInfoMapper;
        this.tokenVerifier = tokenVerifier;
        this.mailSender = mailSender;
    }

    /**
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 申请注册的邮箱
     * @return 响应结果
     */
    @Override
    public Object sendEmailCode(String email) {

        if (userInfoMapper.getUsersWithEmail(email) > 0) {
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

        return "ok";
    }

    /**
     * 提交申请注册的邮箱和接收到的验证码，检验是否合法
     *
     * @param email      申请注册的邮箱
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
     * 提交用户账号初始化相关信息，注册一个新的账号
     *
     * @param token    为账号初始化提供鉴权支持的token
     * @param userInfo {@link UserInfo}，包含了用户账号的初始化信息
     * @return 响应结果
     */
    @Override
    public Object registerNewAccount(String token, UserInfo userInfo) {

        DecodedJWT decodedJWT = tokenVerifier.verifyToken(token);

        long userId = YitIdHelper.nextId();
        String email = decodedJWT.getClaim("email").asString();

        userInfo.setUserId(userId);
        userInfo.setEmail(email);

        userInfoMapper.insertUserInfo(userInfo);

        return "ok";
    }
}
