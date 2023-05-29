package cn.kawauso.service;

/**
 * {@link LoginService}提供了登录服务相关的注册方法
 *
 * @author RealDragonking
 */
public interface LoginService {

    /**
     * 对提交的登录请求进行校验，然后返回后续鉴权使用的token
     *
     * @param email 用户注册使用的邮箱
     * @param password 密码
     * @return 响应结果
     */
    Object authLoginRequest(String email, String password);

}
