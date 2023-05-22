package cn.kawauso;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * {@link Application}是整个spring应用的启动入口
 *
 * @author RealDragonking
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 初始化创建{@link RedisClient}
     *
     * @param host ip地址
     * @param port 端口号
     * @param password 密码
     * @return {@link RedisClient}
     */
    @Bean(destroyMethod = "close")
    public RedisClient initRedisClient(@Value("${spring.redis.host}") String host,
                                       @Value("${spring.redis.port}") int port,
                                       @Value("${spring.redis.password}") String password) {
        RedisURI redisURI = RedisURI.builder()
                .withAuthentication("default", password)
                .withDatabase(0)
                .withHost(host)
                .withPort(port)
                .build();
        return RedisClient.create(redisURI);
    }

    /**
     * 初始化创建{@link RedisCommands}
     *
     * @param redisClient {@link RedisClient}
     * @return {@link RedisClient}
     */
    @Bean
    public RedisCommands<String, String> initRedisCommands(RedisClient redisClient) {
        return redisClient.connect().sync();
    }

}
