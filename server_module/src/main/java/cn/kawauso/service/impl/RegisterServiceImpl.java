package cn.kawauso.service.impl;

import cn.kawauso.entity.UserInfo;
import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.RegisterService;
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
     * @param email 申请注册的邮箱
     * @return 响应结果
     */
    @Override
    public Object sendEmailCode(String email) {

        if (userInfoMapper.getUsersWithEmail(email) > 0) {
            throw new RuntimeException("此邮箱已被注册！");
        }

        String emailCode = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        redisCommands.setex("register:" + email, EMAIL_CODE_TIMEOUT, emailCode);

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

        String answer = redisCommands.get("register:" + email);

        if (answer == null || ! answer.equals(emailCode)) {
            throw new RuntimeException("验证码已经失效！");
        }

        long ticket = YitIdHelper.nextId();

        String strTicket = String.valueOf(ticket);
        redisCommands.set(strTicket, email);

        redisCommands.del("register:" + email);

        return Map.of("ticket", ticket);
    }

    /**
     * 提交ticket和账号初始化信息，注册一个新的账号
     *
     * @param ticket   为账号初始化提供鉴权支持的ticket
     * @param userInfo {@link UserInfo}，包含了用户账号的初始化信息
     * @return 响应结果
     */
    @Override
    public Object registerNewAccount(long ticket, UserInfo userInfo) {

        String strTicket = String.valueOf(ticket);
        String answer = redisCommands.getdel(strTicket);

        if (answer == null) {
            throw new RuntimeException("无效的token！");
        }

        long userId = YitIdHelper.nextId();

        userInfo.setUserId(userId);
        userInfo.setEmail(answer);

        userInfoMapper.insertUserInfo(userInfo);

        return "ok";
    }

}
