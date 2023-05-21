package cn.kawauso.service.impl;

import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.RegisterService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * {@link RegisterServiceImpl}是{@link RegisterService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserInfoMapper userInfoMapper;
    private final JavaMailSender mailSender;

    public RegisterServiceImpl(RedisTemplate<String, String> redisTemplate,
                               UserInfoMapper userInfoMapper,
                               JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.userInfoMapper = userInfoMapper;
        this.mailSender = mailSender;
    }

    /**
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param mail 邮箱
     * @return 响应结果
     */
    @Override
    public Object registerNewAccount(String mail) {

        if (userInfoMapper.getUsersWithMail(mail) > 0) {
            throw new RuntimeException("此邮箱已被注册！");
        }

        String verifyCode = String.valueOf((int)((Math.random() * 9 + 1) * Math.pow(10,5)));

        Boolean setResult = redisTemplate.opsForValue().setIfAbsent(mail, verifyCode, 120, TimeUnit.SECONDS);
        assert setResult != null;

        if (! setResult) {
            throw new RuntimeException("此邮箱正在被使用注册！");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("我们正在注册账号，需要验证您的邮箱！");
        message.setFrom("2146844288@qq.com");
        message.setTo(mail);
        message.setText("您的验证码为 " + verifyCode + " ，有效期为120秒！");

        mailSender.send(message);

        return "success";
    }

}
