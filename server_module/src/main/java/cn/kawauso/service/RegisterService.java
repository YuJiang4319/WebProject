package cn.kawauso.service;

import cn.kawauso.entity.UserInfo;

/**
 * {@link RegisterService}提供了注册服务相关的接口方法
 *
 * @author RealDragonking
 */
public interface RegisterService {

    /**
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 邮箱
     * @return 响应结果
     */
    Object sendEmailCode(String email);

    /**
     * 提交申请注册的邮箱和接收到的验证码，检验是否合法
     *
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    Object authEmailCode(String email, String emailCode);

    /**
     * 提交账号初始化token和相关信息，注册一个新的账号
     *
     * @param token 账号初始化的token，也是用户id
     * @param userInfo {@link UserInfo}，包含了用户的部分信息
     * @return 响应结果
     */
    Object registerNewAccount(long token, UserInfo userInfo);

}
