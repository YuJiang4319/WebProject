package cn.kawauso.service;

/**
 * {@link RegisterService}提供了注册服务相关的接口方法
 *
 * @author RealDragonking
 */
public interface RegisterService {

    /**
     * 启动注册一个新账号的流程，向已经进行过预检验的邮箱，发送包含验证码的邮件，并将其加入到验证码等候队列中
     *
     * @param mail 邮箱
     * @return 响应结果
     */
    Object registerNewAccount(String mail);

}
