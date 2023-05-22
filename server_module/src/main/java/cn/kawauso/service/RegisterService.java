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

}
