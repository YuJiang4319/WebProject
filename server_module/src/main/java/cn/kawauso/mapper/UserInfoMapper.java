package cn.kawauso.mapper;

import cn.kawauso.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * {@link UserInfoMapper}提供了一些对于SQL数据库的用户数据操作方法
 *
 * @author RealDragonking
 */
@Mapper
public interface UserInfoMapper {

    /**
     * 获取到指定邮箱的用户数量
     *
     * @param email 邮箱
     * @return 对应此邮箱的用户数量
     */
    int getUsersWithEmail(String email);

    /**
     * 插入{@link UserInfo}记录的用户信息，注册一个新的用户
     *
     * @param userInfo {@link UserInfo}
     */
    void insertUserInfo(UserInfo userInfo);

    /**
     * 获取到指定邮箱的用户密码
     *
     * @param email 邮箱
     * @return 对应此邮箱的用户密码
     */
    String getUserPassword(String email);

}
