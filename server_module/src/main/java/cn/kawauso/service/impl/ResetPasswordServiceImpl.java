package cn.kawauso.service.impl;

import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.ResetPasswordService;
import com.github.yitter.idgen.YitIdHelper;
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

    private static final long EMAIL_CODE_TIMEOUT = 20 * 60 * 1000;

    private final RedisCommands<String, String> redisCommands;
    private final UserInfoMapper userInfoMapper;
    private final JavaMailSender mailSender;

    public ResetPasswordServiceImpl(RedisCommands<String, String> redisCommands,
                                    UserInfoMapper userInfoMapper,
                                    JavaMailSender mailSender) {
        this.redisCommands = redisCommands;
        this.userInfoMapper = userInfoMapper;
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

        redisCommands.setex("reset:" + email, EMAIL_CODE_TIMEOUT, emailCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("我们正在重置此账号的密码，需要验证您是否是本人操作！");
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

        String answer = redisCommands.get("reset:" + email);

        if (answer == null || ! answer.equals(emailCode)) {
            throw new RuntimeException("验证码已经失效！");
        }

        long userId = userInfoMapper.getUserInfoByEmail(email).getUserId();
        long ticket = YitIdHelper.nextId();

        String strTicket = String.valueOf(ticket);
        String strUserId = String.valueOf(userId);
        redisCommands.set(strTicket, strUserId);

        redisCommands.del("reset:" + email);

        return Map.of("ticket", ticket);
    }

    /**
     * 提交ticket和新设置的密码，重置账号密码
     *
     * @param ticket      为账号密码重置提供鉴权支持的一次性ticket
     * @param newPassword 新设置的密码
     * @return 响应结果
     */
    @Override
    public Object resetPassword(long ticket, String newPassword) {

        String strTicket = String.valueOf(ticket);
        String answer = redisCommands.getdel(strTicket);

        if (answer == null) {
            throw new RuntimeException("无效的token！");
        }

        long userId = Long.parseLong(answer);

        userInfoMapper.updateUserPassword(userId, newPassword);

        return "ok";
    }

}
