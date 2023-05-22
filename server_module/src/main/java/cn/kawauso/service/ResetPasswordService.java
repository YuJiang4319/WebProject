package cn.kawauso.service;

import cn.kawauso.entity.UserInfo;

/**
 * {@link ResetPasswordService}提供了重置密码服务相关的接口方法
 *
 * @author RealDragonking
 */
public interface ResetPasswordService {

    /**
     * 启动重置密码的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param email 已经进行过注册的邮箱
     * @return 响应结果
     */
    Object sendEmailCode(String email);

    /**
     * 提交申请重置密码的邮箱和接收到的验证码，校验是否合法
     *
     * @param email 已经进行过注册的邮箱
     * @param emailCode 邮箱验证码
     * @return 响应结果
     */
    Object authEmailCode(String email, String emailCode);

    /**
     * 提交用户id和新设置的密码，重置账号密码
     *
     * @param token 为账号密码重置提供鉴权支持的token
     * @param userInfo {@link UserInfo}，仅包含新设置的密码
     * @return 响应结果
     */
    Object resetPassword(String token, UserInfo userInfo);

}
