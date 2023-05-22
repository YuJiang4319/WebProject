package cn.kawauso.entity;

/**
 * {@link UserInfo}是一个实体类，记录了用户的基本信息
 *
 * @author RealDragonking
 */
public final class UserInfo {

    private long userId;
    private String password;
    private String email;

    /**
     * @return 用户id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return 用户注册使用的邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户注册使用的邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户的密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
