package cn.kawauso.service.impl;

import cn.kawauso.mapper.UserInfoMapper;
import cn.kawauso.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * {@link LoginServiceImpl}是{@link LoginService}的默认实现类
 *
 * @author RealDragonking
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final UserInfoMapper userInfoMapper;

    public LoginServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
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

        String answer = userInfoMapper.getUserPassword(email);

        if (answer == null) {
            throw new RuntimeException("对应此邮箱的用户不存在！");
        }

        if (! answer.equals(password)) {
            throw new RuntimeException("密码校验错误！");
        }

        return "ok";
    }

}
