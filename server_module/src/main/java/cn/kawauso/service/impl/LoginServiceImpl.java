package cn.kawauso.service.impl;

import cn.kawauso.auth.TokenVerifier;
import cn.kawauso.entity.UserInfo;
import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link LoginServiceImpl}是{@link LoginService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final UserInfoMapper userInfoMapper;
    private final TokenVerifier tokenVerifier;

    public LoginServiceImpl(UserInfoMapper userInfoMapper, TokenVerifier tokenVerifier) {
        this.userInfoMapper = userInfoMapper;
        this.tokenVerifier = tokenVerifier;
    }

    /**
     * 对提交的登录请求进行校验，然后返回后续鉴权使用的token
     *
     * @param email    用户注册使用的邮箱
     * @param password 密码
     * @return 响应结果
     */
    @Override
    public Object authLoginRequest(String email, String password) {

        UserInfo userInfo = userInfoMapper.getUserInfoByEmail(email);
        String answer = userInfo.getPassword();

        if (answer == null) {
            throw new RuntimeException("对应此邮箱的用户不存在！");
        }

        if (! answer.equals(password)) {
            throw new RuntimeException("密码校验错误！");
        }

        String token = tokenVerifier.generateToken(Map.of("userId", userInfo.getUserId()));

        return Map.of("token", token);
    }

}
