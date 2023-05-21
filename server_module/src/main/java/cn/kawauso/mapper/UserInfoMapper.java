package cn.kawauso.mapper;

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
     * @param mail 邮箱
     * @return 对应此邮箱的用户数量
     */
    int getUsersWithMail(String mail);

}
